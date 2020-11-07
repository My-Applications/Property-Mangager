package dey.sayantan.property.management.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dey.sayantan.property.management.exceptions.DocumentProcessingException;
import dey.sayantan.property.management.model.Payment;
import dey.sayantan.property.management.model.Tenant;

public class PDFCreator {
	private PDDocument pdfDocument;
	private PDPage page;
	private PDRectangle pageSize;
	private PDFont font;
	private PDPageContentStream contentStream;
	private float nextYPosition;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final int SMALL_FONT_SIZE = 10;
	private static final int MEDIUM_FONT_SIZE = 14;
	private static final int LARGE_FONT_SIZE = 18;
	private static final String lineSeparator = System.getProperty("line.separator");

	public PDFCreator() {
		pdfDocument = new PDDocument();
		page = new PDPage();
		pageSize = page.getMediaBox();
		font = PDType1Font.HELVETICA_BOLD;
		try {
			contentStream = new PDPageContentStream(pdfDocument, page);
			contentStream.setFont(font, MEDIUM_FONT_SIZE);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new DocumentProcessingException("Error while generating payments documents");
		}
	}

	public byte[] generateTenantSpecificPayslip(Tenant tenant, List<Payment> paymentList) {

		try {
			contentStream.beginText();
			showTenantDetails(tenant);
			showPaymentDetails(paymentList);
			contentStream.endText();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			contentStream.close();
			pdfDocument.save(outputStream);
			byte[] byteArray = outputStream.toByteArray();
			pdfDocument.close();
			return byteArray;
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new DocumentProcessingException("Error while generating payments documents");
		}

	}

	private void showPaymentDetails(List<Payment> paymentList) throws IOException {
		float smallStringHeight = getStringHeight(MEDIUM_FONT_SIZE);
		float mediumStringHeight = getStringHeight(MEDIUM_FONT_SIZE);

		/* Header for tenant description */
		String header = "Payment Details";
		float startingPosition = getHeaderStartingPosition(header, MEDIUM_FONT_SIZE);
		contentStream.setFont(font, MEDIUM_FONT_SIZE);
		contentStream.setTextMatrix(
				Matrix.getTranslateInstance(startingPosition, pageSize.getHeight() - (4 * mediumStringHeight) / 1000f));
		contentStream.showText(header);

		/* Payment Details */
		contentStream.setFont(font, SMALL_FONT_SIZE);
		final float start_x = 40.0f;
		StringBuilder paymentDetailsString = new StringBuilder();
		for (Payment payment : paymentList) {
			paymentDetailsString.append("\t").append("Payment Type ").append(payment.getPaymentType()).append("\n");
			paymentDetailsString.append("\t").append("Payment Amount ").append(payment.getPaymentAmount()).append("\n");
			paymentDetailsString.append("\t").append("Payment Date ").append(payment.getPaymentDate()).append("\n");
			displayText(paymentDetailsString.toString(), start_x, nextYPosition, SMALL_FONT_SIZE);
			paymentDetailsString.setLength(0);

		}
	}

	private void showTenantDetails(Tenant tenant) throws IOException {
		float smallStringHeight = getStringHeight(MEDIUM_FONT_SIZE);
		float mediumStringHeight = getStringHeight(MEDIUM_FONT_SIZE);

		/* Header for tenant description */
		String header = "Tenant Details";
		float startingPosition = getHeaderStartingPosition(header, MEDIUM_FONT_SIZE);
		contentStream.setFont(font, MEDIUM_FONT_SIZE);
		contentStream.setTextMatrix(
				Matrix.getTranslateInstance(startingPosition, pageSize.getHeight() - (4 * mediumStringHeight) / 1000f));
		contentStream.showText(header);
		nextYPosition = pageSize.getHeight() - (6 * mediumStringHeight) / 1000f;

		/* Tenant Details */
		contentStream.setFont(font, SMALL_FONT_SIZE);
		StringBuilder tenantDetailsString = new StringBuilder();
		tenantDetailsString.append("\t").append("Name ").append(tenant.getTenantName()).append("\n");
		tenantDetailsString.append("\t").append("Address ").append(tenant.getPermanentAddress()).append("\n");
		tenantDetailsString.append("\t").append("Contact Number ").append(tenant.getDateOfBirth()).append("\n");
		startingPosition = 40;
		displayText(tenantDetailsString.toString(), startingPosition, nextYPosition, SMALL_FONT_SIZE);

	}

	private void displayText(String string, float startingPositionX, float startingPositionY, int fontSize)
			throws IOException {
		float adjustFact = (float) (fontSize / 1000);
		contentStream.setTextMatrix(Matrix.getTranslateInstance(startingPositionX, startingPositionY));
		float spaceWidth = font.getSpaceWidth();
		contentStream.setTextMatrix(Matrix.getTranslateInstance(startingPositionX, startingPositionY));
		String[] lines = string.split("\\n");
		for (String line : lines) {
			startingPositionX = 40;
			if (line.startsWith("\\t")) {
				startingPositionX += spaceWidth * 4 * adjustFact;
			}
			String[] textSegments = line.split("\\s");
			// insert tabs
			for (String textSegment : textSegments) {
				contentStream.setTextMatrix(Matrix.getTranslateInstance(startingPositionX, startingPositionY));
				contentStream.showText(textSegment);
				startingPositionX += (font.getStringWidth(textSegment) + spaceWidth) * adjustFact;
			}
			startingPositionY -= 10;
		}
		nextYPosition = startingPositionY;
	}

	private float getStringHeight(int mediumFontSize) {
		return font.getFontDescriptor().getFontBoundingBox().getHeight() * mediumFontSize;
	}

	private float getHeaderStartingPosition(String header, int fontSize) throws IOException {
		float stringWidth = getStringWidth(header, fontSize);
		float spaceLeft = pageSize.getWidth() * 1000f - stringWidth;
		return spaceLeft / 2 * 1000;
	}

	private float getStringWidth(String string, int fontSize) throws IOException {
		return font.getStringWidth(string) * fontSize;
	}

}
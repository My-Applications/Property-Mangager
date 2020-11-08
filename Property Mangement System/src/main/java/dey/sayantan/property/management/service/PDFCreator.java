package dey.sayantan.property.management.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
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
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-uuu");
	private static final String lineSeparator = System.getProperty("line.separator");

	public PDFCreator() {
		pdfDocument = new PDDocument();
		page = new PDPage(PDRectangle.A4);
		pdfDocument.addPage(page);
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
		int numberOfFieldsToBeDisPlayed = 3;
		/* Header for tenant description */
		String header = "Payment Details";
		float startingPositionX = getHeaderStartingPosition(header, MEDIUM_FONT_SIZE);
		contentStream.setFont(font, MEDIUM_FONT_SIZE);
		nextYPosition -= MEDIUM_FONT_SIZE;
		displayText(header, startingPositionX, nextYPosition, MEDIUM_FONT_SIZE);

		/* Payment Details */
		contentStream.setFont(font, SMALL_FONT_SIZE);
		startingPositionX = 40.0f;
		StringBuilder paymentDetailsString = new StringBuilder();
		for (Payment payment : paymentList) {
			paymentDetailsString.append("\t").append("Payment Type ").append(payment.getPaymentType()).append("\n");
			paymentDetailsString.append("\t").append("Payment Amount ").append(payment.getPaymentAmount()).append("\n");
			paymentDetailsString.append("\t").append("Payment Date ").append(payment.getPaymentDate()).append("\n");
			if (nextYPosition <= (smallStringHeight / 1000 * (numberOfFieldsToBeDisPlayed + 1)))
				appendNewPage();
			displayText(paymentDetailsString.toString(), startingPositionX, nextYPosition, SMALL_FONT_SIZE);
			paymentDetailsString.setLength(0);

		}
	}

	private void showTenantDetails(Tenant tenant) throws IOException {
		float mediumStringHeight = getStringHeight(MEDIUM_FONT_SIZE);

		/* Header for tenant description */
		String tenantHeader = "Tenant Details";
		float startingPositionX = getHeaderStartingPosition(tenantHeader, MEDIUM_FONT_SIZE);
		contentStream.setFont(font, MEDIUM_FONT_SIZE);
		nextYPosition = pageSize.getHeight() - (2 * mediumStringHeight) / 1000f;
		displayText(tenantHeader, startingPositionX, nextYPosition, MEDIUM_FONT_SIZE);

		/* Tenant Details */
		contentStream.setFont(font, SMALL_FONT_SIZE);
		StringBuilder tenantDetailsString = new StringBuilder();
		tenantDetailsString.append("\t").append(tenant.getTenantName()).append("\n");
		tenantDetailsString.append("\t").append(tenant.getPermanentAddress()).append("\n");
		tenantDetailsString.append("\t").append(tenant.getDateOfBirth().format(formatter)).append("\n");
		tenantDetailsString.append("\t").append("( " + tenant.getCountryCode() + " ) ")
				.append(formatContactNumber(tenant.getContactNumber())).append("\n");
		startingPositionX = 40;
		displayText(tenantDetailsString.toString(), startingPositionX, nextYPosition, SMALL_FONT_SIZE);

	}

	private void displayText(String string, float startingPositionX, float startingPositionY, int fontSize)
			throws IOException {
		float current_x = startingPositionX;
		float adjustFact = ((float) fontSize) / 1000;
		contentStream.setTextMatrix(Matrix.getTranslateInstance(current_x, startingPositionY));
		float spaceWidth = font.getSpaceWidth();
		contentStream.setTextMatrix(Matrix.getTranslateInstance(current_x, startingPositionY));
		String[] lines = string.split("\\n");
		for (String line : lines) {
			current_x = startingPositionX;
			if (line.startsWith("\t")) {
				current_x += spaceWidth * 4 * adjustFact;
			}
			String[] textSegments = line.split("\\s");
			// insert tabs
			for (String textSegment : textSegments) {
				contentStream.setTextMatrix(Matrix.getTranslateInstance(current_x, startingPositionY));
				contentStream.showText(textSegment);
				current_x += (font.getStringWidth(textSegment) + spaceWidth) * adjustFact;
			}
			startingPositionY -= getStringHeight(fontSize) / 1000;
		}
		nextYPosition = startingPositionY - fontSize;
	}

	private float getStringHeight(int mediumFontSize) {
		return font.getFontDescriptor().getFontBoundingBox().getHeight() * mediumFontSize;
	}

	private float getHeaderStartingPosition(String header, int fontSize) throws IOException {
		float stringWidth = getStringWidth(header, fontSize);
		float spaceLeft = pageSize.getWidth() * 1000f - stringWidth;
		return spaceLeft / (2 * 1000);
	}

	private float getStringWidth(String string, int fontSize) throws IOException {
		return font.getStringWidth(string) * fontSize;
	}

	private void appendNewPage() {

		page = new PDPage(PDRectangle.A4);
		pdfDocument.addPage(page);
		pageSize = page.getMediaBox();
		font = PDType1Font.HELVETICA_BOLD;
		try {
			contentStream.endText();
			contentStream.close();
			contentStream = new PDPageContentStream(pdfDocument, page);
			contentStream.setFont(font, SMALL_FONT_SIZE);
			contentStream.beginText();
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new DocumentProcessingException("Error while generating payments documents");
		}
		nextYPosition = pageSize.getHeight() - (2 * getStringHeight(LARGE_FONT_SIZE) / 1000f);

	}

	private Object formatContactNumber(long contactNumber) {
		return String.valueOf(contactNumber).replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1 $2 $3");
	}

}
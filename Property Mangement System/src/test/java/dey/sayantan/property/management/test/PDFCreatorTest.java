package dey.sayantan.property.management.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import dey.sayantan.property.management.model.Payment;
import dey.sayantan.property.management.model.Tenant;
import dey.sayantan.property.management.service.PDFCreator;

/**
 * The test of PDFCreator is made to see the whole PDF generation procedure , so
 * this test also depends on the PDFBOX's api it calls
 */
@RunWith(MockitoJUnitRunner.class)
public class PDFCreatorTest {

	PDFCreator pdfCreator = new PDFCreator();

	@Test
	public void getTenantSpecificPayslip_whenMultiplePayslipsPresent() {
		Tenant mockedTenant = getMockedTenant();
		List<Payment> mockedpayments = getMockedPayments();
		byte[] byteArray = pdfCreator.generateTenantSpecificPayslip(mockedTenant, mockedpayments);
		try {
			FileOutputStream fos = new FileOutputStream("src/test/resources/PDFTestResult.pdf");
			fos.write(byteArray);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		Assert.assertNotNull(byteArray);

	}

	private List<Payment> getMockedPayments() {
		List<Payment> payments = new ArrayList<Payment>();
		Payment payment1 = new Payment(UUID.randomUUID(), "CASH", 1000, LocalDate.now());
		Payment payment2 = new Payment(UUID.randomUUID(), "CARD", 1800, LocalDate.now());
		for (int i = 20; i >= 0; i--)
			payments.add(payment1);
		payments.add(payment2);
		return payments;
	}

	private Tenant getMockedTenant() {
		return new Tenant("AVON_BDALE", "Avon Barksdale", LocalDate.of(1980, 10, 12),
				"49/8 Kingston Avenue, Downtown, New State", 6802564809L, "+123");
	}
}

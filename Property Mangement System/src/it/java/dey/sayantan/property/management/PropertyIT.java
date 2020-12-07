package dey.sayantan.property.management;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import dey.sayantan.property.management.model.Property;
import io.restassured.RestAssured;
import io.restassured.response.Response;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class PropertyIT {

	private static final String URI = "http://localhost";
	private Property propertyTestObject;
	private String propertyId = "test-id";

	@LocalServerPort
	int port;

	@Before
	public void setUp() {
		RestAssured.baseURI = URI;
		RestAssured.port = port;
	}

	@Test
	public void createProperty() throws InterruptedException {
		Response response = with().header("Content-Type", "application/json").body(getPropertyData())
				.post("/CreateProperty");
		System.out.println(
				"time taken to fetch the response " + response.timeIn(TimeUnit.MILLISECONDS) + " milliseconds");
		response.then().assertThat().statusCode(200);
		response.then().body("propertyId", equalTo(propertyTestObject.getPropertyId()))
				.body("propertyCost", equalTo(propertyTestObject.getPropertyCost()))
				.body("propertyType", containsString("type"))
				.body("description", equalTo(propertyTestObject.getDescription()))
				.body("capacity", equalTo(propertyTestObject.getCapacity()));
	}

	@Test
	public void getPropertyById() throws InterruptedException {
		Response response = given().accept("application/json").get("/PropertyViews/" + propertyId);
		System.out.println(
				"time taken to fetch the response " + response.timeIn(TimeUnit.MILLISECONDS) + " milliseconds");
		response.then().assertThat().statusCode(200);
		response.then().body("propertyId", equalTo(propertyTestObject.getPropertyId()))
				.body("propertyCost", equalTo(propertyTestObject.getPropertyCost()))
				.body("propertyType", containsString("type"))
				.body("description", equalTo(propertyTestObject.getDescription()))
				.body("capacity", equalTo(propertyTestObject.getCapacity()));
	}

	private Property getPropertyData() {
		return propertyTestObject = new Property(propertyId, " test property type", 1000000, "test description", 2);
	}

}

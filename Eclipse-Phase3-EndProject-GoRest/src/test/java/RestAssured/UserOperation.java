package RestAssured;

import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.UUID;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UserOperation {
	
HashMap<String, String> userdata = new HashMap<String, String>();
	
	UUID uuid = UUID.randomUUID();	
	String userid;
	Logger logger;
	
	@BeforeSuite
	public void createPayLoad() {
		logger = Logger.getLogger("LogDemo");
		PropertyConfigurator.configure("log4j.properties");
		logger.setLevel(Level.DEBUG);
		userdata.put("name", "Kapil Dev");
		userdata.put("email", uuid + "@gmail.com");
		userdata.put("gender", "male");
		userdata.put("status", "active");
		RestAssured.baseURI = "https://gorest.co.in";
		RestAssured.basePath = "/public/v2/users";
	}
	
	@Test(priority=0)
	public void listOfUsers() {
		RestAssured
		.given()
			.contentType("application/json")
			.header("Authorization", "Bearer ed09268f146f607460115785587b8fdf0c759828ac6a61f24abba107e1f5b306")
		.when()
			.get("https://gorest.co.in/public/v2/users")
		.then()
			.assertThat()
			.statusCode(200);
		logger.debug("To get list of users - Status code is 200 - OK");
	}
	
	@Test(priority=1)
	public void addNewUser() {
		Response response = RestAssured
							.given()
								.contentType("application/json")
								.body(userdata)
								.header("Authorization", "Bearer ed09268f146f607460115785587b8fdf0c759828ac6a61f24abba107e1f5b306")
							.when()
								.post()
							.then()
								.assertThat()
								.statusCode(201)
								.log().all()
								.extract().response();
		logger.debug("For Operation Add New User - Status code is 201 - OK");
		JsonPath jsonPath = response.jsonPath();
		userid = jsonPath.getString("id").toString();

	}
	
	@Test(priority=2)
	public void validateNewUser() {
		RestAssured
		.given()
			.contentType("application/json")
			.header("Authorization", "Bearer ed09268f146f607460115785587b8fdf0c759828ac6a61f24abba107e1f5b306")
		.when()
			.get("https://gorest.co.in/public/v2/users/" + userid)
		.then()
			.assertThat()
			.statusCode(200)
		.and()
			.body("name", is("Kapil Dev"));
		logger.debug("For newly added user - Status code is 200 - OK");
		logger.debug("New User Verified");
	}

}

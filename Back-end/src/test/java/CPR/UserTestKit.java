package CPR;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import CPR.Boundary.NewUserBoundary;
import CPR.Boundary.UserBoundary;
import CPR.Data.UserRole;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

public class UserTestKit {
	private int port;
	// setup helper object to invoke HTTP requests
	private RestTemplate client;
	// setup a String used to represent the URL used to access the server
	private String url;
	private String deleteUsersURL;
	private String test_Username, test_Password;

	// get random port used by server
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}


	@PostConstruct
	public void initTestCase() {
		this.url = "http://localhost:" + this.port +"/api/users" ;
		this.client = new RestTemplate();
		this.test_Username = "testUser";
		this.test_Password = "testPassword";
		this.deleteUsersURL = "http://localhost:" + this.port + "/api/users/{username}";

	}
	
	@BeforeEach 
	public void setUp() {
		//create test user
		this.client.postForObject(this.url, new CPR.Boundary.NewUserBoundary(
				"admin", test_Password, UserRole.ADMIN.name()),
				CPR.Boundary.NewUserBoundary.class);
	}
	
	@AfterEach 
	public void tearDown() {
		
		this.client
		.delete(this.url);
	}
	
	//GIVEN
		//the server is up
	//WHEN
		//I invoke the POST request through the URL: /api/users
		//Inserting new UserBoundary
	//THEN
		//the respond status is 200(OK) and we get user boundary
	@Test
	void testCreateUser() {
			
		NewUserBoundary newUserBoundary=new NewUserBoundary(test_Username, test_Password, UserRole.USER.name());
			
		UserBoundary userBoundary = this.client.postForObject(this.url, newUserBoundary, UserBoundary.class);
			
		assertThat(this.client
				.postForObject(this.url + "/login" , userBoundary, UserBoundary.class))
				.isNotNull()
				.isEqualTo(userBoundary);
		}
	//GIVEN
		//the server is up
	//WHEN
		//I Invoke the POST request through the URL: /api/users
		//Inserting new userboundary with null username
	//THEN
		//the response status is 400(BAD_REQUEST) and we get exception
	@Test
	void testFailedCreateUser() {
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
	    
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		NewUserBoundary newUserBoundary=new NewUserBoundary(null, test_Password, UserRole.USER.name()); // null username
	    
	    HttpEntity<Object> entity = new HttpEntity<Object>(newUserBoundary,headers);
		
	
		Assertions.assertThrows( HttpClientErrorException.class, ()->{
			restTemplate.exchange(this.url + "/login", HttpMethod.POST ,entity ,NewUserBoundary.class);},"expected error code 400, user created instead"
				);
	    

		}
	
	//GIVEN
		//The server is up
	//WHEN
		//I Invoke the POST request through the URL: /api/users/login
		//Inserting a User boundary of existing user
	//THEN
		//the response status is 200(OK) and we get user boundary
	@Test
	void testLogin() {
		
		UserBoundary test_Admin_User = new UserBoundary("admin", UserRole.ADMIN.name(), test_Password);
		
		assertThat(this.client
				.postForObject(this.url + "/login" ,test_Admin_User, UserBoundary.class))
				.isNotNull()
				.isEqualTo(test_Admin_User);
	}
	
	//GIVEN
		//the server is up
	//WHEN
		//I Invoke the POST request through the URL: /api/users
		//Inserting user boundary with password that is different from one in db
	//THEN
		//the response status is 404(NOT_FOUND) and we get exception
	@Test
	void testFailedToLogin() {
		boolean thrown = false;

		UserBoundary test_user =new UserBoundary(test_Username, test_Password, UserRole.USER.name());
				
		try {
		this.client.postForObject(this.url + "/login" , test_user, UserBoundary.class);
		
		}catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			
			assertThat(httpClientOrServerExc.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
			thrown = true;
		}
		
		if (!thrown) {
			fail("no exception was thrown");
		}	

	}

	
	
	// Try to update an existing user's password.
	//GIVEN
		//the server is up
	//WHEN
		//I Invoke the PUT request through the URL: /api/users
		//Inserting user boundary with updated password
	//THEN
		//the response status is 200(OK)
	@Test
	void testUpdateUser() {
		
		NewUserBoundary newUserBoundary=new NewUserBoundary(test_Username, test_Password, "USER");
		
		UserBoundary userBoundary= this.client.postForObject(this.url, newUserBoundary, UserBoundary.class);

		//Update user:
		userBoundary.setRole(UserRole.ADMIN.name());
		userBoundary.setPassword("updated_Password");
		
		this.client.put(this.url, userBoundary);
		
		
		assertThat(this.client
				.postForObject(this.url + "/login" , userBoundary, UserBoundary.class))
				.isNotNull()
				.isEqualTo(userBoundary);

	}
	
	// Trying to update a user's username to a username not existing,  if a user with the username "updated_User" already exists this will update that one's password.
	// NOTE Might want to update in the future to add a token requirement that fits the user trying to be updated.
	//GIVEN
		//the server is up
	//WHEN
		//I Invoke the PUT request through the URL: /api/users
		//Inserting user boundary with username that doesn't exist in db
	//THEN
		//the response status is 404(NOT_FOUND) and we get exception
	@Test
	void testFailedUpdateUser() {
		boolean thrown = false;
		
		NewUserBoundary newUserBoundary=new NewUserBoundary(test_Username, test_Password, UserRole.USER.name());
		
		UserBoundary userBoundary= this.client.postForObject(this.url, newUserBoundary, UserBoundary.class);

		//Update user:
		userBoundary.setUsername("updated_User"); 
		userBoundary.setPassword("updated_Password");
		
		
		try {
			this.client.put(this.url, userBoundary);
			
			}catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
				
				assertThat(httpClientOrServerExc.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
				thrown = true;
			}
			
			if (!thrown) {
				fail("no exception was thrown");
			}	


	}
}

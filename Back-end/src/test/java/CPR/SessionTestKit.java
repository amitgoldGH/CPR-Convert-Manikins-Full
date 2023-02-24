package CPR;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import CPR.Boundary.SampleBoundary;
import CPR.Boundary.SessionBoundary;
import CPR.Data.SessionType;

import CPR.Finals.FINALS;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

public class SessionTestKit {
	private int port;
	// setup helper object to invoke HTTP requests
	private RestTemplate client;
	// setup a String used to represent the URL used to access the server
	private String url, url_calculate, url_calculateCpr, url_calculateBvm;
	private String test_Username, test_Password;
	private String url_sample, url_user;
	private String deleteURL_user;

	// get random port used by server
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}


	@PostConstruct
	public void initTestCase() {
		this.url = "http://localhost:" + this.port +"/api/sessions" ;
		this.url_sample = "http://localhost:" + this.port +"/api/samples" ;
		this.url_user = "http://localhost:" + this.port +"/api/users"; 
		this.url_calculate = "/calculate/";
		this.url_calculateCpr = "/calculate/cpr/";
		this.url_calculateBvm = "/calculate/bvm/";
		this.client = new RestTemplate();
		this.test_Username = "testUser";
		this.test_Password = "testPassword";
		this.deleteURL_user = url_user;


	}
	
	@BeforeEach 
	public void setUp() {
		//create test user
		this.client.postForObject(this.url_user, new CPR.Boundary.NewUserBoundary(
				test_Username,
				test_Password,
				CPR.Data.UserRole.USER.name()),
				CPR.Boundary.NewUserBoundary.class);
	}
	
	@AfterEach 
	public void tearDown() {
		this.client.delete(this.deleteURL_user);
		this.client
		.delete(this.url);
	}
	
	//GIVEN
		//the server is up
	//WHEN
		//I invoke the POST request through the URL: /api/sessions
		//Inserting SessionBoundary
	//THEN
		//the respond status is 200(OK) and we get session boundary with generated ID
	//THIS TESTS BOTH CREATE AND READ ASPECTS
	@Test
	void testCreateSession() {
		SessionBoundary test_Session = this.client
				.postForObject(this.url
						, new SessionBoundary(null, test_Username, SessionType.CPR.name(), new String[0], new Date())
						, SessionBoundary.class); 
		
		assertThat(this.client
				.getForObject(this.url + "/{id}", SessionBoundary.class, test_Session.getSessionId()))
				.isNotNull()
				.isEqualTo(test_Session);
	}
	
	//GIVEN
		//the server is up
	//WHEN
		//I invoke the POST request through the URL: /api/sessions
		//Inserting SessionBoundary with null username
	//THEN
		//the respond status is 400(BAD_REQUEST) and we get exception.
	@Test
	void testFailedCreateSession() {
		boolean thrown = false;
		
		try {
			this.client
			.postForObject(this.url
					, new SessionBoundary(null, null, null, new String[0], new Date())
					, SessionBoundary.class); 
			
		}catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			
			assertThat(httpClientOrServerExc.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
			thrown = true;
		}
		
		if (!thrown) {
			fail("no exception was thrown");
		}	
	}
	
	//GIVEN
		//the server is up
		//session boundary with same id exists already
	//WHEN
		//I invoke the PUT request through the URL: /api/sessions
		//Inserting valid SessionBoundary
	//THEN
		//the respond status is 200(OK) and we get updated session boundary
	@Test
	void testUpdateSession() {
		SessionBoundary test_Session = new SessionBoundary(null, test_Username, SessionType.CPR.name(), new String[0], new Date());
		test_Session = this.client.postForObject(this.url, test_Session, SessionBoundary.class); // Put in the new id
		
		test_Session.setMeasurementSummary(new String[]{"Updated Measure Summary"});
		test_Session.setType(SessionType.BVM.name());
		
		this.client.put(this.url, test_Session);
		
		assertThat(this.client
				.getForObject(this.url + "/{id}", SessionBoundary.class, test_Session.getSessionId()))
				.isNotNull()
				.isEqualTo(test_Session);
		
	}
	
	//GIVEN
		//the server is up
	//WHEN
		//I invoke the PUT request through the URL: /api/sessions
		//Inserting SessionBoundary with null id
	//THEN
		//the respond status is 400(BAD_REQUEST) and we get exception.
	@Test
	void testFailedUpdateSession() {
		boolean thrown = false;
		
		SessionBoundary test_Session = new SessionBoundary(null, test_Username, SessionType.CPR.name(), new String[0], new Date());
		
		try {
			this.client.put(this.url, test_Session);
			
			}catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
				
				assertThat(httpClientOrServerExc.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
				thrown = true;
			}
			
			if (!thrown) {
				fail("no exception was thrown");
			}	
	}
	
	//GIVEN
		//the server is up
		//no session with id 1 exists
	//WHEN
		//I invoke the GET request through the URL: /api/sessions/1
	//THEN
		// The response status is 404(NOT_FOUND) and we get exception.
	@Test
	void testFailGetSession() {
		boolean thrown = false;
		
		try {
			this.client.getForObject(this.url + "/{id}", SessionBoundary.class, "1");
			
			}catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
				
				assertThat(httpClientOrServerExc.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
				thrown = true;
			}
			
			if (!thrown) {
				fail("no exception was thrown");
			}	
	}
	
	//GIVEN
		//the server is up
		//session with samples exist
	//WHEN
		//I invoke the GET request through the URL: /api/sessions/calculate/cpr/session_id
	//THEN
		// The response status is 200 and the session with the id is updated
	@Test
	void testCalculateGoodCprSessionSummary() {
		SessionBoundary test_Session = this.client
				.postForObject(this.url
						, new SessionBoundary(null, test_Username, SessionType.CPR.name(), new String[0], new Date())
						, SessionBoundary.class);
		String test_Session_Id = test_Session.getSessionId();
		// Sample format is : DEPTH[0], RATE[1], LEFT_TOUCH[2], TOP_TOUCH[3], BOTTOM_TOUCH[4], RIGHT_TOUCH[5], CENTER_SENSOR[6]
		// GOOD STATS - (DEPTH: 4-6, RATE: 70-120, TOUCH: All but center: 0, center: 1)
		SampleBoundary[] test_sample_array = new SampleBoundary[10];
		test_sample_array[0] = new SampleBoundary(null, test_Session_Id, new String[] {"3", "100", "1","0","0","0", "1100"}); // BAD SAMPLE DEPTH + TOUCH
		test_sample_array[1] = new SampleBoundary(null, test_Session_Id, new String[] {"4", "119", "0","0","0","0", "400"}); // GOOD SAMPLE
		test_sample_array[2] = new SampleBoundary(null, test_Session_Id, new String[] {"4", "110", "0","0","0","0", "500"}); // GOOD SAMPLE
		test_sample_array[3] = new SampleBoundary(null, test_Session_Id, new String[] {"5", "160", "0","0","0","0", "700"}); // GOOD SAMPLE
		test_sample_array[4] = new SampleBoundary(null, test_Session_Id, new String[] {"2", "100", "1","0","0","0", "1500"}); // BAD SAMPLE DEPTH
		test_sample_array[5] = new SampleBoundary(null, test_Session_Id, new String[] {"0", "70", "1","0","0","0", "200"}); // BAD SAMPLE TOUCH + RATE + DEPTH
		test_sample_array[6] = new SampleBoundary(null, test_Session_Id, new String[] {"0", "50", "1","0","1","0", "1600"}); // BAD SAMPLE TOUCH + RATE + DEPTH
		test_sample_array[7] = new SampleBoundary(null, test_Session_Id, new String[] {"5", "100", "0","0","0","0", "1005"}); // GOOD SAMPLE
		test_sample_array[8] = new SampleBoundary(null, test_Session_Id, new String[] {"5", "100", "0","0","0","0", "1200"}); // GOOD SAMPLE
		test_sample_array[9] = new SampleBoundary(null, test_Session_Id, new String[] {"4", "100", "0","0","0","0", "1150"}); // GOOD SAMPLE
		for (SampleBoundary sampleBoundary : test_sample_array) {
			this.client.postForObject(this.url_sample, sampleBoundary, SampleBoundary.class);
		}
		
		// 6 out of 10 good samples 
		SessionBoundary result_Session = this.client.getForObject(this.url + this.url_calculate + test_Session_Id, SessionBoundary.class);
		String[] result_Array = result_Session.getMeasurementSummary();
		// Make sure I got a result array with 4 strings, [0] for depth score, [1] for cpr rate, [2] for touching sides score [3] for center touch score
		// result = 0 means didn't pass, 1 = passed.
		assertTrue(
				result_Array.length >= 4 
				&& result_Array[0].equals(FINALS.PASSING_MESSAGE) 
				&& result_Array[1].equals(FINALS.PASSING_MESSAGE) 
				&& result_Array[2].equals(FINALS.PASSING_MESSAGE) 
				&& result_Array[3].equals(FINALS.PASSING_MESSAGE)
				);
	}
	
	//GIVEN
			//the server is up
			//session with samples exist
		//WHEN
			//I invoke the GET request through the URL: /api/sessions/calculate/cpr/session_id
		//THEN
			// The response status is 200 and the session with the id is updated
		@Test
		void testCalculateBadCprSessionSummary() {
			SessionBoundary test_Session = this.client
					.postForObject(this.url
							, new SessionBoundary(null, test_Username, SessionType.CPR.name(), new String[0], new Date())
							, SessionBoundary.class);
			String test_Session_Id = test_Session.getSessionId();
			// Sample format is : DEPTH[0], RATE[1], LEFT_TOUCH[2], TOP_TOUCH[3], BOTTOM_TOUCH[4], RIGHT_TOUCH[5], CENTER_SENSOR[6]
			// GOOD STATS - (DEPTH: 4-6, RATE: 70-120, TOUCH: All but center: 0, center: 1)
			SampleBoundary[] test_sample_array = new SampleBoundary[10];
			test_sample_array[0] = new SampleBoundary(null, test_Session_Id, new String[] {"3", "100", "1","0","0","0", "1100"}); // BAD SAMPLE DEPTH + TOUCH
			test_sample_array[1] = new SampleBoundary(null, test_Session_Id, new String[] {"4", "19", "0","0","0","0", "400"}); // GOOD SAMPLE
			test_sample_array[2] = new SampleBoundary(null, test_Session_Id, new String[] {"0", "50", "1","0","1","0", "1600"}); // BAD SAMPLE TOUCH + RATE + DEPTH
			test_sample_array[3] = new SampleBoundary(null, test_Session_Id, new String[] {"0", "50", "1","0","1","0", "1600"}); // BAD SAMPLE TOUCH + RATE + DEPTH
			test_sample_array[4] = new SampleBoundary(null, test_Session_Id, new String[] {"2", "100", "1","0","0","0", "500"}); // BAD SAMPLE DEPTH
			test_sample_array[5] = new SampleBoundary(null, test_Session_Id, new String[] {"0", "70", "1","0","0","0", "200"}); // BAD SAMPLE TOUCH + RATE + DEPTH
			test_sample_array[6] = new SampleBoundary(null, test_Session_Id, new String[] {"0", "50", "1","0","1","0", "100"}); // BAD SAMPLE TOUCH + RATE + DEPTH
			test_sample_array[7] = new SampleBoundary(null, test_Session_Id, new String[] {"5", "10", "0","0","0","0", "1"}); // GOOD SAMPLE
			test_sample_array[8] = new SampleBoundary(null, test_Session_Id, new String[] {"5", "100", "0","0","0","0", "1"}); // GOOD SAMPLE
			test_sample_array[9] = new SampleBoundary(null, test_Session_Id, new String[] {"4", "100", "0","0","0","0", "1"}); // GOOD SAMPLE
			for (SampleBoundary sampleBoundary : test_sample_array) {
				this.client.postForObject(this.url_sample, sampleBoundary, SampleBoundary.class);
			}
			
			// 6 out of 10 good samples 
			SessionBoundary result_Session = this.client.getForObject(this.url + this.url_calculate + test_Session_Id, SessionBoundary.class);
			String[] result_Array = result_Session.getMeasurementSummary();
			// Make sure I got a result array with 4 strings, [0] for depth score, [1] for cpr rate, [2] for touching sides score [3] for center touch score
			// result = 0 means didn't pass, 1 = passed.
			assertTrue(
					result_Array.length >= 4 
					&& result_Array[0].equals(FINALS.FAILING_MESSAGE) 
					&& result_Array[1].equals(FINALS.FAILING_MESSAGE) 
					&& result_Array[2].equals(FINALS.FAILING_MESSAGE) 
					&& result_Array[3].equals(FINALS.FAILING_MESSAGE)
					);

		}
	
	//GIVEN
			//the server is up
			//session with samples exist
		//WHEN
			//I invoke the GET request through the URL: /api/sessions/calculate/bvm/session_id
		//THEN
			// The response status is 200 and the session with the id is updated
	@Test
	void testCalculateGoodBvmSessionSummary() {
		SessionBoundary test_Session = this.client
				.postForObject(this.url
						, new SessionBoundary(null, test_Username, SessionType.BVM.name(), new String[0], new Date())
						, SessionBoundary.class); 
		String test_Session_Id = test_Session.getSessionId();
		// Sample format is :  PRESSURE_RATE[0] (sample 10 times to get rate),AIRWAY[1], SEAL[2]
		// GOOD STATS - (PRESSURE 1 AIRWAY 1 SEAL 1)
		SampleBoundary[] test_sample_array = new SampleBoundary[10];
		test_sample_array[0] = new SampleBoundary(null, test_Session_Id, new String[] {"1", "1", "1"}); // GOOD SAMPLE
		test_sample_array[1] = new SampleBoundary(null, test_Session_Id, new String[] {"1", "1", "1"}); // GOOD SAMPLE
		test_sample_array[2] = new SampleBoundary(null, test_Session_Id, new String[] {"1", "1", "1"}); // GOOD SAMPLE
		test_sample_array[3] = new SampleBoundary(null, test_Session_Id, new String[] {"1", "1", "1"}); // GOOD SAMPLE
		test_sample_array[4] = new SampleBoundary(null, test_Session_Id, new String[] {"1", "1", "1"}); // GOOD SAMPLE
		test_sample_array[5] = new SampleBoundary(null, test_Session_Id, new String[] {"1", "1", "1"}); // GOOD SAMPLE
		test_sample_array[6] = new SampleBoundary(null, test_Session_Id, new String[] {"1", "1", "0"}); // BAD SAMPLE NOT SEALED
		test_sample_array[7] = new SampleBoundary(null, test_Session_Id, new String[] {"1", "0", "1"}); // BAD SAMPLE AIRWAY CLOSED
		test_sample_array[8] = new SampleBoundary(null, test_Session_Id, new String[] {"1", "0", "1"}); // BAD SAMPLE AIRWAY CLOSED
		test_sample_array[9] = new SampleBoundary(null, test_Session_Id, new String[] {"0", "1", "1"}); // BAD SAMPLE PRESSURE_RATE NOT GOOD
		
		// EXPECTED: RATE: 9/10, AIRWAY 8/10, SEAL 9/10 => GOOD, GOOD, GOOD.
		
		for (SampleBoundary sampleBoundary : test_sample_array) {
			this.client.postForObject(this.url_sample, sampleBoundary, SampleBoundary.class);
		}
		
		// 6 out of 10 good samples 
		SessionBoundary result_Session = this.client.getForObject(this.url + this.url_calculate + test_Session_Id, SessionBoundary.class);	
		String[] result_Array = result_Session.getMeasurementSummary();
		// Make sure I got a result array with 3 strings, [0] for pressure_rate score, [1] for airway score, [2] for mask seal score.
		// result = 0 means didn't pass, 1 = passed.
		assertTrue(
				result_Array.length >= 3 
				&& result_Array[0].equals(FINALS.PASSING_MESSAGE)
				&& result_Array[1].equals(FINALS.PASSING_MESSAGE) 
				&& result_Array[2].equals(FINALS.PASSING_MESSAGE));		
	}
	
	
	
}

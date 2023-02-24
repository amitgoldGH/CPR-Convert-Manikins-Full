package CPR;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import CPR.Boundary.SampleBoundary;
import CPR.Boundary.SessionBoundary;
import CPR.Boundary.UserBoundary;
import CPR.Data.SessionType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AdminTestKit {
	
	private int port;
	// setup helper object to invoke HTTP requests
	private RestTemplate client;
	// setup a String used to represent the URL used to access the server
	private String url_sample, url_user, url_session;
	private String deleteURL_user, deleteURL_sample, deleteURL_session;
	private String test_Username, test_Password, test_Session_Id;
	private String[] measurements, measurement_summary;

	// get random port used by server
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}


	@PostConstruct
	public void initTestCase() {
		this.url_user = "http://localhost:" + this.port +"/api/users" ;
		this.url_sample = "http://localhost:" + this.port +"/api/samples" ;
		this.url_session = "http://localhost:" + this.port +"/api/sessions" ;
		this.deleteURL_sample = url_sample;
		this.deleteURL_session = url_session;
		this.deleteURL_user = url_user;
		this.client = new RestTemplate();
		this.test_Username = "testUser";
		this.test_Password = "testPassword";
		this.test_Session_Id = "session_id";
		this.measurement_summary = new String[] {"Measure1 Summary", "Measure2 Summary"};
		this.measurements = new String[] {"Measurement1", "Measurement2"};
	}
	
	@BeforeEach 
	public void setUp() {
		//create Admin user
		this.client.postForObject(this.url_user, new CPR.Boundary.NewUserBoundary(
				test_Username,
				test_Password,
				CPR.Data.UserRole.USER.name()),
				CPR.Boundary.NewUserBoundary.class);
	}
	
	@AfterEach 
	public void tearDown() {
		this.client.delete(this.deleteURL_user);
		this.client.delete(this.deleteURL_sample);
		this.client.delete(this.deleteURL_session);
	}
	
	
	//GIVEN
		//Server is up
		//Server has admin user from postconstruct
	//WHEN
		//I invoke GET request through the URL: /api/users
	//THEN
		// I get user boundary array with admin user boundary in it
	@Test
	void testGetAllUsers() {
		assertThat(this.client.getForObject(this.url_user, UserBoundary[].class)).isNotNull().hasSize(1);
	}
	
	//GIVEN
		//Server is up
		//Server has sessions in it
	//WHEN
		//I invoke GET request through the URL: /api/users
	//THEN
		// I get session boundary array with 2 sessions in it
	@Test
	void testGetAllSessions() {
		SessionBoundary session1 = new SessionBoundary(null, test_Username, SessionType.CPR.name(), measurement_summary, new Date());
		SessionBoundary session2 = new SessionBoundary(null, test_Username, SessionType.BVM.name(), measurement_summary, new Date());
		
		session1 = this.client.postForObject(this.url_session, session1, SessionBoundary.class);
		session2 = this.client.postForObject(this.url_session, session2, SessionBoundary.class);
		
		SessionBoundary[] session_array = this.client.getForObject(this.url_session, SessionBoundary[].class);
		
		assertThat(session_array.length == 2 && session_array[0].equals(session1) && session_array[1].equals(session2));
		
	}
	
	//GIVEN
		//Server is up
		//Server has sessions in it
	//WHEN
		//I invoke GET request through the URL: /api/sessions/user/{username}
		//with username "testUser"
	//THEN
		// I get session boundary array with 2 sessions in it
	@Test
	void testGetAllSessionsByUser() {
		SessionBoundary session1 = new SessionBoundary(null, test_Username, SessionType.CPR.name(), measurement_summary, new Date());
		SessionBoundary session2 = new SessionBoundary(null, test_Username, SessionType.BVM.name(), measurement_summary, new Date());
		
		session1 = this.client.postForObject(this.url_session, session1, SessionBoundary.class);
		session2 = this.client.postForObject(this.url_session, session2, SessionBoundary.class);
		
		SessionBoundary[] session_array = this.client.getForObject(this.url_session + "/user/{username}"
				, SessionBoundary[].class, test_Username);
		
		assertThat(session_array.length == 2 && session_array[0].equals(session1) && session_array[1].equals(session2));
	}
	
	//GIVEN
		//Server is up
		//Server has samples in it
	//WHEN
		//I invoke GET request through the URL: /api/samples
	//THEN
		// I get sample boundary array with 2 samples in it
	@Test
	void testGetAllSamples() {
		SampleBoundary sample1 = new SampleBoundary(null, test_Session_Id, measurements);
		SampleBoundary sample2 = new SampleBoundary(null, test_Session_Id, measurements);
		
		sample1 = this.client.postForObject(this.url_sample, sample1, SampleBoundary.class);
		sample2 = this.client.postForObject(this.url_sample, sample2, SampleBoundary.class);
		
		SampleBoundary[] sample_array = this.client.getForObject(this.url_sample, SampleBoundary[].class);
		
		assertThat(sample_array.length == 2 && sample_array[0].equals(sample1) && sample_array[1].equals(sample2));

	}
	
	//GIVEN
		//Server is up
	//WHEN
		//I invoke DELETE request through the URL: /api/samples
	//THEN
		// I get empty sample boundary array
	@Test
	void testDeleteAllSamples() {
		
		SampleBoundary sample1 = new SampleBoundary(null, test_Session_Id, measurements);
		SampleBoundary sample2 = new SampleBoundary(null, test_Session_Id, measurements);
		
		//  POST samples to server
		sample1 = this.client.postForObject(this.url_sample, sample1, SampleBoundary.class);
		sample2 = this.client.postForObject(this.url_sample, sample2, SampleBoundary.class);
		
		// DELETE samples from server
		this.client.delete(this.url_sample);
		
		// GET all samples available
		SampleBoundary[] sample_array = this.client.getForObject(this.url_sample, SampleBoundary[].class);
		
		assertThat(sample_array.length == 0);
	}
	
	//GIVEN
		//Server is up
	//WHEN
		//I invoke DELETE request through the URL: /api/sessions
	//THEN
		// I get empty session boundary array
	@Test
	void testDeleteAllSessions() {
		
		SessionBoundary session1 = new SessionBoundary(null, test_Username, SessionType.CPR.name(), measurement_summary, new Date());
		SessionBoundary session2 = new SessionBoundary(null, test_Username, SessionType.BVM.name(), measurement_summary, new Date());
		// POST sessions to server
		session1 = this.client.postForObject(this.url_session, session1, SessionBoundary.class);
		session2 = this.client.postForObject(this.url_session, session2, SessionBoundary.class);
		
		// DELETE sessions from server
		this.client.delete(this.url_session);
		
		// GET all sessions available on server
		SessionBoundary[] session_array = this.client.getForObject(this.url_session, SessionBoundary[].class);
		
		assertThat(session_array.length == 0);
	}
	
	//GIVEN
		//Server is up
		//Server has admin user from postconstruct
	//WHEN
		//I invoke DELETE request through the URL: /api/users
	//THEN
		// I get an empty user boundary array
	@Test
	void testDeleteAllUsers() {
		
		// Server already has admin user from set up
		
		// DELETE all users from server
		this.client.delete(this.url_user);
		
		assertThat(this.client.getForObject(this.url_user, UserBoundary[].class)).isNotNull().hasSize(0);
	}
	
	
}

package CPR;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

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

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SampleTestKit {
	
	private int port;
	// setup helper object to invoke HTTP requests
	private RestTemplate client;
	// setup a String used to represent the URL used to access the server
	private String url;
	private String test_Session_Id;

	// get random port used by server
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}


	@PostConstruct
	public void initTestCase() {
		this.url = "http://localhost:" + this.port +"/api/samples" ;
		this.client = new RestTemplate();
		this.test_Session_Id = "1";
	}
	
	@BeforeEach 
	public void setUp() {
	}
	
	@AfterEach 
	public void tearDown() {
		this.client.delete(this.url);
	}
	
	
	@Test
	void testCreateSample() {
		String[] measures = new String[] {"Measure1","Measure2"};
		SampleBoundary test_sample = new SampleBoundary(null, test_Session_Id, measures);
		test_sample = this.client.postForObject(this.url, test_sample, SampleBoundary.class);
		
		assertThat(test_sample.getSampleId() != null 
				&& test_sample.getSessionId().equals(test_Session_Id) 
				&& test_sample.getMeasurements().equals(measures));
		
	}
	
	@Test
	void testFailedCreateSample() {
		boolean thrown = false;
		
		try {
			this.client
			.postForObject(this.url, 
					new SampleBoundary(null, null, new String[] {"Measure1","Measure2"})
					, SampleBoundary.class);
			
		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			
			assertThat(httpClientOrServerExc.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
			thrown = true;
		}
		if (!thrown) {
			fail("no exception was thrown");
		}	
	}
	
	@Test
	void testGetSamplesBySession() {
		SampleBoundary test_sample = new SampleBoundary(null, test_Session_Id, new String[] {"Measure1","Measure2"});
		test_sample = this.client.postForObject(this.url, test_sample, SampleBoundary.class); // gets generated id
		
		SampleBoundary[] response_array = this.client
				.getForObject(this.url + "/session/{id}"
						, SampleBoundary[].class, this.test_Session_Id); // get all samples with same session id
		
		assertThat(response_array.length == 1 && response_array[0].equals(test_sample));
	}

}

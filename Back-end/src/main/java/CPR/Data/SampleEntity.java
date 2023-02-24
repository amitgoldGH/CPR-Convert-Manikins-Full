package CPR.Data;

import java.util.Arrays;

import javax.persistence.Lob;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "SAMPLES")
/* SAMPLES table:
 * SAMPLEID				| SESSIONID			| MEASUREMENTS	|
 * VARCHAR(255) <PK>	| VARCHAR(255) <FK>	| CLOB			|
 * */
public class SampleEntity {
	private String sampleId;
	private String sessionId;
	private String[] measurements; // Might need to be int instead of String, depending on what the manikin transmits.
	
	public SampleEntity() {}
	
	public SampleEntity(String sample_Id, String session_Id, String[] measurements) {
		super();
		this.setSampleId(sample_Id);
		this.setSessionId(session_Id);
		this.setMeasurements(measurements);
	}

	@MongoId
	public String getSampleId() {
		return sampleId;
	}

	public void setSampleId(String sample_Id) {
		this.sampleId = sample_Id;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	@Lob
	public String[] getMeasurements() {
		return measurements;
	}

	public void setMeasurements(String[] measurements) {
		this.measurements = measurements;
	}

	@Override
	public String toString() {
		return "SampleEntity [sampleId=" + sampleId + ", sessionId=" + sessionId + ", measurements="
				+ Arrays.toString(measurements) + "]";
	}


	
	
}

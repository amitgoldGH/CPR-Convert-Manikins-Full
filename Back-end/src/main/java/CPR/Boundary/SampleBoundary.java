package CPR.Boundary;

import java.util.Arrays;
import java.util.Objects;

public class SampleBoundary {
	private String sampleId;
	private String sessionId;
	private String[] measurements;
	
	public SampleBoundary() {}
	
	public SampleBoundary(String sample_Id, String session_Id, String[] measurements) {
		this.setSampleId(sample_Id);
		this.setSessionId(session_Id);
		this.setMeasurements(measurements);
	}
	
	public String getSampleId() {
		return sampleId;
	}
	public void setSampleId(String sample_Id) {
		this.sampleId = sample_Id;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String session_Id) {
		this.sessionId = session_Id;
	}
	public String[] getMeasurements() {
		return measurements;
	}
	public void setMeasurements(String[] measurements) {
		this.measurements = measurements;
	}
	
	@Override
	public String toString() {
		return "SampleBoundary [sampleId=" + sampleId + ", sessionId=" + sessionId + ", measurements="
				+ Arrays.toString(measurements) + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(sampleId, sessionId, measurements);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SampleBoundary other = (SampleBoundary) obj;
		return Objects.equals(sampleId, other.sampleId)
				&& Objects.equals(sessionId, other.sessionId)
				&& Objects.equals(measurements, other.measurements);
	}
	
	
}

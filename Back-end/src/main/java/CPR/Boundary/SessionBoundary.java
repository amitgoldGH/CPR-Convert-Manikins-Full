package CPR.Boundary;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class SessionBoundary {

	private String sessionId;
	private String username;
	private String type;
	private String[] measurementSummary; // Calculated at end of session.
	private Date creation_Date;
	
	public SessionBoundary() {}
	
	public SessionBoundary(String session_Id, String username, String type, String[] measurement_Summary, Date creation_Date) {
		this.setSessionId(session_Id);
		this.setUsername(username);
		this.setType(type);
		this.setMeasurementSummary(measurement_Summary);
		this.setCreationDate(creation_Date);
	}
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String session_Id) {
		this.sessionId = session_Id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String[] getMeasurementSummary() {
		return measurementSummary;
	}
	public void setMeasurementSummary(String[] measurement_Summary) {
		this.measurementSummary = measurement_Summary;
	}
	public Date getCreationDate() {
		return creation_Date;
	}
	public void setCreationDate(Date creation_Date) {
		this.creation_Date = creation_Date;
	}
	
	@Override
	public String toString() {
		return "SessionBoundary [sessionId=" + sessionId + ", username=" + username + ", type=" + type
				+ ", measurementSummary=" + Arrays.toString(measurementSummary) + ", creation_Date=" + creation_Date
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(measurementSummary);
		result = prime * result + Objects.hash(creation_Date, sessionId, type, username);
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SessionBoundary other = (SessionBoundary) obj;
		return Objects.equals(creation_Date, other.creation_Date)
				&& Arrays.equals(measurementSummary, other.measurementSummary)
				&& Objects.equals(sessionId, other.sessionId) && Objects.equals(type, other.type)
				&& Objects.equals(username, other.username);
	}
	
	
	
	
}

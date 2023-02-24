package CPR.Data;

import java.util.Arrays;
import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "SESSIONS")
/* SESSIONS table:
 * ID				| USERNAME			| TYPE					| MEASUREMENT_SUMMARY	| CREATION_DATE
 * VARCHAR(255) <PK>| VARCHAR(255) <FK>	| ENUMERATED			| CLOB					| TIMESTAMP
 * */
public class SessionEntity {
	private String sessionId;
	private String username;
	private SessionType type;
	private String[] measurementSummary; // Calculated at end of session.
	private Date creationDate;
	
	public SessionEntity() {}
	
	public SessionEntity(String session_Id, String username, SessionType type, String[] measurement_Summary, Date creation_Date)
	{
		super();
		this.setSessionId(session_Id);
		this.setUsername(username);
		this.setType(type);
		this.setMeasurementSummary(measurement_Summary);
		this.setCreationDate(creation_Date);
	}
	
	public SessionEntity(String session_Id, String username, SessionType type, String[] measurement_Summary)
	{
		this(session_Id, username, type, measurement_Summary, new Date());
	}
	
	@MongoId
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
	@Enumerated(EnumType.STRING)
	public SessionType getType() {
		return type;
	}
	public void setType(SessionType type) {
		this.type = type;
	}
	@Lob
	public String[] getMeasurementSummary() {
		return measurementSummary;
	}
	public void setMeasurementSummary(String[] measurement_Summary) {
		this.measurementSummary = measurement_Summary;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creation_Date) {
		this.creationDate = creation_Date;
	}

	@Override
	public String toString() {
		return "SessionEntity [sessionId=" + sessionId + ", username=" + username + ", type=" + type
				+ ", measurementSummary=" + Arrays.toString(measurementSummary) + ", creationDate=" + creationDate
				+ "]";
	}

	
}

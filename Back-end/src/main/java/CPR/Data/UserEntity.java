package CPR.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "USERS")
/* USERS table:
 * ID (USERNAME)		| ROLE			| PASSWORD 		|
 * VARCHAR(255) <PK>	| ENUMERATED	| VARCHAR(255)	|
 * */
public class UserEntity {
	
	
	private String username;
	private UserRole role;
	private String password; // Needs to be hashed.
	
	public UserEntity() {}
	
	public UserEntity(String username, String hashed_Password, UserRole role)
	{
		super();
		this.username = username;
		this.password = hashed_Password;
		this.role = role;
	}
	
	@MongoId
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	@Enumerated(EnumType.STRING)
	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserEntity [username=" + username + ", role=" + role + ", password=" + password + "]";
	}

	
}

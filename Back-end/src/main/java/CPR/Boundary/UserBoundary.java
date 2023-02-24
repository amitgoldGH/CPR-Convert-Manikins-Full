package CPR.Boundary;

import java.util.Objects;

public class UserBoundary {
	
	private String username;
	private String role;
	private String password;
	
	public UserBoundary() {}
	
	public UserBoundary(String username, String role, String password) {
		super();
		this.setUsername(username);
		this.setRole(role);
		this.setPassword(password);
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
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
		return "UserBoundary [username=" + username + ", role=" + role + ", password=" + password + "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(password, role, username);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserBoundary other = (UserBoundary) obj;
		return Objects.equals(password, other.password) && Objects.equals(role, other.role)
				&& Objects.equals(username, other.username);
	}
	
	
}

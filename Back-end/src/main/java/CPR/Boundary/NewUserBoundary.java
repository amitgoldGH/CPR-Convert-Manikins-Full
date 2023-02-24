package CPR.Boundary;

public class NewUserBoundary {
	
	private String username;
	private String role;
	private String password;
	
	
	public NewUserBoundary() {}
	
	public NewUserBoundary(String username, String password, String role) {
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
		return "NewUserBoundary [username=" + username + ", role=" + role + ", password=" + password + "]";
	}
	
	
}

package CPR.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import CPR.Boundary.NewUserBoundary;
import CPR.Boundary.UserBoundary;

public interface UserService {
	
	//CREATE
	public Object createUser(NewUserBoundary new_User_Boundary);
	
	//RETRIEVE
	public Object login(UserBoundary user_Boundary);
	
	public Object getUser(String username);
	
	public List<Object> getAllUsers();
	
	//UPDATE
	public void updateUser(UserBoundary user_Boundary);
	
	//DELETE
	public void deleteAllUsers();
	
	public void deleteUser(String username);
}

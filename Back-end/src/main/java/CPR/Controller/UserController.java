package CPR.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import CPR.Boundary.NewUserBoundary;
import CPR.Boundary.UserBoundary;
import CPR.Service.UserService;

@RestController
public class UserController {
	
	private UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	//POST request, path="/api/users"
	//Accept: NewUserBoundary
	//Return: UserBoundary
	@RequestMapping(
			path = "/api/users",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Object createUser(@RequestBody NewUserBoundary new_User_Boundary) {
		System.out.println("USER_CONTROLLER /api/users CREATE USER TYPE_POST called " + new_User_Boundary.toString());
		
		return this.userService.createUser(new_User_Boundary);
	}
	
	//POST request, path="/api/users/login"
	//Accept: UserBoundary
	//Return: Object
	@RequestMapping(
			path="/api/users/login",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Object login(@RequestBody UserBoundary user_Boundary) {
		System.out.println("USER_CONTROLLER /api/users/login LOGIN TYPE_POST called LOGIN INFO: " + user_Boundary.toString());
		
		return this.userService.login(user_Boundary);
	}
	
	//GET request, path="/api/users/{username}"
	//Accept: Nothing
	//Return: UserBoundary
	@RequestMapping(
			path = "/api/users/{username}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Object getUser(@PathVariable("username") String username) {
		System.out.println("USER_CONTROLLER /api/users/" + username + " GET USER TYPE_GET called");
		
		return this.userService.getUser(username);
	}
	
	//PUT request, path="/api/users"
	//Accept: Updated UserBoundary of existing user
	//Return: Nothing
	@RequestMapping(
			path="/api/users",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateUser(@RequestBody UserBoundary user_Boundary) {
		System.out.println("USER_CONTROLLER /api/users UPDATE USER TYPE_PUT called " + user_Boundary.toString());
		this.userService.updateUser(user_Boundary);
	}
}

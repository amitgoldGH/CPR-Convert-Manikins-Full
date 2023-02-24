package CPR.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import CPR.Boundary.NewUserBoundary;
import CPR.Boundary.UserBoundary;
import CPR.Data.UserConverter;
import CPR.Data.UserEntity;
import CPR.Data.UserRole;
import CPR.Exception.UserBadRequestException;
import CPR.Exception.UserNotFoundException;

//@Service
public class UserServiceImplMockup implements UserService{

	private Map<String, UserEntity> storage;
	private UserConverter converter;
	
	@Autowired
	public UserServiceImplMockup(UserConverter converter) {
		this.converter = converter;
	}
	
	@PostConstruct
	public void init()
	{
		// initialize thread safe storage
		this.storage = Collections.synchronizedMap(new HashMap<>());
			
	}
	
	@Override
	public Object createUser(NewUserBoundary new_User_Boundary) {
		if (new_User_Boundary.getUsername() != null && new_User_Boundary.getPassword() != null)
		{
			if (storage.get(new_User_Boundary.getUsername()) != null)
			{
				return null;
				// TODO EXCEPTION USER ALREADY EXIST
			}
			else
			{
				UserEntity entity = new UserEntity(new_User_Boundary.getUsername(), new_User_Boundary.getPassword(), UserRole.valueOf(new_User_Boundary.getRole()));
				storage.put(entity.getUsername(), entity);
				System.out.println("Created and stored user successfully");
				return converter.convertToBoundary(entity);
			}
		}
		
		// TODO EXCEPTION Invalid username/password
		return null;
	}

	@Override
	public Object login(UserBoundary user_Boundary) {
		if (user_Boundary.getUsername() != null && user_Boundary.getPassword() != null)
		{
			UserEntity entity = storage.get(user_Boundary.getUsername());
			if (entity != null && entity.getPassword().equals(user_Boundary.getPassword()))
			{
				System.out.println(user_Boundary.getUsername() + " Login successful!");
				return user_Boundary;
			}
			else
			{
				throw new UserNotFoundException("User doesn't exist or password incorrect");
				// TODO EXCEPTION User doesnt exist or invalid passsword
			}
		}
		
		// TODO EXCEPTION Invalid input username/password
		throw new UserBadRequestException("Input username/password were null");
		
		}

	@Override
	public Object getUser(String username) {
		UserEntity entity = storage.get(username);
		if (entity == null)
		{
			// TODO EXCEPTION USER NOT FOUND
			return null;
		}
		else
			return converter.convertToBoundary(entity);
	}

	@Override
	public List<Object> getAllUsers() {
		return storage.values().stream().parallel().map(converter::convertToBoundary).collect(Collectors.toList());
	}

	@Override
	public void updateUser(UserBoundary user_Boundary) {
		UserEntity entity = converter.convertToEntity(user_Boundary);
		if (entity.getUsername() != null && entity.getPassword() != null && entity.getRole() != null)
		{
			if (storage.get(entity.getUsername()) != null)
				storage.put(entity.getUsername(), entity);
			else
			{
				throw new UserNotFoundException("No existing user with that username.");
				// TODO EXCEPTION USER DOESNT EXIST
			}
		}
		else
		{
			throw new UserBadRequestException("UserBoundary object missing fields.");
			// TODO updated user missing fields.
		}
		
	}

	@Override
	public void deleteAllUsers() {
		storage.clear();
		System.out.println("User storage cleared");
		
	}

	@Override
	public void deleteUser(String username) {
		if (storage.get(username) != null)
			storage.remove(username);
		else
		{
			// TODO EXCEPTION USER DOESNT EXIST
		}
		
	}

}

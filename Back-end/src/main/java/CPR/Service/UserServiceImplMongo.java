package CPR.Service;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import CPR.Boundary.NewUserBoundary;
import CPR.Boundary.UserBoundary;
import CPR.DAL.UserDao;
import CPR.Data.UserConverter;
import CPR.Data.UserEntity;
import CPR.Data.UserRole;
import CPR.Exception.UserBadRequestException;
import CPR.Exception.UserNotFoundException;

@Service
public class UserServiceImplMongo implements UserService {
	private UserDao userDao;
	private UserConverter converter;
	
	@Autowired
	public UserServiceImplMongo(UserDao userDao, UserConverter converter) {
		this.userDao = userDao;
		this.converter = converter;
	}
	
	@PostConstruct
	public void init() {}
	
	@Override
	public Object createUser(NewUserBoundary new_User_Boundary) {
		if (new_User_Boundary.getUsername() != null && !(new_User_Boundary.getUsername().equals("")) && new_User_Boundary.getPassword() != null && !(new_User_Boundary.getPassword().equals("")))
		{
			UserEntity entity = userDao.findByUsername(new_User_Boundary.getUsername());
			if (entity != null)
				throw new UserBadRequestException("User with that username already exists");
			else
			{
				entity = new UserEntity(new_User_Boundary.getUsername(), new_User_Boundary.getPassword(), UserRole.valueOf(new_User_Boundary.getRole()));
				entity = userDao.save(entity);
				System.out.println("Created and stored user successfully");
				return converter.convertToBoundary(entity);
			}
		}
		else
			throw new UserBadRequestException("Invalid username/password");
	}

	@Override
	public Object login(UserBoundary user_Boundary) {
		if (user_Boundary.getUsername() != null && !(user_Boundary.getUsername().equals("")) && user_Boundary.getPassword() != null && !(user_Boundary.getPassword().equals("")))
		{
			UserEntity entity = userDao.findByUsername(user_Boundary.getUsername());
			if (entity != null && entity.getPassword().equals(user_Boundary.getPassword()))
			{
				System.out.println(user_Boundary.getUsername() + " Login successful!");
				return user_Boundary;
			}
			else
			{
				throw new UserNotFoundException("User doesn't exist or password incorrect");
			}
		}
		throw new UserBadRequestException("Input username/password were null");
		
		}

	@Override
	public Object getUser(String username) {
		UserEntity entity = userDao.findByUsername(username);
		if (entity == null)
		{
			throw new UserNotFoundException("User not found.");
		}
		else
			return converter.convertToBoundary(entity);
	}

	@Override
	public List<Object> getAllUsers() {
		return userDao.findAll()
				.stream().parallel()
				.map(converter::convertToBoundary)
				.collect(Collectors.toList());
	}

	@Override
	public void updateUser(UserBoundary updated_user) {
		UserEntity updated_entity = converter.convertToEntity(updated_user);
		if (updated_entity.getUsername() != null && updated_entity.getPassword() != null && updated_entity.getRole() != null)
		{
			if (userDao.findByUsername(updated_entity.getUsername()) != null)
				userDao.save(updated_entity);			
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
		userDao.deleteAll();
		System.out.println("User storage cleared");
		
	}

	@Override
	public void deleteUser(String username) {
		if (userDao.findByUsername(username) != null)
			userDao.deleteById(username);
		else
		{
			// TODO EXCEPTION USER DOESNT EXIST
		}
		
	}

}

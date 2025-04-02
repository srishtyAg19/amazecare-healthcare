package com.hexaware.AmazeCare.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.AmazeCare.customException.UserNotFoundException;
import com.hexaware.AmazeCare.dto.UserDTO;
import com.hexaware.AmazeCare.mapper.UserMapper;
import com.hexaware.AmazeCare.model.User;
import com.hexaware.AmazeCare.repository.UserRepository;
import com.hexaware.AmazeCare.model.Role;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User", "ID", id));
        return userMapper.toDTO(user);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User", "ID", id));

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());

        if (userDTO.getRole() != null) {
            user.setRole(Role.valueOf(userDTO.getRole().toUpperCase())); // Convert String to Role enum
        }

        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User", "ID", id));
        userRepository.delete(user);
    }

	@Override
	public UserDTO getUserByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    
	}
}

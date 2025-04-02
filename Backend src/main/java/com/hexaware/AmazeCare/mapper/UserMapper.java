package com.hexaware.AmazeCare.mapper;

import com.hexaware.AmazeCare.dto.UserDTO;
import com.hexaware.AmazeCare.model.Role;
import com.hexaware.AmazeCare.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        // Convert Role enum to String
        if (user.getRole() != null) {
            dto.setRole(user.getRole().name());
        }
        return dto;
    }

    public User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        // Convert String to Role enum
        if (userDTO.getRole() != null) {
            user.setRole(Role.valueOf(userDTO.getRole().toUpperCase())); // Ensure valid enum value
        }
        return user;
    }
}
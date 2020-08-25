package com.springproject.springbootapp.model;

import com.springproject.springbootapp.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserConverter {
    private final OrderConverter orderConverter;

    public UserDTO convertToDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .login(user.getLogin())
                .orders(user.getOrders() == null ? null : orderConverter.convertToSetDTO(user.getOrders()))
                .build();
    }

    public User convertToEntity(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .login(userDTO.getLogin())
                .orders(userDTO.getOrders() == null ? null : orderConverter.convertToSetEntity(userDTO.getOrders()))
                .build();
    }

    public List<UserDTO> convertToListDTO(List<User> users) {
        return users.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}

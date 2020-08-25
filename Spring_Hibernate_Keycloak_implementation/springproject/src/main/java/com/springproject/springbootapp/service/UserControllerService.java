package com.springproject.springbootapp.service;

import com.springproject.springbootapp.model.User;
import com.springproject.springbootapp.model.UserConverter;
import com.springproject.springbootapp.model.dto.UserDTO;
import com.springproject.springbootapp.service.data.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserControllerService {
    private final UserService userService;

    private final UserConverter userConverter;

    public List<UserDTO> findAllUsers() {
        return userConverter.convertToListDTO(userService.findAllUsers());
    }

    public UserDTO findUser(String login) {
        Optional<User> op = userService.findByLogin(login);
        return op.map(userConverter::convertToDto).orElse(null);
    }

    public User addUser(String login, String role){
        User user = new User();
        user.setLogin(login);
        user.setUser_role(role);
        userService.addUser(user);
        return user;
    }
}

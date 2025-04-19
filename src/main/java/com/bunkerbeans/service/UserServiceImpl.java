package com.bunkerbeans.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bunkerbeans.dto.UserDTO;
import com.bunkerbeans.entity.User;
import com.bunkerbeans.repository.UserRepository;
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;    

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        User user=userDTO.toEntity();
        user=userRepository.save(user);
        return user.toDTO(); 
    }

}

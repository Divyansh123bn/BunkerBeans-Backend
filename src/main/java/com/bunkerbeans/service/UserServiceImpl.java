package com.bunkerbeans.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bunkerbeans.dto.LoginDTO;
import com.bunkerbeans.dto.UserDTO;
import com.bunkerbeans.entity.User;
import com.bunkerbeans.exception.CustomException;
import com.bunkerbeans.repository.UserRepository;
import com.bunkerbeans.utility.Utilities;
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;    

    @Autowired
    public PasswordEncoder passwordEncoder;
    

    @Override
    public UserDTO registerUser(UserDTO userDTO) throws CustomException {
        Optional<User> optional=userRepository.findByEmail(userDTO.getEmail());
        if(optional.isPresent()){
            throw new CustomException("USER_PRESENT");
        }
        userDTO.setId(Utilities.getNextSequence("users"));
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDTO.setCreatedAt(Instant.now());
        User user=userDTO.toEntity();
        user=userRepository.save(user);
        return user.toDTO(); 
    }

    @Override
    public UserDTO loginUser(LoginDTO loginDTO) throws CustomException {
        User user=userRepository.findByEmail(loginDTO.getEmail()).orElseThrow(()-> new CustomException("USER_NOT_FOUND"));
        if(!passwordEncoder.matches(loginDTO.getPassword(),user.getPassword())){
            throw new CustomException("INVALID_CREDENTIALS");
        }
        return user.toDTO();
    }

}

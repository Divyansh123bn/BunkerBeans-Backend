package com.bunkerbeans.service;

import com.bunkerbeans.dto.LoginDTO;
import com.bunkerbeans.dto.UserDTO;
import com.bunkerbeans.exception.CustomException;


public interface UserService {
    public UserDTO registerUser(UserDTO userDTO) throws CustomException;

    public UserDTO loginUser(LoginDTO loginDTO) throws CustomException ;
}

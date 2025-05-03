package com.bunkerbeans.service;

import com.bunkerbeans.dto.LoginDTO;
import com.bunkerbeans.dto.UserDTO;
import com.bunkerbeans.exception.CustomException;


public interface UserService {
    public UserDTO registerUser(UserDTO userDTO) throws CustomException;

    void sendResetOtp(String email);

    void setResetPassword(String email,String otp,String newPassword);

    void sendOtp(String email);
    
    void verifyOtp(String email,String otp);
}

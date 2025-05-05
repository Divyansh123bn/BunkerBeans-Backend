package com.bunkerbeans.service;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bunkerbeans.dto.LoginDTO;
import com.bunkerbeans.dto.UserDTO;
import com.bunkerbeans.entity.UserEntity;
import com.bunkerbeans.exception.CustomException;
import com.bunkerbeans.repository.UserRepository;
import com.bunkerbeans.utility.Utilities;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;    

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;
    

    @Override
    public UserDTO registerUser(UserDTO userDTO) throws CustomException {
        Optional<UserEntity> optional=userRepository.findByEmail(userDTO.getEmail());
        if(optional.isPresent()){
            throw new CustomException("USER_PRESENT");
        }
        userDTO.setId(Utilities.getNextSequence("users"));
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDTO.setCreatedAt(Instant.now());
        userDTO.setIsAccountVerified(false);
        userDTO.setResetOtpExpireAt(0L);
        userDTO.setVerifyOtp(null);
        userDTO.setVerifyOtpExpireAt(0L);
        userDTO.setResetOtp(null);
        UserEntity user=userDTO.toEntity();
        user=userRepository.save(user);
        return user.toDTO(); 
    }

     @Override
    public void sendResetOtp(String email) {
        UserEntity existingEntity=userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found"+email));
        // generate 6 digit otp
        String otp=String .valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));
        // calculate expiry time of otp 15 mins in milliseconds
        long expiryTime=System.currentTimeMillis()+(15*60*1000);
        // update profile
        existingEntity.setResetOtp(otp);
        existingEntity.setResetOtpExpireAt(expiryTime);
        userRepository.save(existingEntity);
        try{
            // send reset otp email:
            emailService.sendResetOtpEmail(existingEntity.getEmail(), otp);

        }catch(Exception ex){
            throw new RuntimeException("Unable to send email");
        }
    }

    

    @Override
    public void setResetPassword(String email, String newPassword) {
       UserEntity existingUser=userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found with "+email));
       existingUser.setPassword(passwordEncoder.encode(newPassword));
       existingUser.setIsAccountVerified(true);
       
       userRepository.save(existingUser);

    }

    @Override
    public void sendOtp(String email) {
        UserEntity existingUser=userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not present:"+email));
        if(existingUser.getIsAccountVerified()!=null && existingUser.getIsAccountVerified()){
            return;
        }
        // generate 6 digit otp
        String otp=String .valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));
        // calculate expiry time of otp 24 hours in milliseconds
        long expiryTime=System.currentTimeMillis()+(24*60*60*1000);
        // update User Entity
        existingUser.setVerifyOtp(otp);
        existingUser.setVerifyOtpExpireAt(expiryTime);
        userRepository.save(existingUser);
        try{
            emailService.sendOtpEmail(existingUser.getEmail(), otp);
        }catch(Exception e){
            throw new RuntimeException("Unable to send OTP.");
        }
    }

    @Override
    public void verifyOtp(String email, String otp) {
        UserEntity existinguser=userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("USer not present"));
        if(existinguser.getVerifyOtp()==null || !existinguser.getVerifyOtp().equals(otp)){
            throw new RuntimeException("Invalid OTP");
        }
        if(existinguser.getVerifyOtpExpireAt()<System.currentTimeMillis()){
            throw new RuntimeException("OTP Expired");
        }
        existinguser.setIsAccountVerified(true);
        existinguser.setVerifyOtp(null);
        existinguser.setVerifyOtpExpireAt(0L);
        userRepository.save(existinguser);
    }

    @Override
    public Boolean verifyResetOtp(String email, String otp) {
        UserEntity existingUser=userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found with "+email));
        if(existingUser.getResetOtp()==null || !existingUser.getResetOtp().equals(otp)){
         throw new RuntimeException("Invalid OTP");
        }
        if(existingUser.getResetOtpExpireAt()<System.currentTimeMillis()){
         throw new RuntimeException("OTP Expired");
        }
        existingUser.setResetOtp(null);
        existingUser.setResetOtpExpireAt(0L);
        userRepository.save(existingUser);
        return true;
    }

}

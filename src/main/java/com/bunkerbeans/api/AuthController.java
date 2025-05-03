package com.bunkerbeans.api;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.bunkerbeans.io.AuthRequest;
import com.bunkerbeans.io.AuthResponse;
import com.bunkerbeans.io.ResetPasswordRequest;
import com.bunkerbeans.service.AppUserDetailsService;
import com.bunkerbeans.service.UserService;
import com.bunkerbeans.utility.JwtUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AppUserDetailsService appUserDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService profileService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest){
        try{
            authenticate(authRequest.getEmail(),authRequest.getPassword());
            final UserDetails userDetails=appUserDetailsService.loadUserByUsername(authRequest.getEmail());
            final String jwtToken=jwtUtil.generateToken(userDetails);
            ResponseCookie cookie=ResponseCookie.from("jwt",jwtToken)
                                    .httpOnly(true)
                                    .path("/")
                                    .maxAge(Duration.ofDays(1))
                                    .sameSite("Strict")
                                    .build();
            return ResponseEntity.ok().header(org.springframework.http.HttpHeaders.SET_COOKIE,cookie.toString())
                                        .body(new AuthResponse(authRequest.getEmail(),jwtToken));
        }catch(BadCredentialsException ex){
            Map<String,Object> error=new HashMap<>();
            error.put("error",true);
            error.put("message","Email or password is incorrect");
            return new ResponseEntity<>(error,HttpStatus.UNAUTHORIZED);
        }catch(DisabledException ex){
            Map<String,Object> error=new HashMap<>();
            error.put("error",true);
            error.put("message","Account is disabled");
            return new ResponseEntity<>(error,HttpStatus.UNAUTHORIZED);
        }catch(Exception ex){
            Map<String,Object> error=new HashMap<>();
            error.put("error",true);
            error.put("message","Authentication Failed");
            return new ResponseEntity<>(error,HttpStatus.UNAUTHORIZED);
        }
    }


    private void authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    @GetMapping("/is-authenticated")
    public ResponseEntity<Boolean> isAuthenticated(@CurrentSecurityContext(expression = "authentication?.name")String email){
        return ResponseEntity.ok(email!=null);
    }

    @PostMapping("/reset-otp")
    public void sendResetOtp(@RequestParam String email){
        try{
            profileService.sendResetOtp(email);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest request){
        try{
            profileService.setResetPassword(request.getEmail(), request.getOtp(), request.getNewPassword());
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

    @PostMapping("/send-otp")
    public void sendVerifyOtp(@CurrentSecurityContext(expression = "authentication?.name") String email){
        try{
            profileService.sendOtp(email);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public void verifyEmail(@RequestBody Map<String,Object> request,@CurrentSecurityContext(expression = "authentication?.name")String email){
        if(request.get("otp").toString()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing otp");
        }
        try{
            profileService.verifyOtp(email,request.get("otp").toString());
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

}


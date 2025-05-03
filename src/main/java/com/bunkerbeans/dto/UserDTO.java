package com.bunkerbeans.dto;


import java.time.Instant;

import com.bunkerbeans.entity.UserEntity;
import com.bunkerbeans.utility.AccountType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    @NotBlank(message = "{user.name.absent}")
    private String name;
    @NotBlank(message = "{user.email.absent}")
    @Email(message = "{user.email.invalid}")
    private String email;
    @NotBlank(message = "{user.password.absent}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",message = "{user.password.invalid}")
    private String password;
    private AccountType accountType;
    private String verifyOtp;
    private Boolean isAccountVerified;
    private Long verifyOtpExpireAt;
    private String resetOtp;
    private Long resetOtpExpireAt;
    private Instant createdAt;
    private Instant updatedAt;

    public UserEntity toEntity(){
        return new UserEntity(this.id, this.name, this.email, this.password, this.accountType,this.verifyOtp,this.isAccountVerified,this.verifyOtpExpireAt,this.resetOtp,this.resetOtpExpireAt,this.createdAt,this.updatedAt);
    }
}

package com.bunkerbeans.io;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {
    @NotBlank(message = "Password is Required")
    private String newPassword;
    @NotBlank(message = "Email is Required")
    private String email;
}

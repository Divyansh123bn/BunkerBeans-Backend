package com.bunkerbeans.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.bunkerbeans.dto.UserDTO;
import com.bunkerbeans.utility.AccountType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;
    private String name;
    @Indexed(unique = true)
    private String email;
    private String password;
    private AccountType accountType;

    public UserDTO toDTO(){
        return new UserDTO(this.id, this.name, this.email, this.password, this.accountType);
    }
}

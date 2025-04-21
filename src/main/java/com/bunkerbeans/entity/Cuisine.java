package com.bunkerbeans.entity;

import java.util.Base64;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.bunkerbeans.dto.CuisineDTO;
import com.bunkerbeans.dto.Detail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "Cuisines")
@AllArgsConstructor
@NoArgsConstructor
public class Cuisine {
    @Id
    private Long id;
    private byte[] image;
    private String name;
    private Detail detail;
    private String description;
    private int price;

    public CuisineDTO toDTO(){
        return new CuisineDTO(this.id,this.image!=null?Base64.getEncoder().encodeToString(this.image):null, this.name, this.detail, this.description, this.price);
    } 
}

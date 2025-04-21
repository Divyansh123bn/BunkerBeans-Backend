package com.bunkerbeans.dto;

import java.util.Base64;

import com.bunkerbeans.entity.Cuisine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CuisineDTO {
    private Long id;
    private String image;
    private String name;
    private Detail detail;
    private String description;
    private int price;

    public Cuisine toEntity(){
        return new Cuisine(this.id, this.image!=null?Base64.getDecoder().decode(this.image):null, this.name, this.detail, this.description, this.price);
    }
}

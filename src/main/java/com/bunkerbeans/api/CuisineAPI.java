package com.bunkerbeans.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bunkerbeans.dto.CuisineDTO;
import com.bunkerbeans.entity.Cuisine;
import com.bunkerbeans.exception.CustomException;
import com.bunkerbeans.service.CuisineService;

@RestController
@CrossOrigin
@RequestMapping("/cuisines")
public class CuisineAPI {

    @Autowired
    private CuisineService cuisineService;

    @PostMapping("/add")
    public ResponseEntity<CuisineDTO> addCuisine(@RequestBody CuisineDTO cuisineDTO) throws CustomException{
        return new ResponseEntity<>(cuisineService.addCuisine(cuisineDTO),HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<CuisineDTO> getCuisine(@PathVariable Long id) throws CustomException{
        return new ResponseEntity<>(cuisineService.getCuisine(id),HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<CuisineDTO> updateCuisine(@RequestBody CuisineDTO cuisineDTO) throws CustomException{
        return new ResponseEntity<>(cuisineService.updateCuisine(cuisineDTO),HttpStatus.OK);
    }

    @PostMapping("/remove/{id}")
    public ResponseEntity removeCuisine(@PathVariable Long id) throws CustomException{
        cuisineService.removeCuisine(id);
        return new ResponseEntity<>(HttpStatus.OK);
    } 
}

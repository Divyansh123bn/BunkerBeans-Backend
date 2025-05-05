package com.bunkerbeans.service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bunkerbeans.dto.CuisineDTO;
import com.bunkerbeans.entity.Cuisine;
import com.bunkerbeans.exception.CustomException;
import com.bunkerbeans.repository.CuisineRepository;
import com.bunkerbeans.utility.Utilities;

@Service("CuisineService")
public class CuisineServiceImpl implements CuisineService {


    @Autowired
    private CuisineRepository cuisineRepository;

    @Override
    public CuisineDTO addCuisine(CuisineDTO cuisineDTO) throws CustomException {
        Cuisine cuisine=new Cuisine();
        cuisine.setId(Utilities.getNextSequence("cuisines"));
        cuisine.setName(cuisineDTO.getName());
        String base64Image = cuisineDTO.getImage();
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        cuisine.setImage(imageBytes);
        cuisine.setDetail(cuisineDTO.getDetail());
        cuisine.setDescription(cuisineDTO.getDescription());
        cuisine.setPrice(cuisineDTO.getPrice());
        cuisineRepository.save(cuisine);    
        return cuisine.toDTO();
    }

    @Override
    public CuisineDTO getCuisine(Long id) throws CustomException {
        CuisineDTO cuisine=cuisineRepository.findById(id).orElseThrow(()->new CustomException("CUISINE_NOT_FOUND")).toDTO();
        return cuisine;
    }

    @Override
    public CuisineDTO updateCuisine(CuisineDTO cuisineDTO) throws CustomException {
       Cuisine cuisine=cuisineRepository.findById(cuisineDTO.getId()).orElseThrow(()-> new CustomException("CUISINE_NOT_FOUND"));
       String base64Image = cuisineDTO.getImage();
       byte[] imageBytes = Base64.getDecoder().decode(base64Image);
       cuisine.setImage(imageBytes);
       cuisine.setDetail(cuisineDTO.getDetail());
       cuisine.setDescription(cuisineDTO.getDescription());
       cuisine.setPrice(cuisineDTO.getPrice());
       cuisineRepository.save(cuisine);    
       return cuisine.toDTO();
    }

    @Override
    public void removeCuisine(Long id) throws CustomException {
        Cuisine cuisine=cuisineRepository.findById(id).orElseThrow(()->new CustomException("CUISINE_NOT_FOUND"));
        cuisineRepository.delete(cuisine);
    }

    @Override
    public List<CuisineDTO> getAllCuisines() throws CustomException {
        List<Cuisine> cuisines=cuisineRepository.findAll();
        return cuisines.stream().map(x->x.toDTO()).toList();
    }

    

}

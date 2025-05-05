package com.bunkerbeans.service;

import java.util.List;

import com.bunkerbeans.dto.CuisineDTO;
import com.bunkerbeans.exception.CustomException;

public interface CuisineService {

    public List<CuisineDTO> getAllCuisines() throws CustomException;

    public CuisineDTO addCuisine(CuisineDTO cuisineDTO) throws CustomException;

    public CuisineDTO getCuisine(Long id) throws CustomException;

    public CuisineDTO updateCuisine(CuisineDTO cuisineDTO) throws CustomException;

    public void removeCuisine(Long id) throws CustomException;

}

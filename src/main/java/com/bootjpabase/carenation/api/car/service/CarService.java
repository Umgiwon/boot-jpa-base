package com.bootjpabase.carenation.api.car.service;

import com.bootjpabase.carenation.api.car.domain.dto.request.CarListRequestDTO;
import com.bootjpabase.carenation.api.car.domain.dto.response.CarResponseDTO;
import com.bootjpabase.carenation.api.car.repository.CarRepository;
import com.bootjpabase.carenation.api.car.repository.CarRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarService {

    private final CarRepository carRepository;
    private final CarRepositoryCustom carRepositoryCustom;

    /**
     * Car 목록 조회
     *
     * @param dto
     * @return
     */
    public List<CarResponseDTO> getCarList(CarListRequestDTO dto) {
        return carRepositoryCustom.getCarList(dto);
    }
}

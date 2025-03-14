package com.bootjpabase.api.car.service;

import com.bootjpabase.api.car.domain.dto.request.CarListRequestDTO;
import com.bootjpabase.api.car.domain.dto.response.CarResponseDTO;
import com.bootjpabase.api.car.repository.CarRepository;
import com.bootjpabase.api.car.repository.CarRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Page<CarResponseDTO> getCarList(CarListRequestDTO dto, Pageable pageable) {
        return carRepositoryCustom.getCarList(dto, pageable);
    }
}

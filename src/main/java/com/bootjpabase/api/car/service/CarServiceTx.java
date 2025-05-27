package com.bootjpabase.api.car.service;

import com.bootjpabase.api.car.domain.dto.request.CarSaveRequestDTO;
import com.bootjpabase.api.car.domain.dto.request.CarUpdateRequestDTO;
import com.bootjpabase.api.car.domain.dto.response.CarResponseDTO;
import com.bootjpabase.api.car.domain.entity.Car;
import com.bootjpabase.api.car.mapper.CarMapper;
import com.bootjpabase.api.car.repository.CarRepository;
import com.bootjpabase.global.enums.common.ApiReturnCode;
import com.bootjpabase.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CarServiceTx {

    private final CarMapper carMapper;
    private final CarRepository carRepository;

    /**
     * Car 저장 (단건 & 다건)
     *
     * @param dtoList Car 저장 요청 dto
     * @return 저장된 Car 목록 응답 dto
     */
    public List<CarResponseDTO> saveCar(List<CarSaveRequestDTO> dtoList) {

        // 요청 dto를 entity로 변환
        List<Car> carEntities = dtoList.stream()
                .map(carMapper::toCarEntity)
                .toList();

        // entity 저장 후 dto 반환
        return carRepository.saveAll(carEntities).stream()
                .map(carMapper::toCarResponseDTO)
                .toList();
    }

    /**
     * Car 수정
     *
     * @param carSn 수정할 Car 순번
     * @param dto   Car 수정 요청 dto
     * @return 수정된 Car 응답 dto
     */
    public CarResponseDTO updateCar(Long carSn, CarUpdateRequestDTO dto) {

        // 수정할 entity 조회
        Car car = carRepository.findById(carSn)
                .orElseThrow(() -> new BusinessException(ApiReturnCode.NO_DATA_ERROR));

        // entity 영속성 컨텍스트 수정
        car.updateCarInfo(dto);

        // entity 수정 후 dto 반환
        return carMapper.toCarResponseDTO(car);
    }
}

package com.bootjpabase.carmanager.api.car.service;

import com.bootjpabase.carmanager.api.car.domain.dto.request.CarSaveRequestDTO;
import com.bootjpabase.carmanager.api.car.domain.dto.request.CarUpdateRequestDTO;
import com.bootjpabase.carmanager.api.car.domain.entity.Car;
import com.bootjpabase.carmanager.api.car.repository.CarRepository;
import com.bootjpabase.carmanager.api.car.repository.CarRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CarServiceTx {

    private final CarRepository carRepository;
    private final CarRepositoryCustom carRepositoryCustom;

    /**
     * Car 단건 저장
     * @param dto
     */
    public boolean saveCar(CarSaveRequestDTO dto) {
        boolean result = false;

        // 저장할 entity 객체 생성
        Car saveCar = Car.builder()
                .category(dto.getCategory())
                .manufacturer(dto.getManufacturer())
                .modelName(dto.getModelName())
                .productionYear(dto.getProductionYear())
                .rentalYn("Y")
                .build();

        // 단건 저장
        carRepository.save(saveCar);
        result = true;

        return result;
    }

    /**
     * Car 다건 저장
     * @param dtoList
     */
    public boolean saveAllCar(List<CarSaveRequestDTO> dtoList) {
        boolean result = false;

        // 저장할 entity 목록 담을 array 초기화
        List<Car> saveCarList = new ArrayList<>();

        // dto 반복하며 entity 생성하여 저장목록에 담는다.
        dtoList.forEach(dto -> {
            Car saveCar = Car.builder()
                    .category(dto.getCategory())
                    .manufacturer(dto.getManufacturer())
                    .modelName(dto.getModelName())
                    .productionYear(dto.getProductionYear())
                    .rentalYn("Y")
                    .build();

            saveCarList.add(saveCar);
        });

        // 담긴 저장목록 다건 저장
        carRepository.saveAll(saveCarList);
        result = true;

        return result;
    }

    /**
     * Car 수정
     * @param dto
     */
    public boolean updateCar(CarUpdateRequestDTO dto) {
        boolean result = false;

        // 수정할 entity 조회
        Car updateCar = carRepository.findById(dto.getCarSn()).orElse(null);

        // entity 영속성 컨텍스트 수정
        if(!ObjectUtils.isEmpty(updateCar)) {

            // 수정할 값이 있을 경우에만 수정
            if(!ObjectUtils.isEmpty(dto.getCategory())) {
                updateCar.setCategory(dto.getCategory()); // 카테고리
            }
            if(!ObjectUtils.isEmpty(dto.getRentalYn())) {
                updateCar.setRentalYn(dto.getRentalYn()); // 대여 가능 여부
            }
            if(!ObjectUtils.isEmpty(dto.getRentalDescription())) {
                updateCar.setRentalDescription(dto.getRentalDescription()); // 대여 상세 정보
            }

            result = true;
        }

        return result;
    }
}

package com.bootjpabase.api.car.service;

import com.bootjpabase.api.car.domain.dto.request.CarSaveRequestDTO;
import com.bootjpabase.api.car.domain.dto.request.CarUpdateRequestDTO;
import com.bootjpabase.api.car.domain.entity.Car;
import com.bootjpabase.api.car.repository.CarRepository;
import com.bootjpabase.api.car.repository.CarRepositoryCustom;
import com.bootjpabase.global.enums.common.ApiReturnCode;
import com.bootjpabase.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        // 저장할 자동차 entity 객체 생성
        Car saveCar = createCarEntity(dto);

        // 단건 저장
        carRepository.save(saveCar);

        return true;
    }

    /**
     * Car 다건 저장
     * @param dtoList
     */
    public boolean saveAllCar(List<CarSaveRequestDTO> dtoList) {

        // 저장할 entity 목록 담을 array 초기화
        List<Car> saveCarList = new ArrayList<>();

        // dto 반복하며 entity 생성하여 저장목록에 담는다.
        dtoList.forEach(dto -> {
            Car saveCar = createCarEntity(dto);

            saveCarList.add(saveCar);
        });

        // 담긴 저장목록 다건 저장
        carRepository.saveAll(saveCarList);

        return true;
    }

    /**
     * 자동차 entity 생성 (저장시)
     * @param dto
     * @return
     */
    private Car createCarEntity(CarSaveRequestDTO dto) {
        return Car.builder()
                .category(dto.getCategory())
                .manufacturer(dto.getManufacturer())
                .modelName(dto.getModelName())
                .productionYear(dto.getProductionYear())
                .rentalYn("Y")
                .build();
    }

    /**
     * Car 수정
     * @param dto
     */
    public boolean updateCar(Long carSn, CarUpdateRequestDTO dto) {

        // 수정할 entity 조회
        Car updateCar = carRepository.findById(carSn)
                .orElseThrow(() -> new BusinessException(ApiReturnCode.NO_DATA_ERROR));

        // entity 영속성 컨텍스트 수정
        updateCar(updateCar, dto);

        return true;
    }

    /**
     * 자동차 수정 (수정할 값이 있는 데이터만 수정)
     * @param car
     * @param dto
     */
    private void updateCar(Car car, CarUpdateRequestDTO dto) {

        Optional.ofNullable(dto.getCategory()).ifPresent(car::setCategory); // 카테고리
        Optional.ofNullable(dto.getCategory()).ifPresent(car::setCategory); // 대여 가능 여부
        Optional.ofNullable(dto.getCategory()).ifPresent(car::setCategory); // 대여 상세 정보
    }
}

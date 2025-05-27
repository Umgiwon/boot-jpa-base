package com.bootjpabase.api.car.mapper;

import com.bootjpabase.api.car.domain.dto.request.CarSaveRequestDTO;
import com.bootjpabase.api.car.domain.dto.response.CarResponseDTO;
import com.bootjpabase.api.car.domain.entity.Car;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CarMapper {

    /**
     * CarSaveRequestDTO를 Car 엔티티로 변환
     *
     * @param dto Car 저장 요청 dto
     * @return Car 엔티티
     */
    Car toCarEntity(CarSaveRequestDTO dto);

    /**
     * Car 엔티티를 CarResponseDTO로 변환
     *
     * @param car Car 엔티티
     * @return Car 응답 dto
     */
    CarResponseDTO toCarResponseDTO(Car car);
}

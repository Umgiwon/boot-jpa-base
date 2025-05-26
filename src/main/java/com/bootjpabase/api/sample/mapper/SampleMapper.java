package com.bootjpabase.api.sample.mapper;

import com.bootjpabase.api.sample.domain.dto.request.SampleSaveRequestDTO;
import com.bootjpabase.api.sample.domain.dto.response.SampleResponseDTO;
import com.bootjpabase.api.sample.domain.entity.Sample;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * MapStruct를 사용한 객체 매핑을 위한 인터페이스
 * 
 * @Mapper 어노테이션이 선언된 인터페이스는 자동으로 구현체(MapperImpl)가 생성된다.
 * 객체 변환 시 원본 객체를 'source', 대상 객체를 'target'이라고 한다.
 * <p>
 * 주요 설정:
 * - componentModel: Spring Bean으로 등록하여 의존성 주입이 가능하도록 설정
 * - unmappedTargetPolicy: 대상 객체에 매핑되지 않는 필드가 있을 경우 무시하고 null로 설정
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SampleMapper {

    /*
      필드명이 동일한 경우 MapStruct가 자동으로 매핑을 수행합니다.
      예시: source.sampleSn -> target.sampleSn

      필드명이 다른 경우 @Mapping 어노테이션으로 수동 매핑이 가능합니다.
      예시: @Mapping(target = "targetField", source = "sourceField")
     */

    /**
     * SampleSaveRequestDTO를 Sample 엔티티로 변환
     *
     * @param dto Sample 저장 요청 dto
     * @return Sample 엔티티
     */
    Sample toSampleEntity(SampleSaveRequestDTO dto);

    /**
     * Sample 엔티티를 SampleResponseDTO로 변환
     *
     * @param sample Sample 엔티티
     * @return Sample 응답 dto
     */
    SampleResponseDTO toSampleResponseDTO(Sample sample);
}
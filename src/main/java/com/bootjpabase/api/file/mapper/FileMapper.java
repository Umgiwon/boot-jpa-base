package com.bootjpabase.api.file.mapper;

import com.bootjpabase.api.file.domain.dto.response.FileResponseDTO;
import com.bootjpabase.api.file.domain.entity.File;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileMapper {

    /**
     * File 엔티티를 FileResponseDTO로 변환
     *
     * @param file File 엔티티
     * @return File 응답 dto
     */
    FileResponseDTO toFileResponseDTO(File file);
}

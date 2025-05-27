package com.bootjpabase.api.sample;

import com.bootjpabase.api.user.domain.dto.request.UserSaveRequestDTO;
import com.bootjpabase.api.user.domain.dto.response.UserResponseDTO;
import com.bootjpabase.api.user.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    /**
     * UserSaveRequestDTO를 User 엔티티로 변환
     *
     * @param dto User 저장 요청 dto
     * @return User 엔티티
     */
    User toUserEntity(UserSaveRequestDTO dto);

    /**
     * User 엔티티를 UserResponseDTO로 변환
     *
     * @param user User 엔티티
     * @return User 응답 dto
     */
    UserResponseDTO toUserResponseDTO(User user);
}

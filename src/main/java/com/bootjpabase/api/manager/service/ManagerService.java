package com.bootjpabase.api.manager.service;

import com.nangman.api.admin.system.code.domain.dto.response.CodeNameListResponseDTO;
import com.nangman.api.admin.system.code.repository.CodeRepositoryCustom;
import com.nangman.api.admin.system.manager.domain.dto.request.ManagerListRequestDTO;
import com.nangman.api.admin.system.manager.domain.dto.request.ManagerLoginRequestDTO;
import com.nangman.api.admin.system.manager.domain.dto.response.ManagerDetailResponseDTO;
import com.nangman.api.admin.system.manager.domain.dto.response.ManagerResponseDTO;
import com.nangman.api.admin.system.manager.domain.entity.Manager;
import com.nangman.api.admin.system.manager.repository.ManagerRepository;
import com.nangman.api.admin.system.manager.repository.ManagerRepositoryCustom;
import com.nangman.global.config.jwt.component.TokenProvider;
import com.nangman.global.config.jwt.domain.dto.TokenResponseDTO;
import com.nangman.global.enums.common.ApiReturnCode;
import com.nangman.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final ManagerRepositoryCustom managerRepositoryCustom;
    private final CodeRepositoryCustom codeRepositoryCustom;
    private final BCryptPasswordEncoder encoder;
    private final TokenProvider tokenProvider;

    /**
     * 관리자 목록 조회
     * @param dto
     * @param pageable
     * @return
     */
    public List<ManagerResponseDTO> getManagerList(ManagerListRequestDTO dto, Pageable pageable) {
        return managerRepositoryCustom.getManagerList(dto, pageable);
    }

    /**
     * 관리자 상세 조회
     * @param id
     * @return
     */
    public ManagerDetailResponseDTO getManager(String id) {

        // 관리자 상세 조회
        ManagerDetailResponseDTO result = managerRepositoryCustom.getManager(id);

        // 관리자 등급 코드 목록 조회
        List<CodeNameListResponseDTO> roleCodeList = codeRepositoryCustom.getRoleCodeList();

        // 관리자 상세에 관리자 등급코드 목록 set
        if(!ObjectUtils.isEmpty(result) && !ObjectUtils.isEmpty(roleCodeList)) {
            result.setRoleCodeList(roleCodeList);
        }

        return result;
    }

    /**
     * 관리자 로그인
     * @param dto
     * @return
     */
    public TokenResponseDTO managerLogin(ManagerLoginRequestDTO dto) {
        TokenResponseDTO tokenDto;

        // 해당 계정 조회
        Manager manager = managerRepository.findById(dto.getId()).orElse(null);

        // 해당 아이디 없을 경우
        if(ObjectUtils.isEmpty(manager)) {
            throw new BusinessException(ApiReturnCode.LOGIN_ID_FAIL_ERROR);
        }

        // 비밀번호 체크
        if(!encoder.matches(dto.getPassword(), manager.getPassword())) {
            throw new BusinessException(ApiReturnCode.LOGIN_PWD_FAIL_ERROR);
        }

        tokenDto = TokenResponseDTO.builder()
                .token(tokenProvider.createToken(manager))
                .build();

        return tokenDto;
    }
}

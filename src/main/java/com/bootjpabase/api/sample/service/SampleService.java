package com.bootjpabase.api.sample.service;

import com.bootjpabase.api.sample.domain.dto.request.SampleListRequestDTO;
import com.bootjpabase.api.sample.domain.dto.response.SampleResponseDTO;
import com.bootjpabase.api.sample.repository.SampleRepositoryCustom;
import com.bootjpabase.global.enums.common.ApiReturnCode;
import com.bootjpabase.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SampleService {

    private final SampleRepositoryCustom sampleRepositoryCustom;

    /**
     * Sample 상세 조회
     *
     * @param sampleSn 조회할 Sample 순번
     * @return 조회된 Sample 응답 dto
     */
    public SampleResponseDTO getSample(Long sampleSn) {
        return Optional.ofNullable(sampleRepositoryCustom.getSample(sampleSn))
                .orElseThrow(() -> new BusinessException(ApiReturnCode.NO_DATA_ERROR));
    }

    /**
     * Sample 목록 조회
     *
     * @param dto      조회할 Sample 조건 dto
     * @param pageable 페이징 조건
     * @return 조회된 Sample 목록 응답 dto
     */
    public Page<SampleResponseDTO> getSampleList(SampleListRequestDTO dto, Pageable pageable) {
        return sampleRepositoryCustom.getSampleList(dto, pageable);
    }
}

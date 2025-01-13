package com.bootjpabase.carenation.api.sample.service;

import com.bootjpabase.carenation.api.sample.domain.dto.request.SampleDetailRequestDTO;
import com.bootjpabase.carenation.api.sample.domain.dto.request.SampleListRequestDTO;
import com.bootjpabase.carenation.api.sample.domain.dto.response.SampleResponseDTO;
import com.bootjpabase.carenation.api.sample.repository.SampleRepository;
import com.bootjpabase.carenation.api.sample.repository.SampleRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SampleService {

    private final SampleRepository sampleRepository;
    private final SampleRepositoryCustom sampleRepositoryCustom;

    /**
     * Sample 목록 조회
     * @param dto
     * @return
     */
    public List<SampleResponseDTO> getSampleList(SampleListRequestDTO dto) {
        return sampleRepositoryCustom.getSampleList(dto);
    }

    /**
     * Sample 단건 조회
     * @param dto
     * @return
     */
    public SampleResponseDTO getSample(SampleDetailRequestDTO dto) {
        return sampleRepositoryCustom.getSample(dto);
    }

}

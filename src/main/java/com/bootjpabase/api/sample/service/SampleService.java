package com.bootjpabase.api.sample.service;

import com.bootjpabase.api.sample.domain.dto.request.SampleListRequestDTO;
import com.bootjpabase.api.sample.domain.dto.response.SampleResponseDTO;
import com.bootjpabase.api.sample.repository.SampleRepository;
import com.bootjpabase.api.sample.repository.SampleRepositoryCustom;
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
     * Sample 단건 조회
     * @param sampleSn
     * @return
     */
    public SampleResponseDTO getSample(Long sampleSn) {
        return sampleRepositoryCustom.getSample(sampleSn);
    }

    /**
     * Sample 목록 조회
     * @param dto
     * @return
     */
    public List<SampleResponseDTO> getSampleList(SampleListRequestDTO dto) {
        return sampleRepositoryCustom.getSampleList(dto);
    }
}

package com.bootjpabase.api.sample.service;

import com.bootjpabase.api.sample.domain.dto.request.SampleSaveRequestDTO;
import com.bootjpabase.api.sample.domain.dto.request.SampleUpdateRequestDTO;
import com.bootjpabase.api.sample.domain.dto.response.SampleResponseDTO;
import com.bootjpabase.api.sample.domain.entity.Sample;
import com.bootjpabase.api.sample.mapper.SampleMapper;
import com.bootjpabase.api.sample.repository.SampleRepository;
import com.bootjpabase.global.enums.common.ApiReturnCode;
import com.bootjpabase.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SampleServiceTx {

    private final SampleMapper sampleMapper;
    private final SampleRepository sampleRepository;

    /**
     * Sample 저장 (단건 & 다건)
     *
     * @param dtoList Sample 저장 요청 dto
     * @return 저장된 Sample 목록 응답 dto
     */
    public List<SampleResponseDTO> saveSample(List<SampleSaveRequestDTO> dtoList) {

        // 요청 dto를 entity로 변환
        List<Sample> sampleEntities = dtoList.stream()
                .map(sampleMapper::toSampleEntity)
                .toList();

        // entity 저장 후 dto 반환
        return sampleRepository.saveAll(sampleEntities).stream()
                .map(sampleMapper::toSampleResponseDTO)
                .toList();
    }

    /**
     * Sample 수정
     *
     * @param sampleSn 수정할 Sample 순번
     * @param dto      Sample 수정 요청 dto
     * @return 수정된 Sample 응답 dto
     */
    public SampleResponseDTO updateSample(Long sampleSn, SampleUpdateRequestDTO dto) {

        // 수정할 entity 조회
        Sample sample = sampleRepository.findById(sampleSn)
                .orElseThrow(() -> new BusinessException(ApiReturnCode.NO_DATA_ERROR));

        // entity 영속성 컨텍스트 수정
        sample.updateSampleInfo(dto);

        // entity 수정 후 dto 반환
        return sampleMapper.toSampleResponseDTO(sample);
    }

    /**
     * Sample 삭제
     *
     * @param sampleSn 삭제할 Sample 순번
     * @return 삭제된 Sample 응답 dto
     */
    public SampleResponseDTO deleteSample(Long sampleSn) {

        // 삭제할 entity 조회
        Sample sample = sampleRepository.findById(sampleSn)
                .orElseThrow(() -> new BusinessException(ApiReturnCode.NO_DATA_ERROR));

        // 삭제
        sampleRepository.delete(sample);

        // 삭제 후 dto 반환
        return sampleMapper.toSampleResponseDTO(sample);
    }
}

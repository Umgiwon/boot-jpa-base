package com.bootjpabase.api.sample.service;

import com.bootjpabase.api.sample.domain.dto.request.SampleSaveRequestDTO;
import com.bootjpabase.api.sample.domain.dto.request.SampleUpdateRequestDTO;
import com.bootjpabase.api.sample.domain.dto.response.SampleResponseDTO;
import com.bootjpabase.api.sample.domain.entity.Sample;
import com.bootjpabase.api.sample.repository.SampleRepository;
import com.bootjpabase.api.sample.repository.SampleRepositoryCustom;
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
public class SampleServiceTx {

    private final SampleRepository sampleRepository;
    private final SampleRepositoryCustom sampleRepositoryCustom;

    /**
     * Sample 단건 저장
     * @param dto
     */
    public SampleResponseDTO saveSample(SampleSaveRequestDTO dto) {

        // 저장할 샘플 entity 객체 생성
        Sample saveSample = createSampleEntity(dto);

        // 단건 저장 후 dto 반환
        return sampleEntityToDto(sampleRepository.save(saveSample));
    }

    /**
     * Sample 다건 저장
     * @param dtoList
     */
    public List<SampleResponseDTO> saveAllSample(List<SampleSaveRequestDTO> dtoList) {

        // 저장할 entity 목록 담을 array 초기화
        List<Sample> saveSampleList = new ArrayList<>();

        // dto 반복하며 entity 생성하여 저장목록에 담는다.
        dtoList.forEach(dto -> {
            Sample saveSample = createSampleEntity(dto);

            saveSampleList.add(saveSample);
        });

        // 저장된 dto 목록 담을 array 초기화
        List<SampleResponseDTO> savedSampleList = new ArrayList<>();

        // 담긴 저장목록 다건 저장 후 DTO 반환
        sampleRepository.saveAll(saveSampleList)
                .forEach(savedSample -> savedSampleList.add(sampleEntityToDto(savedSample)));

        return savedSampleList;
    }

    /**
     * 샘플 entity 생성 (저장시)
     * @param dto
     * @return
     */
    private Sample createSampleEntity(SampleSaveRequestDTO dto) {
        return Sample.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();
    }

    /**
     * Sample 수정
     * @param dto
     */
    public SampleResponseDTO updateSample(Long sampleSn, SampleUpdateRequestDTO dto) {

        // 수정할 entity 조회
        Sample updateSample = sampleRepository.findById(sampleSn)
                .orElseThrow(() -> new BusinessException(ApiReturnCode.NO_DATA_ERROR));

        // entity 영속성 컨텍스트 수정
        updateSample(updateSample, dto);

        return sampleEntityToDto(updateSample);
    }

    /**
     * 샘플 수정 (수정할 값이 있는 데이터만 수정)
     * @param sample
     * @param dto
     * @return
     */
    private void updateSample(Sample sample, SampleUpdateRequestDTO dto) {

        Optional.ofNullable(dto.getTitle()).ifPresent(sample::setTitle); // 제목
        Optional.ofNullable(dto.getContent()).ifPresent(sample::setContent); // 내용
    }

    /**
     * Sample 삭제
     * @param sampleSn
     */
    public SampleResponseDTO deleteSample(Long sampleSn) {

        // 삭제할 entity 조회
        Sample deleteSample = sampleRepository.findById(sampleSn)
                .orElseThrow(() -> new BusinessException(ApiReturnCode.NO_DATA_ERROR));

        // 삭제
        sampleRepository.delete(deleteSample);

        // 삭제 후 dto 반환
        return sampleEntityToDto(deleteSample);
    }

    /**
     * 샘플 entity를 DTO로 변환
     * @param savedSample
     * @return
     */
    private SampleResponseDTO sampleEntityToDto(Sample savedSample) {
        return SampleResponseDTO.builder()
                .sampleSn(savedSample.getSampleSn())
                .title(savedSample.getTitle())
                .content(savedSample.getContent())
                .build();
    }
}

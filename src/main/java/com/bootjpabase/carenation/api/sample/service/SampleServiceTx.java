package com.bootjpabase.carenation.api.sample.service;

import com.bootjpabase.carenation.api.sample.domain.dto.request.SampleDeleteRequestDTO;
import com.bootjpabase.carenation.api.sample.domain.dto.request.SampleSaveRequestDTO;
import com.bootjpabase.carenation.api.sample.domain.dto.request.SampleUpdateRequestDTO;
import com.bootjpabase.carenation.api.sample.domain.entity.Sample;
import com.bootjpabase.carenation.api.sample.repository.SampleRepository;
import com.bootjpabase.carenation.api.sample.repository.SampleRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    public boolean saveSample(SampleSaveRequestDTO dto) {

        boolean result = false;

        // 저장할 entity 객체 생성
        Sample saveSample = Sample.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();

        // 단건 저장
        sampleRepository.save(saveSample);
        result = true;

        return result;
    }

    /**
     * Sample 다건 저장
     * @param dtoList
     */
    public boolean saveAllSample(List<SampleSaveRequestDTO> dtoList) {

        boolean result = false;

        // 저장할 entity 목록 담을 array 초기화
        List<Sample> saveSampleList = new ArrayList<>();

        // dto 반복하며 entity 생성하여 저장목록에 담는다.
        dtoList.forEach(dto -> {
            Sample saveSample = Sample.builder()
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .build();
            saveSampleList.add(saveSample);
        });

        // 담긴 저장목록 다건 저장
        sampleRepository.saveAll(saveSampleList);
        result = true;

        return result;
    }

    /**
     * Sample 수정
     * @param dto
     */
    public void updateSample(SampleUpdateRequestDTO dto) {

        // 수정할 entity 조회
        Sample updateSample = sampleRepository.findById(dto.getSampleSn()).orElseThrow();

        // entity 영속성 컨텍스트 수정
        updateSample.setTitle(dto.getTitle());
        updateSample.setContent(dto.getContent());
    }

    /**
     * Sample 삭제
     * @param dto
     */
    public void deleteSample(SampleDeleteRequestDTO dto) {

        // 삭제할 entity 조회
        Sample deleteSample = sampleRepository.findById(dto.getSampleSn()).orElseThrow();

        // 삭제
        sampleRepository.delete(deleteSample);
    }
}

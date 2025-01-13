package com.bootjpabase.carenation.api.sample.repository;

import com.bootjpabase.carenation.api.sample.domain.dto.request.SampleDetailRequestDTO;
import com.bootjpabase.carenation.api.sample.domain.dto.request.SampleListRequestDTO;
import com.bootjpabase.carenation.api.sample.domain.dto.response.SampleResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SampleRepositoryCustom {

//    private final JPAQueryFactory queryFactory;

    /**
     * Sample 목록 조회
     * @param dto
     * @return
     */
    public List<SampleResponseDTO> getSampleList(SampleListRequestDTO dto) {

        List<SampleResponseDTO> resultList = new ArrayList<>();


//        List<SampleResponseDTO> resultList = queryFactory


        return resultList;
    }

    /**
     * Sample 단건 조회
     * @param dto
     * @return
     */
    public SampleResponseDTO getSample(SampleDetailRequestDTO dto) {
        SampleResponseDTO sampleResponseDTO = new SampleResponseDTO();



        return sampleResponseDTO;
    }
}

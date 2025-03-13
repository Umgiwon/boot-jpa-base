package com.bootjpabase.global.file.repository;

import com.bootjpabase.global.file.domain.dto.response.FileResponseDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 파일 상세 조회
     * @param fileSn
     * @return
     */
    public FileResponseDTO getFile(Long fileSn) {
        FileResponseDTO result = new FileResponseDTO();

//        result = queryFactory
//                .select(
//                        new QFileResponseDTO(
//                                file.fileSn
//                                , file.realFileNm
//                                , file.saveFileNm
//                                , file.filePath
//                                , file.fileSize
//                        )
//                )
//                .from(file)
//                .where(file.fileSn.eq(fileSn))
//                .fetchOne();

        return result;
    }

}

package com.bootjpabase.api.file.repository;

import com.bootjpabase.api.file.domain.dto.response.FileResponseDTO;
import com.bootjpabase.api.file.domain.dto.response.QFileResponseDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.bootjpabase.api.file.domain.entity.QFile.file;


@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 파일 상세 조회
     *
     * @param fileSn 조회할 file 순번
     * @return 조회된 file 응답 dto
     */
    public FileResponseDTO getFile(Long fileSn) {
        return queryFactory
                .select(
                        new QFileResponseDTO(
                                file.fileSn
                                , file.realFileNm
                                , file.saveFileNm
                                , file.filePath
                                , file.fileSize
                        )
                )
                .from(file)
                .where(file.fileSn.eq(fileSn))
                .fetchOne();
    }
}

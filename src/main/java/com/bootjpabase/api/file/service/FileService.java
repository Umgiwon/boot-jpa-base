package com.bootjpabase.api.file.service;

import com.bootjpabase.api.file.domain.dto.response.FileResponseDTO;
import com.bootjpabase.api.file.repository.FileRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {

    private final FileRepositoryCustom fileRepositoryCustom;

    /**
     * 파일 상세 조회
     *
     * @param fileSn 조회할 파일 순번
     * @return 조회된 파일 응답 dto
     */
    public FileResponseDTO getFile(Long fileSn) {
        return fileRepositoryCustom.getFile(fileSn);
    }
}

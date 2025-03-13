package com.bootjpabase.global.file.service;

import com.bootjpabase.global.file.domain.dto.response.FileResponseDTO;
import com.bootjpabase.global.file.repository.FileRepository;
import com.bootjpabase.global.file.repository.FileRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {

    private final FileRepository fileRepository;
    private final FileRepositoryCustom fileRepositoryCustom;

    /**
     * 파일 상세 조회
     * @param fileSn
     * @return
     */
    public FileResponseDTO getFile(Long fileSn) {
        return fileRepositoryCustom.getFile(fileSn);
    }
}

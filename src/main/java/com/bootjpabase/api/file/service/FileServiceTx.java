package com.bootjpabase.api.file.service;

import com.bootjpabase.api.file.domain.dto.response.FileResponseDTO;
import com.bootjpabase.api.file.domain.entity.File;
import com.bootjpabase.api.file.mapper.FileMapper;
import com.bootjpabase.api.file.repository.FileRepository;
import com.bootjpabase.global.enums.common.ApiReturnCode;
import com.bootjpabase.global.enums.file.UploadFileType;
import com.bootjpabase.global.exception.BusinessException;
import com.bootjpabase.global.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class FileServiceTx {

    private final FileMapper fileMapper;
    private final FileRepository fileRepository;

    /**
     * 파일 저장
     *
     * @param file           저장할 파일
     * @param uploadFileType 파일 타입
     * @return 파일
     * @throws IOException IOException 처리
     */
    public File saveFile(MultipartFile file, UploadFileType uploadFileType) throws IOException {

        // 서버에 파일 upload
        File saveFile = FileUtil.uploadFile(file, uploadFileType);

        // 업로드된 파일정보 db에 저장
        return fileRepository.save(saveFile);
    }

    /**
     * 파일 삭제
     *
     * @param fileSn 삭제할 파일 순번
     * @return 삭제된 파일 dto
     */
    public FileResponseDTO deleteFile(Long fileSn) {

        // DB에 저장된 삭제할 entity 조회
        File deleteFile = fileRepository.findById(fileSn)
                .orElseThrow(() -> new BusinessException(ApiReturnCode.NO_FILE_DATA_ERROR));

        // 서버에 저장된 파일 삭제
        FileUtil.deleteFile(deleteFile);

        // DB에 저장된 파일정보 삭제
        fileRepository.delete(deleteFile);

        // 삭제 후 dto 반환
        return fileMapper.toFileResponseDTO(deleteFile);
    }
}

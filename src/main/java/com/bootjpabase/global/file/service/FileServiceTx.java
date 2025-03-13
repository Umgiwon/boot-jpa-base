package com.bootjpabase.global.file.service;

import com.bootjpabase.global.enums.common.ApiReturnCode;
import com.bootjpabase.global.enums.file.UploadFileType;
import com.bootjpabase.global.exception.BusinessException;
import com.bootjpabase.global.file.domain.entity.File;
import com.bootjpabase.global.file.repository.FileRepository;
import com.bootjpabase.global.file.repository.FileRepositoryCustom;
import com.bootjpabase.global.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class FileServiceTx {

    private final FileRepository fileRepository;
    private final FileRepositoryCustom fileRepositoryCustom;

    /**
     * 파일 저장
     * @param uploadFileType
     * @param file
     * @return
     * @throws IOException
     */
    public File saveFile(MultipartFile file, UploadFileType uploadFileType) throws IOException {

        // 서버에 파일 upload
        File saveFile = FileUtils.uploadFile(file, uploadFileType);

        // 업로드된 파일정보 db에 저장
        return fileRepository.save(saveFile);
    }

    /**
     * 파일 삭제
     * @param fileSn
     * @return
     */
    public boolean deleteFile(Long fileSn) {

        // DB에 저장된 삭제할 entity 조회
        File deleteFile = fileRepository.findById(fileSn)
                .orElseThrow(() -> new BusinessException(ApiReturnCode.NO_FILE_DATA_ERROR));

        // 서버에 저장된 파일 삭제
        FileUtils.deleteFile(deleteFile);

        // DB에 저장된 파일정보 삭제
        fileRepository.delete(deleteFile);

        return true;
    }
}

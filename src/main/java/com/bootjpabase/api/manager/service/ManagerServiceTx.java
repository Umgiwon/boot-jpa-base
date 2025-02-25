package com.bootjpabase.api.manager.service;

import com.nangman.api.admin.system.manager.domain.dto.request.ManagerSaveRequestDTO;
import com.nangman.api.admin.system.manager.domain.dto.request.ManagerUpdateRequestDTO;
import com.nangman.api.admin.system.manager.domain.entity.Manager;
import com.nangman.api.admin.system.manager.repository.ManagerRepository;
import com.nangman.api.admin.system.manager.repository.ManagerRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class ManagerServiceTx {

    private final ManagerRepository managerRepository;
    private final ManagerRepositoryCustom managerRepositoryCustom;
    private final BCryptPasswordEncoder encoder;


    /**
     * 관리자 저장
     * @param dto
     * @return
     */
    public boolean saveManager(ManagerSaveRequestDTO dto) {
        boolean result = false;

        // 저장할 entity 객체 생성
        Manager saveManager = Manager.builder()
                .id(dto.getId())
                .password(encoder.encode(dto.getPassword()))
                .name(dto.getName())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .roleCode(dto.getRoleCode())
                .build();

        // 관리자 저장
        managerRepository.save(saveManager);
        result = true;

        return result;
    }

    /**
     * 관리자 수정
     * @param dto
     * @return
     */
    public boolean updateManager(ManagerUpdateRequestDTO dto) {
        boolean result = false;

        // 수정할 entity 조회
        Manager updateManager = managerRepository.findById(dto.getId()).orElse(null);

        if(!ObjectUtils.isEmpty(updateManager)) {

            // entity 영속성 컨텍스트 수정
            if(!ObjectUtils.isEmpty(dto.getPassword())) {
                updateManager.setPassword(dto.getPassword());
            }

            if(!ObjectUtils.isEmpty(dto.getPhone())) {
                updateManager.setPhone(dto.getPhone());
            }

            if(!ObjectUtils.isEmpty(dto.getEmail())) {
                updateManager.setEmail(dto.getEmail());
            }

            if(!ObjectUtils.isEmpty(dto.getRoleCode())) {
                updateManager.setRoleCode(dto.getRoleCode());
            }

            result = true;
        }

        return result;
    }

    /**
     * 관리자 삭제
     * @param dto
     * @return
     */
    public boolean deleteManager(String id) {
        boolean result = false;

        // 삭제할 entity 조회
        Manager deleteManager = managerRepository.findById(id).orElse(null);

        // 삭제
        if(!ObjectUtils.isEmpty(deleteManager)) {
            managerRepository.delete(deleteManager);
            result = true;
        }

        return result;
    }
}

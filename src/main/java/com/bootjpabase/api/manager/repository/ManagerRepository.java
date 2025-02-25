package com.bootjpabase.api.manager.repository;

import com.nangman.api.admin.system.manager.domain.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, String> {
}

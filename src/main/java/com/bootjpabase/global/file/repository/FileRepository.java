package com.bootjpabase.global.file.repository;

import com.bootjpabase.global.file.domain.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}

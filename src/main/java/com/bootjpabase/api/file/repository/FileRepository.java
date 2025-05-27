package com.bootjpabase.api.file.repository;

import com.bootjpabase.api.file.domain.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}

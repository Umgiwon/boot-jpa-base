package com.bootjpabase.api.sample.repository;

import com.bootjpabase.api.sample.domain.entity.Sample;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepository extends JpaRepository<Sample, Long> {
    boolean existsByTitle(String title);
}

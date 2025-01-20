package com.bootjpabase.carmanager.api.sample.repository;

import com.bootjpabase.carmanager.api.sample.domain.entity.Sample;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepository extends JpaRepository<Sample, Long> {
}

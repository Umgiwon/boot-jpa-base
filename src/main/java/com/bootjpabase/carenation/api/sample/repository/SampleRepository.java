package com.bootjpabase.carenation.api.sample.repository;

import com.bootjpabase.carenation.api.sample.domain.entity.Sample;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepository extends JpaRepository<Sample, Long> {
}

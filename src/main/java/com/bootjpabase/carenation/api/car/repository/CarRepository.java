package com.bootjpabase.carenation.api.car.repository;

import com.bootjpabase.carenation.api.car.domain.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}

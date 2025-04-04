package com.bootjpabase.api.car.repository;

import com.bootjpabase.api.car.domain.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
    Car findByModelName(String modelName);
}

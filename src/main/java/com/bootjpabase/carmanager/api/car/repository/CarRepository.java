package com.bootjpabase.carmanager.api.car.repository;

import com.bootjpabase.carmanager.api.car.domain.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}

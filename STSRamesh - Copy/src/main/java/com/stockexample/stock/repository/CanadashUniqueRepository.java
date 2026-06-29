package com.stockexample.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stockexample.stock.entity.CanadashUniqueEntity;

@Repository
public interface CanadashUniqueRepository extends JpaRepository<CanadashUniqueEntity, Long> {

}

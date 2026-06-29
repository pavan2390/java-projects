package com.stockexample.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stockexample.stock.entity.IndiaUniqueEntity;

@Repository
public interface IndiaUniqueRepository extends JpaRepository<IndiaUniqueEntity, Long> {

}

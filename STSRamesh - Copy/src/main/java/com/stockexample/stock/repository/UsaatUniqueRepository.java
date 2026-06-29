package com.stockexample.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stockexample.stock.entity.UsaatUniqueEntity;



@Repository
public interface UsaatUniqueRepository extends JpaRepository<UsaatUniqueEntity, Long> {

    /*
     * Copy every method from
     * CanadaatUniqueRepository.
     */

}

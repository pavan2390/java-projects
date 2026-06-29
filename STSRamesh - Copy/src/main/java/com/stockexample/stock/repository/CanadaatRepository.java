package com.stockexample.stock.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.stockexample.stock.entity.CanadaatEntity;

public interface CanadaatRepository extends JpaRepository<CanadaatEntity, Long> {
	
	Page<CanadaatEntity> findAllBySymbol(String symbol, Pageable pageable);
	
	@Query("select distinct symbol from CanadaatEntity order by symbol asc")
	Page<String> findAllDistinctSymbol(Pageable pageable);
	
	@Query("select distinct symbol from CanadaatEntity c where c.date between ?1 and ?2 order by symbol asc")
	Page<String> findAllDistinctSymbolByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);

	@Query("select c from CanadaatEntity c where c.symbol = ?1 and c.date between ?2 and ?3")
	Page<CanadaatEntity> findAllBySymbolAndDateRange(String symbol, LocalDate startDate, LocalDate endDate, Pageable pageable);
	
	@Query("select c from CanadaatEntity c where c.date between ?1 and ?2")
	Page<CanadaatEntity> findAllByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);
	
	@Query("select a from CanadaatEntity a inner join (select min(id) as id, date as date, symbol as symbol from CanadaatEntity group by date, symbol) b on a.id = b.id order by a.date")
	Page<CanadaatEntity> findAllUniqueRecords(Pageable pageable); //This is unique by date and symbol column
	
	@Query("select a from CanadaatEntity a inner join (select min(id) as id, date as date, symbol as symbol from CanadaatEntity group by date, symbol) b on a.id = b.id where a.date between ?1 and ?2 order by a.date")
	Page<CanadaatEntity> findAllUniqueRecordsByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);
	
	@Query("select a from CanadaatEntity a inner join (select min(id) as id, date as date, symbol as symbol from CanadaatEntity group by date, symbol) b on a.id = b.id where a.symbol = ?1 and a.date between ?2 and ?3 order by a.date")
	Page<CanadaatEntity> findAllUniqueRecordsBySymbolAndDateRange(String symbol, LocalDate startDate, LocalDate endDate, Pageable pageable);
	
	@Query("select a from CanadaatEntity a inner join (select min(id) as id, date as date, symbol as symbol from CanadaatEntity group by date, symbol) b on a.id = b.id where a.symbol = ?1 order by a.date")
	Page<CanadaatEntity> findAllUniqueRecordsBySymbol(String symbol, Pageable pageable);
}
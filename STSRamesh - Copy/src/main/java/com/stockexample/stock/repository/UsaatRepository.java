package com.stockexample.stock.repository;



import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.stockexample.stock.entity.UsaatEntity;

@Repository
public interface UsaatRepository extends JpaRepository<UsaatEntity, Long> {

Page<UsaatEntity> findAllBySymbol(String symbol, Pageable pageable);
	
	@Query("select distinct symbol from UsaatEntity order by symbol asc")
	Page<String> findAllDistinctSymbol(Pageable pageable);
	
	@Query("select distinct symbol from UsaatEntity c where c.date between ?1 and ?2 order by symbol asc")
	Page<String> findAllDistinctSymbolByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);

	@Query("select c from UsaatEntity c where c.symbol = ?1 and c.date between ?2 and ?3")
	Page<UsaatEntity> findAllBySymbolAndDateRange(String symbol, LocalDate startDate, LocalDate endDate, Pageable pageable);
	
	@Query("select c from UsaatEntity c where c.date between ?1 and ?2")
	Page<UsaatEntity> findAllByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);
	
	@Query("select a from UsaatEntity a inner join (select min(id) as id, date as date, symbol as symbol from UsaatEntity group by date, symbol) b on a.id = b.id order by a.date")
	Page<UsaatEntity> findAllUniqueRecords(Pageable pageable); //This is unique by date and symbol column
	
	@Query("select a from UsaatEntity a inner join (select min(id) as id, date as date, symbol as symbol from UsaatEntity group by date, symbol) b on a.id = b.id where a.date between ?1 and ?2 order by a.date")
	Page<UsaatEntity> findAllUniqueRecordsByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);
	
	@Query("select a from UsaatEntity a inner join (select min(id) as id, date as date, symbol as symbol from UsaatEntity group by date, symbol) b on a.id = b.id where a.symbol = ?1 and a.date between ?2 and ?3 order by a.date")
	Page<UsaatEntity> findAllUniqueRecordsBySymbolAndDateRange(String symbol, LocalDate startDate, LocalDate endDate, Pageable pageable);
	
	@Query("select a from UsaatEntity a inner join (select min(id) as id, date as date, symbol as symbol from UsaatEntity group by date, symbol) b on a.id = b.id where a.symbol = ?1 order by a.date")
	Page<UsaatEntity> findAllUniqueRecordsBySymbol(String symbol, Pageable pageable);
}

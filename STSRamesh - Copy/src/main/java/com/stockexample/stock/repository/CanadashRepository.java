package com.stockexample.stock.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.stockexample.stock.entity.CanadashEntity;

@Repository
public interface CanadashRepository extends JpaRepository<CanadashEntity, Long> {

	Page<CanadashEntity> findAllBySymbol(String symbol, Pageable pageable);

	@Query("select distinct symbol from CanadashEntity order by symbol asc")
	Page<String> findAllDistinctSymbol(Pageable pageable);

	@Query("select distinct symbol from CanadashEntity c where c._date between ?1 and ?2 order by symbol asc")
	Page<String> findAllDistinctSymbolByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);

	@Query("select c from CanadashEntity c where c.symbol = ?1 and c._date between ?2 and ?3")
	Page<CanadashEntity> findAllBySymbolAndDateRange(String symbol, LocalDate startDate, LocalDate endDate, Pageable pageable);

	@Query("select c from CanadashEntity c where c._date between ?1 and ?2")
	Page<CanadashEntity> findAllByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);

	@Query("select a from CanadashEntity a inner join (select min(id) as id, _date as _date, symbol as symbol from CanadashEntity group by _date, symbol) b on a.id = b.id order by a._date")
	Page<CanadashEntity> findAllUniqueRecords(Pageable pageable); // This is unique by date and symbol column

	@Query("select a from CanadashEntity a inner join (select min(id) as id, _date as _date, symbol as symbol from CanadashEntity group by _date, symbol) b on a.id = b.id where a._date between ?1 and ?2 order by a._date")
	Page<CanadashEntity> findAllUniqueRecordsByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);

	@Query("select a from CanadashEntity a inner join (select min(id) as id, _date as _date, symbol as symbol from CanadashEntity group by _date, symbol) b on a.id = b.id where a.symbol = ?1 and a._date between ?2 and ?3 order by a._date")
	Page<CanadashEntity> findAllUniqueRecordsBySymbolAndDateRange(String symbol, LocalDate startDate, LocalDate endDate, Pageable pageable);

	@Query("select a from CanadashEntity a inner join (select min(id) as id, _date as _date, symbol as symbol from CanadashEntity group by _date, symbol) b on a.id = b.id where a.symbol = ?1 order by a._date")
	Page<CanadashEntity> findAllUniqueRecordsBySymbol(String symbol, Pageable pageable);
}

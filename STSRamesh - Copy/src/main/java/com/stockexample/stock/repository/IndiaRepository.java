package com.stockexample.stock.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.stockexample.stock.entity.IndiaEntity;

@Repository
public interface IndiaRepository extends JpaRepository<IndiaEntity, Long> {

	Page<IndiaEntity> findAllByScripName(String scripName, Pageable pageable);

	@Query("select distinct scripName from IndiaEntity order by scripName asc")
	Page<String> findAllDistinctScripName(Pageable pageable);

	@Query("select distinct scripName from IndiaEntity c where c.date between ?1 and ?2 order by scripName asc")
	Page<String> findAllDistinctScripNameByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);

	@Query("select c from IndiaEntity c where c.scripName = ?1 and c.date between ?2 and ?3")
	Page<IndiaEntity> findAllByScripNameAndDateRange(String scripName, LocalDate startDate, LocalDate endDate, Pageable pageable);

	@Query("select c from IndiaEntity c where c.date between ?1 and ?2")
	Page<IndiaEntity> findAllByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);

	@Query("select a from IndiaEntity a inner join (select min(id) as id, date as date, scripName as scripName from IndiaEntity group by date, scripName) b on a.id = b.id order by a.date")
	Page<IndiaEntity> findAllUniqueRecords(Pageable pageable); // This is unique by date and symbol column

	@Query("select a from IndiaEntity a inner join (select min(id) as id, date as date, scripName as scripName from IndiaEntity group by date, scripName) b on a.id = b.id where a.date between ?1 and ?2 order by a.date")
	Page<IndiaEntity> findAllUniqueRecordsByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);

	@Query("select a from IndiaEntity a inner join (select min(id) as id, date as date, scripName as scripName from IndiaEntity group by date, scripName) b on a.id = b.id where a.scripName = ?1 and a.date between ?2 and ?3 order by a.date")
	Page<IndiaEntity> findAllUniqueRecordsByScripNameAndDateRange(String symbol, LocalDate startDate, LocalDate endDate, Pageable pageable);

	@Query("select a from IndiaEntity a inner join (select min(id) as id, date as date, scripName as scripName from IndiaEntity group by date, scripName) b on a.id = b.id where a.scripName = ?1 order by a.date")
	Page<IndiaEntity> findAllUniqueRecordsByScripName(String symbol, Pageable pageable);
}

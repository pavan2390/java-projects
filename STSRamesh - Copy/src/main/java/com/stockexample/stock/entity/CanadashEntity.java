package com.stockexample.stock.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Canadash")
@Getter
@Setter
public class CanadashEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // Primary key, if applicable

	private LocalDate _date;

	private String dayOfWeek;
	
	private Integer dayOfWeekName;

	private Integer weekOfMonth;

	private Integer weekOfYear;

	private String watchList;
	
	private String ticker;
	
	private String c;
	
	private String q;
	
	private String name;
	
	private Double eps;
	
	private Double pe;
	
	private Double yield;
	
	private Double shrOs;
	
	private Double _div;
	
	private LocalDate divPayDt;
	
	private LocalDate exDiv;

	private Double marketCapinMillions;
		
	private Double fiftyTwoWeekRange;

	private String symbol;
	
	private String curr;
	
	private Double _change;

	private Double percentChange;

	private Double mark;

	private Double avgCost;
	
	private Double bookCost;
	
	private Double pl;

	private Double dayPl;
	
	private Double dayPercentagePl;
	
	private Double percentagePl;
	
	private String volume;
	
	private String averageVolumeOfSixMonth;
	
	private String blkVolume;
	
	private Double marketCap;
	
	private Double marketVal;
	
	private Double openInt;

	private Double openIntChange;
		
	private String callPut;
	
	private String exch;

	private Double _open;
	
	private Double close;

	private Double fiftyTwotWeekLow;

	private Double low;

	private Double bid;
	
	private Double bidSz;

	private Double ask;
	
	private Double askSz;
	
	private Double trdSz;
	
	private Double price;

	private Double high;
	
	private Integer quantity;
	
	private Double fiftyTwotWeekHigh;
	
	private Double impVol;

	private String prcTck;

	private LocalDate expDate;
	
	private LocalDate entryDate;
	
	private String quoteTrend;

	private Double trade;
	
	private Double trend;

	private Double strike;

	private Double _range;

	private String relRange;

	private Double numTrd;
	
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime dateTimeStamp;
}

/**
 * 
 */
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

/**
 * 
 */
@Entity
@Table(name = "CanadaAT")
@Getter
@Setter
public class CanadaatEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // Primary key, if applicable

	private LocalDate date;

	private Integer dayOfTheWeek;

	private Integer weekOfTheMonth;

	private Integer weekOfTheYear;

	private String watchList;

	private Double marketCapNinMillions;
	
	private Double marketCapInMNumbers;
	
	private Double fiftyTwoWeekDiff;

	private String symbol;

	private String description;

	private Double percentChange;

	private Double beta;

	private Double mark;

	private Double lastR;

	private Long openInt;

	private Double spread;

	private Double netChange;

	private Long bidSize;

	private String size;

	private Long askSize;

	private Long lastSize;
	
	private String volume;
	
	private String MarketCap;

	private String instrument;
	
	private String shares;

	private Double Open1;

	private Double fiftyTwotWeekLow;

	private Double low;

	private Double bid;

	private Double ask;

	private Double high;

	private Double prevClose;
	
	private Double fiftyTwotWeekHigh;

	private String cusip;

	private Double maintenaceMargin;

	private Integer daysToExpiration;

	private Double delta;

	private String expirationDate;

	private String firstNoticeDate;

	private Double impliedVolatility;

	private Double gamma;

	private String tradingHalt;

	private String lastTradeDate;

	private Double limitDown;

	private Double limitUp;

	private Double rho;

	private Double theoPrice;

	private Double strike;

	private Double theta;

	private Double tickValue;

	private Double vega;
	
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime dateTimeStamp;
}

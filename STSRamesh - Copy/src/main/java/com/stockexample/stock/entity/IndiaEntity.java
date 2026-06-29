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
@Table(name = "IndiaAT")
@Getter
@Setter
public class IndiaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // Primary key, if applicable

	private LocalDate date;

	private Integer dayOfTheWeek;

	private Integer weekOfTheMonth;

	private Integer weekOfTheYear;

	private String watchList;

	private Double fiftyTwoWeekDiff;

	private String selected;

	private String exchange;

	private String newsEvent;

	private String scripName;

	private String ScripCode;

	private String CompanyName;

	private Double priceTickSize;

	private Double percentChange;

	private String sectorIndustry;

	private Integer lastTradedQty;

	private Double fiftyTwoWeekHigh;

	private Double high;

	private Double offerPrice;

	private Double upperCircuit;

	private Double Current1;

	private Double lowerCircuit;

	private Double avgRate;

	private Double Open1;

	private Double Close1;

	private Double bidPrice;

	private Double low;

	private Double fiftyTwoWeekLow;

	private Double turnOver;

	private Integer bidQty;

	private Integer offerQty;

	private Integer qty;

	private Integer totalBuyQty;

	private Integer totalSellQty;

	private String lastUpdatedTime;

	private String lastTradedTime;

	private String lastTradedDate;

	private Double pOpen;

	private Double pHigh;

	private Double pLow;

	private Double pClose;

	private Integer pQuantity;

	private Double pivotRes3;

	private Double pivotRes2;

	private Double pivotRes1;

	private Double pivot1;

	private Double pivotSup1;

	private Double pivotSup2;

	private Double PivotSup3;

	private Integer oIDifference;

	private Double oIDifferencePercentage;

	private String indexGroups;

	private String instrumentType;

	private String callPut;

	private Double strikePrice;

	private Integer lotSize;

	private String underlying;

	private String priceQuotation;

	private Integer openInterest;

	private String expiryDate;

	private Double difference;

	private String shortExchangeName;

	private Double settlementPrice;

	private Double prevVal;

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime dateTimeStamp;

}

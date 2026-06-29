package com.stockexample.stock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USAAT_UNIQUE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsaatUniqueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Copy every remaining field from
     * CanadaatUniqueEntity.
     *
     * Example only:
     */

    private String symbol;

    private String exchange;

    private String isin;

    private String cusip;

    private String sedol;

    /*
     * Continue copying every field exactly
     * from CanadaatUniqueEntity.
     */
}
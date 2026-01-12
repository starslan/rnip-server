package ru.starslan.rnip_server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BudgetIndexDTO {
    private String status;
    private String payReason;
    private String taxPeriod;
    private String docNumber;
    private Date docDate;
}

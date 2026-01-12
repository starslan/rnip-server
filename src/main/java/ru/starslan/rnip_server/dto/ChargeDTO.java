package ru.starslan.rnip_server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChargeDTO {
    private String supplierBillId;
    private Timestamp billDate;
    private Date validUntil;
    private Long totalAmount;
    private String purpose;
    private String kbk;
    private String oktmo;
    private Date deliveryDate;
    private String legalAct;
    private Date paymentTerm;
    private String origin;
    private Integer noticeTerm;
    private String okved;
    private Boolean chargeOffense;
    private Long payeeId;
    private Long payerId;
    private Long budgetIndexId;
    private Long executiveProcInfoId;
}

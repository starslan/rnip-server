package ru.starslan.rnip_server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.starslan.rnip_server.model.ChangeStatusMeaning;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private String paymentId;
    private Timestamp paymentDate;
    private Long amount;
    private String transKind;
    private String supplierBillId;
    private String purpose;
    private Date receiptDate;
    private String kbk;
    private String oktmo;
    private Date deliveryDate;
    private String esiaId;
    private String accDocNo;
    private Date accDocDate;
    private ChangeStatusMeaning changeStatusMeaning;
    private Timestamp changeStatusChangeDate;
    private String changeStatusReason;
    private String paymentOrgType;
    private String paymentOrgBik;
    private String paymentOrgName;
    private String paymentOrgAccount;
    private String paymentOrgUfk;
    private String payerName;
    private String payerAccount;
    private String payerIdentifier;
    private Long chargeId;
    private Long payeeId;
    private Long payerId;
    private Long budgetIndexId;
}

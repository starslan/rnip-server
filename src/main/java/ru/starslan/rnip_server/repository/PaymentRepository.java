package ru.starslan.rnip_server.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.starslan.rnip_server.dto.PaymentDTO;
import ru.starslan.rnip_server.model.ChangeStatusMeaning;

@Repository
public class PaymentRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PaymentRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Long insertPayment(PaymentDTO paymentDTO) {
        String insertSql = "INSERT INTO payment (" +
                    "payment_id, payment_date, amount, trans_kind, supplier_bill_id, " +
                    "purpose, receipt_date, kbk, oktmo, delivery_date, esia_id, " +
                    "acc_doc_no, acc_doc_date, change_status_meaning, change_status_change_date, " +
                    "change_status_reason, payment_org_type, payment_org_bik, payment_org_name, " +
                    "payment_org_account, payment_org_ufk, payer_name, payer_account, " +
                    "payer_identifier, charge_id, payee_id, payer_id, budget_index_id" +
                ") VALUES (" +
                    ":paymentId, :paymentDate, :amount, :transKind, :supplierBillId, " +
                    ":purpose, :receiptDate, :kbk, :oktmo, :deliveryDate, :esiaId, " +
                    ":accDocNo, :accDocDate, :changeStatusMeaning, :changeStatusChangeDate, " +
                    ":changeStatusReason, :paymentOrgType, :paymentOrgBik, :paymentOrgName, " +
                    ":paymentOrgAccount, :paymentOrgUfk, :payerName, :payerAccount, " +
                    ":payerIdentifier, :chargeId, :payeeId, :payerId, :budgetIndexId" +
                ")";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("paymentId", paymentDTO.getPaymentId())
                .addValue("paymentDate", paymentDTO.getPaymentDate())
                .addValue("amount", paymentDTO.getAmount())
                .addValue("transKind", paymentDTO.getTransKind())
                .addValue("supplierBillId", paymentDTO.getSupplierBillId())
                .addValue("purpose", paymentDTO.getPurpose())
                .addValue("receiptDate", paymentDTO.getReceiptDate())
                .addValue("kbk", paymentDTO.getKbk())
                .addValue("oktmo", paymentDTO.getOktmo())
                .addValue("deliveryDate", paymentDTO.getDeliveryDate())
                .addValue("esiaId", paymentDTO.getEsiaId())
                .addValue("accDocNo", paymentDTO.getAccDocNo())
                .addValue("accDocDate", paymentDTO.getAccDocDate())
                .addValue("changeStatusMeaning", paymentDTO.getChangeStatusMeaning() != null ? paymentDTO.getChangeStatusMeaning().getCode() : null)
                .addValue("changeStatusChangeDate", paymentDTO.getChangeStatusChangeDate())
                .addValue("changeStatusReason", paymentDTO.getChangeStatusReason())
                .addValue("paymentOrgType", paymentDTO.getPaymentOrgType())
                .addValue("paymentOrgBik", paymentDTO.getPaymentOrgBik())
                .addValue("paymentOrgName", paymentDTO.getPaymentOrgName())
                .addValue("paymentOrgAccount", paymentDTO.getPaymentOrgAccount())
                .addValue("paymentOrgUfk", paymentDTO.getPaymentOrgUfk())
                .addValue("payerName", paymentDTO.getPayerName())
                .addValue("payerAccount", paymentDTO.getPayerAccount())
                .addValue("payerIdentifier", paymentDTO.getPayerIdentifier())
                .addValue("chargeId", paymentDTO.getChargeId())
                .addValue("payeeId", paymentDTO.getPayeeId())
                .addValue("payerId", paymentDTO.getPayerId())
                .addValue("budgetIndexId", paymentDTO.getBudgetIndexId());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(insertSql, params, keyHolder, new String[]{"id"});

        Number key = keyHolder.getKey();
        return key != null ? key.longValue() : null;
    }
}

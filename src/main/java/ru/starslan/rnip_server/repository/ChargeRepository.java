package ru.starslan.rnip_server.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.starslan.rnip_server.dto.ChargeDTO;

@Repository
public class ChargeRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ChargeRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Long insertCharge(ChargeDTO chargeDTO) {
        String insertSql = "INSERT INTO charge (" +
                    "supplier_bill_id, bill_date, valid_until, total_amount, purpose, " +
                    "kbk, oktmo, delivery_date, legal_act, payment_term, origin, " +
                    "notice_term, okved, charge_offense, payee_id, payer_id, " +
                    "budget_index_id, executive_proc_info_id" +
                ") VALUES (:supplierBillId, :billDate, :validUntil, :totalAmount, :purpose, " +
                ":kbk, :oktmo, :deliveryDate, :legalAct, :paymentTerm, :origin, " +
                ":noticeTerm, :okved, :chargeOffense, :payeeId, :payerId, " +
                ":budgetIndexId, :executiveProcInfoId)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("supplierBillId", chargeDTO.getSupplierBillId())
                .addValue("billDate", chargeDTO.getBillDate())
                .addValue("validUntil", chargeDTO.getValidUntil())
                .addValue("totalAmount", chargeDTO.getTotalAmount())
                .addValue("purpose", chargeDTO.getPurpose())
                .addValue("kbk", chargeDTO.getKbk())
                .addValue("oktmo", chargeDTO.getOktmo())
                .addValue("deliveryDate", chargeDTO.getDeliveryDate())
                .addValue("legalAct", chargeDTO.getLegalAct())
                .addValue("paymentTerm", chargeDTO.getPaymentTerm())
                .addValue("origin", chargeDTO.getOrigin())
                .addValue("noticeTerm", chargeDTO.getNoticeTerm())
                .addValue("okved", chargeDTO.getOkved())
                .addValue("chargeOffense", chargeDTO.getChargeOffense() != null && chargeDTO.getChargeOffense())
                .addValue("payeeId", chargeDTO.getPayeeId())
                .addValue("payerId", chargeDTO.getPayerId())
                .addValue("budgetIndexId", chargeDTO.getBudgetIndexId())
                .addValue("executiveProcInfoId", chargeDTO.getExecutiveProcInfoId());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(insertSql, params, keyHolder, new String[]{"id"});

        Number key = keyHolder.getKey();
        return key.longValue();
    }
}

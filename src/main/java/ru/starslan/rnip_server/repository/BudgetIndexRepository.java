package ru.starslan.rnip_server.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.starslan.rnip_server.dto.BudgetIndexDTO;

@Repository
public class BudgetIndexRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BudgetIndexRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Long findOrCreate(BudgetIndexDTO budgetIndexDTO) {
        String selectSql = "SELECT id FROM budget_index WHERE status = :status AND pay_reason = :payReason AND tax_period = :taxPeriod " +
                "AND COALESCE(doc_number, '') = COALESCE(:docNumber, '') AND COALESCE(doc_date::text, '') = COALESCE(:docDate::text, '')";
        
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("status", budgetIndexDTO.getStatus())
                .addValue("payReason", budgetIndexDTO.getPayReason())
                .addValue("taxPeriod", budgetIndexDTO.getTaxPeriod())
                .addValue("docNumber", budgetIndexDTO.getDocNumber())
                .addValue("docDate", budgetIndexDTO.getDocDate());
        
        Long existingId = namedParameterJdbcTemplate.query(selectSql, params,
            (rs, rowNum) -> rs.getLong("id")).stream().findFirst().orElse(null);

        if (existingId != null) {
            return existingId;
        }

        String insertSql = "INSERT INTO budget_index (status, pay_reason, tax_period, doc_number, doc_date) " +
                "VALUES (:status, :payReason, :taxPeriod, :docNumber, :docDate)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        namedParameterJdbcTemplate.update(insertSql, params, keyHolder, new String[]{"id"});

        Number key = keyHolder.getKey();
        return key.longValue();
    }
}

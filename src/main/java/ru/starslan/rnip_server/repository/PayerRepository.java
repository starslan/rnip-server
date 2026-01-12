package ru.starslan.rnip_server.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.starslan.rnip_server.dto.PayerDTO;

@Repository
public class PayerRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PayerRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Long findOrCreate(PayerDTO payerDTO) {
        String selectSql = "SELECT id FROM payer WHERE payer_identifier = :payerIdentifier";
        
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("payerIdentifier", payerDTO.getPayerIdentifier());
        
        Long existingId = namedParameterJdbcTemplate.query(selectSql, params,
            (rs, rowNum) -> rs.getLong("id")).stream().findFirst().orElse(null);

        if (existingId != null) {
            return existingId;
        }

        String insertSql = "INSERT INTO payer (payer_type, payer_name, payer_identifier, additional_payer_identifier) " +
                "VALUES (:payerType, :payerName, :payerIdentifier, :additionalPayerIdentifier)";
        
        params.addValue("payerType", payerDTO.getPayerType().name())
                .addValue("payerName", payerDTO.getPayerName())
                .addValue("additionalPayerIdentifier", payerDTO.getAdditionalPayerIdentifier());
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        namedParameterJdbcTemplate.update(insertSql, params, keyHolder, new String[]{"id"});

        Number key = keyHolder.getKey();
        return key.longValue();
    }
}

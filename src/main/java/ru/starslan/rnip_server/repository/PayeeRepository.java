package ru.starslan.rnip_server.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.starslan.rnip_server.dto.PayeeDTO;

@Repository
public class PayeeRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PayeeRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Long findOrCreate(PayeeDTO payeeDTO) {
        String selectSql = "SELECT id FROM payee WHERE inn = :inn AND kpp = :kpp";
        
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("inn", payeeDTO.getInn())
                .addValue("kpp", payeeDTO.getKpp());
        
        Long existingId = namedParameterJdbcTemplate.query(selectSql, params,
            (rs, rowNum) -> rs.getLong("id")).stream().findFirst().orElse(null);

        if (existingId != null) {
            return existingId;
        }

        String insertSql = "INSERT INTO payee (inn, kpp, name, ogrn, account, bank_name) " +
                "VALUES (:inn, :kpp, :name, :ogrn, :account, :bankName)";
        
        params.addValue("name", payeeDTO.getName())
                .addValue("ogrn", payeeDTO.getOgrn())
                .addValue("account", payeeDTO.getAccount())
                .addValue("bankName", payeeDTO.getBankName());
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        namedParameterJdbcTemplate.update(insertSql, params, keyHolder, new String[]{"id"});

        Number key = keyHolder.getKey();
        return key.longValue();
    }
}

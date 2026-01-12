package ru.starslan.rnip_server.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.starslan.rnip_server.dto.ExecutiveProcedureInfoDTO;

@Repository
public class ExecutiveProcedureInfoRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ExecutiveProcedureInfoRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Long findOrCreate(ExecutiveProcedureInfoDTO execProcInfoDTO) {
        String selectSql = "SELECT id FROM executive_procedure_info WHERE exec_doc_number = :execDocNumber AND exec_doc_date = :execDocDate";
        
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("execDocNumber", execProcInfoDTO.getExecDocNumber())
                .addValue("execDocDate", execProcInfoDTO.getExecDocDate());
        
        Long existingId = namedParameterJdbcTemplate.query(selectSql, params,
            (rs, rowNum) -> rs.getLong("id")).stream().findFirst().orElse(null);

        if (existingId != null) {
            return existingId;
        }

        String insertSql = "INSERT INTO executive_procedure_info (exec_doc_number, exec_doc_date, bailiff_dept) " +
                "VALUES (:execDocNumber, :execDocDate, :bailiffDept)";
        
        params.addValue("bailiffDept", execProcInfoDTO.getBailiffDept());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        namedParameterJdbcTemplate.update(insertSql, params, keyHolder, new String[]{"id"});

        Number key = keyHolder.getKey();
        return key.longValue();
    }
}

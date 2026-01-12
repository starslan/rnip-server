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
public class ExecutiveProcedureInfoDTO {
    private String execDocNumber;
    private Date execDocDate;
    private String bailiffDept;
}

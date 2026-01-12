package ru.starslan.rnip_server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayeeDTO {
    private String inn;
    private String kpp;
    private String name;
    private String ogrn;
    private String account;
    private String bankName;
}

package ru.starslan.rnip_server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.starslan.rnip_server.model.PayerType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayerDTO {
    private PayerType payerType;
    private String payerName;
    private String payerIdentifier;
    private String additionalPayerIdentifier;
}

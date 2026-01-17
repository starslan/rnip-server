package ru.starslan.rnip_server.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.starslan.rnip_server.model.ChargeOrigin;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChargeDTO {
    
    @NotBlank(message = "УИН (supplierBillId) не может быть пустым")
    @Pattern(regexp = "(\\w{20})|(\\d{25})", message = "УИН должен содержать либо 20 букв/цифр, либо 25 цифр")
    @Size(max = 25, message = "УИН не может быть длиннее 25 символов")
    private String supplierBillId;
    
    @NotNull(message = "Дата выставления начисления (billDate) обязательна")
    private Timestamp billDate;
    
    private Date validUntil;
    
    @NotNull(message = "Сумма начисления (totalAmount) обязательна")
    @Positive(message = "Сумма начисления должна быть положительной")
    private Long totalAmount;
    
    @NotBlank(message = "Назначение платежа (purpose) обязательно")
    @Size(max = 210, message = "Назначение платежа не может быть длиннее 210 символов")
    private String purpose;
    
    @NotBlank(message = "КБК (kbk) обязателен")
    @Size(max = 20, message = "КБК не может быть длиннее 20 символов")
    private String kbk;
    
    @NotBlank(message = "ОКТМО (oktmo) обязательно")
    @Size(max = 11, message = "ОКТМО не может быть длиннее 11 символов")
    private String oktmo;
    
    private Date deliveryDate;
    
    @Size(max = 255, message = "Нормативный правовой акт не может быть длиннее 255 символов")
    private String legalAct;
    
    private Date paymentTerm;
    
    private ChargeOrigin origin;
    
    @Min(value = 0, message = "Количество дней до повторного уведомления не может быть отрицательным")
    private Integer noticeTerm;
    
    @Size(max = 10, message = "Код ОКВЭД не может быть длиннее 10 символов")
    private String okved;
    
    private Boolean chargeOffense;
    
    @NotNull(message = "Идентификатор получателя средств (payeeId) обязателен")
    @Positive(message = "Идентификатор получателя средств должен быть положительным")
    private Long payeeId;
    
    @NotNull(message = "Идентификатор плательщика (payerId) обязателен")
    @Positive(message = "Идентификатор плательщика должен быть положительным")
    private Long payerId;
    
    @NotNull(message = "Идентификатор бюджетных показателей (budgetIndexId) обязателен")
    @Positive(message = "Идентификатор бюджетных показателей должен быть положительным")
    private Long budgetIndexId;
    
    private Long executiveProcInfoId;
}

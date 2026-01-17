package ru.starslan.rnip_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.starslan.rnip_server.dto.ChargeDTO;
import ru.starslan.rnip_server.dto.PaymentDTO;
import ru.starslan.rnip_server.model.ChangeStatusMeaning;
import ru.starslan.rnip_server.repository.PaymentRepository;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Генерирует платеж для начисления
     * @param chargeDTO начисление, для которого создается платеж
     * @param chargeId ID сохраненного начисления в БД
     * @param payerIdentifier идентификатор плательщика
     * @param payerName имя плательщика
     * @return ID созданного платежа
     */
    public Long generatePaymentForCharge(ChargeDTO chargeDTO, Long chargeId, String payerIdentifier, String payerName) {
        Timestamp now = Timestamp.from(Instant.now());
        
        String paymentId = generatePaymentId();
        
        PaymentDTO paymentDTO = new PaymentDTO();
        
        paymentDTO.setPaymentId(paymentId);
        paymentDTO.setPaymentDate(now);
        paymentDTO.setAmount(chargeDTO.getTotalAmount());
        paymentDTO.setTransKind("01");
        paymentDTO.setSupplierBillId(chargeDTO.getSupplierBillId());
        paymentDTO.setChangeStatusMeaning(ChangeStatusMeaning.NEW);
        paymentDTO.setChangeStatusChangeDate(now);
        paymentDTO.setChargeId(chargeId);
        
        paymentDTO.setPurpose(chargeDTO.getPurpose());
        paymentDTO.setKbk(chargeDTO.getKbk());
        paymentDTO.setOktmo(chargeDTO.getOktmo());
        paymentDTO.setDeliveryDate(chargeDTO.getDeliveryDate());
        paymentDTO.setReceiptDate(chargeDTO.getDeliveryDate() != null ? 
            new Date(chargeDTO.getDeliveryDate().getTime()) : new Date(now.getTime()));
        
        paymentDTO.setPayerIdentifier(payerIdentifier);
        paymentDTO.setPayerName(payerName);
        
        paymentDTO.setPayeeId(chargeDTO.getPayeeId());
        paymentDTO.setPayerId(chargeDTO.getPayerId());
        paymentDTO.setBudgetIndexId(chargeDTO.getBudgetIndexId());
        
        return paymentRepository.insertPayment(paymentDTO);
    }

    /**
     * Генерирует уникальный идентификатор платежа (УПНО)
     * Формат: UUID без дефисов, обрезанный до 32 символов
     */
    private String generatePaymentId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 32);
    }
}

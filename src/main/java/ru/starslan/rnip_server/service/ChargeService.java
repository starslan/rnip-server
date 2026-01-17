package ru.starslan.rnip_server.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.starslan.rnip_server.dto.*;
import ru.starslan.rnip_server.model.ChargeOrigin;
import ru.starslan.rnip_server.model.PayerType;
import ru.starslan.rnip_server.repository.*;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

@Slf4j
@Service
public class ChargeService {

    private final ChargeRepository chargeRepository;
    private final PayeeRepository payeeRepository;
    private final PayerRepository payerRepository;
    private final BudgetIndexRepository budgetIndexRepository;
    private final ExecutiveProcedureInfoRepository executiveProcedureInfoRepository;
    private final PaymentService paymentService;
    private final Validator validator;

    @Autowired
    public ChargeService(
            ChargeRepository chargeRepository,
            PayeeRepository payeeRepository,
            PayerRepository payerRepository,
            BudgetIndexRepository budgetIndexRepository,
            ExecutiveProcedureInfoRepository executiveProcedureInfoRepository,
            PaymentService paymentService,
            Validator validator
    ) {
        this.chargeRepository = chargeRepository;
        this.payeeRepository = payeeRepository;
        this.payerRepository = payerRepository;
        this.budgetIndexRepository = budgetIndexRepository;
        this.executiveProcedureInfoRepository = executiveProcedureInfoRepository;
        this.paymentService = paymentService;
        this.validator = validator;
    }

    public void saveCharge(Object importedCharge) throws Exception {

        String supplierBillId = getValue(importedCharge, "getSupplierBillID");
        log.debug("Получен УИН начисления: {}", supplierBillId);
        
        if (supplierBillId != null && chargeRepository.existsBySupplierBillId(supplierBillId)) {
            String errorMessage = String.format("Начисление с УИН %s уже существует", supplierBillId);
            log.warn(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        XMLGregorianCalendar billDateXml = getValue(importedCharge, "getBillDate");
        XMLGregorianCalendar validUntilXml = getValue(importedCharge, "getValidUntil");
        BigInteger totalAmountBig = getValue(importedCharge, "getTotalAmount");
        String purpose = getValue(importedCharge, "getPurpose");
        String kbk = getValue(importedCharge, "getKbk");
        String oktmo = getValue(importedCharge, "getOktmo");
        XMLGregorianCalendar deliveryDateXml = getValue(importedCharge, "getDeliveryDate");
        String legalAct = getValue(importedCharge, "getLegalAct");
        XMLGregorianCalendar paymentTermXml = getValue(importedCharge, "getPaymentTerm");
        String originStr = getValue(importedCharge, "getOrigin");
        ChargeOrigin origin = null;
        if (originStr != null && !originStr.isEmpty()) {
            try {
                origin = ChargeOrigin.fromCode(originStr);
            } catch (IllegalArgumentException e) {
                log.warn("Неизвестное значение origin '{}', будет сохранено как null", originStr);
            }
        }
        BigInteger noticeTermBig = getValue(importedCharge, "getNoticeTerm");
        String okved = getValue(importedCharge, "getOKVED");
        Boolean chargeOffense = getValue(importedCharge, "getChargeOffense");

        Timestamp billDate = billDateXml != null ? Timestamp.from(billDateXml.toGregorianCalendar().toZonedDateTime().toInstant()) : null;
        Date validUntil = validUntilXml != null ? new Date(validUntilXml.toGregorianCalendar().getTimeInMillis()) : null;
        Date deliveryDate = deliveryDateXml != null ? new Date(deliveryDateXml.toGregorianCalendar().getTimeInMillis()) : null;
        Date paymentTerm = paymentTermXml != null ? new Date(paymentTermXml.toGregorianCalendar().getTimeInMillis()) : null;
        Long totalAmount = totalAmountBig != null ? totalAmountBig.longValue() : null;
        Integer noticeTerm = noticeTermBig != null ? noticeTermBig.intValue() : null;

        Object payee = getValue(importedCharge, "getPayee");
        Long payeeId = savePayee(payee);

        Object payer = getValue(importedCharge, "getPayer");
        Long payerId = savePayer(payer);
        
        String payerIdentifier = getValue(payer, "getPayerIdentifier");
        String payerName = getValue(payer, "getPayerName");

        Object budgetIndex = getValue(importedCharge, "getBudgetIndex");
        Long budgetIndexId = saveBudgetIndex(budgetIndex);

        Object execProcInfo = getValue(importedCharge, "getExecutiveProcedureInfo");
        Long execProcInfoId = execProcInfo != null ? saveExecutiveProcedureInfo(execProcInfo) : null;

        ChargeDTO chargeDTO = new ChargeDTO(
                supplierBillId,
                billDate,
                validUntil,
                totalAmount,
                purpose,
                kbk,
                oktmo,
                deliveryDate,
                legalAct,
                paymentTerm,
                origin,
                noticeTerm,
                okved,
                chargeOffense,
                payeeId,
                payerId,
                budgetIndexId,
                execProcInfoId
        );

        Set<ConstraintViolation<ChargeDTO>> violations = validator.validate(chargeDTO);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Ошибки валидации данных начисления с УИН ").append(supplierBillId).append(": ");
            for (ConstraintViolation<ChargeDTO> violation : violations) {
                errorMessage.append(violation.getPropertyPath()).append(" - ").append(violation.getMessage()).append("; ");
            }
            log.error(errorMessage.toString());
            throw new IllegalArgumentException(errorMessage.toString());
        }
        
        log.debug("Валидация начисления с УИН {} прошла успешно", supplierBillId);

        Long chargeId = chargeRepository.insertCharge(chargeDTO);
        
        try {
            Long paymentId = paymentService.generatePaymentForCharge(chargeDTO, chargeId, payerIdentifier, payerName);
            log.info("Платеж успешно сгенерирован для начисления с УИН {}, ID платежа: {}", supplierBillId, paymentId);
        } catch (Exception e) {
            log.error("Ошибка при генерации платежа для начисления с УИН {}: {}", supplierBillId, e.getMessage(), e);
        }
    }

    private Long savePayee(Object payee) throws Exception {
        if (payee == null) {
            throw new IllegalArgumentException("Payee не может быть null");
        }

        String name = getValue(payee, "getName");
        String inn = getValue(payee, "getInn");
        String kpp = getValue(payee, "getKpp");
        String ogrn = getValue(payee, "getOgrn");

        Object orgAccount = getValue(payee, "getOrgAccount");
        String account = null;
        String bankName = null;
        if (orgAccount != null) {
            account = getValue(orgAccount, "getAccountNumber");
            Object bank = getValue(orgAccount, "getBank");
            if (bank != null) {
                bankName = getValue(bank, "getName");
            }
        }

        PayeeDTO payeeDTO = new PayeeDTO(inn, kpp, name, ogrn, account, bankName);
        return payeeRepository.findOrCreate(payeeDTO);
    }

    private Long savePayer(Object payer) throws Exception {
        if (payer == null) {
            throw new IllegalArgumentException("Payer не может быть null");
        }

        String payerIdentifier = getValue(payer, "getPayerIdentifier");
        String payerName = getValue(payer, "getPayerName");
        String additionalPayerIdentifier = getValue(payer, "getAdditionalPayerIdentifier");
        
        PayerType payerType = determinePayerType(payerIdentifier);

        PayerDTO payerDTO = new PayerDTO(payerType, payerName, payerIdentifier, additionalPayerIdentifier);
        return payerRepository.findOrCreate(payerDTO);
    }

    private PayerType determinePayerType(String payerIdentifier) {
        if (payerIdentifier == null || payerIdentifier.isEmpty()) {
            return PayerType.INDIVIDUAL;
        }
        if (payerIdentifier.startsWith("1")) {
            return PayerType.INDIVIDUAL;
        } else if (payerIdentifier.startsWith("2")) {
            return PayerType.LEGAL_ENTITY;
        } else if (payerIdentifier.startsWith("3")) {
            return PayerType.ENTREPRENEUR;
        }
        return PayerType.INDIVIDUAL;
    }

    private Long saveBudgetIndex(Object budgetIndex) throws Exception {
        if (budgetIndex == null) {
            throw new IllegalArgumentException("BudgetIndex не может быть null");
        }

        String status = getValue(budgetIndex, "getStatus");
        String payReason = getValue(budgetIndex, "getPaytReason");
        String taxPeriod = getValue(budgetIndex, "getTaxPeriod");
        String docNumber = getValue(budgetIndex, "getDocNumber");
        XMLGregorianCalendar docDateXml = getValue(budgetIndex, "getDocDate");
        Date docDate = docDateXml != null ? new Date(docDateXml.toGregorianCalendar().getTimeInMillis()) : null;

        BudgetIndexDTO budgetIndexDTO = new BudgetIndexDTO(status, payReason, taxPeriod, docNumber, docDate);
        return budgetIndexRepository.findOrCreate(budgetIndexDTO);
    }

    private Long saveExecutiveProcedureInfo(Object execProcInfo) throws Exception {
        if (execProcInfo == null) {
            return null;
        }

        Object deedInfo = getValue(execProcInfo, "getDeedInfo");
        String execDocNumber = null;
        Date execDocDate = null;
        if (deedInfo != null) {
            execDocNumber = getValue(deedInfo, "getIdDocNo");
            XMLGregorianCalendar execDocDateXml = getValue(deedInfo, "getIdDocDate");
            execDocDate = execDocDateXml != null ? new Date(execDocDateXml.toGregorianCalendar().getTimeInMillis()) : null;
        }

        Object executeOrgan = getValue(execProcInfo, "getExecutOrgan");
        String bailiffDept = null;
        if (executeOrgan != null) {
            bailiffDept = getValue(executeOrgan, "getOrgan");
        }

        if (execDocNumber == null || execDocDate == null) {
            return null;
        }

        ExecutiveProcedureInfoDTO execProcInfoDTO = new ExecutiveProcedureInfoDTO(execDocNumber, execDocDate, bailiffDept);
        return executiveProcedureInfoRepository.findOrCreate(execProcInfoDTO);
    }

    @SuppressWarnings("unchecked")
    private <T> T getValue(Object obj, String methodName) {
        try {
            java.lang.reflect.Method method = obj.getClass().getMethod(methodName);
            Object result = method.invoke(obj);
            return (T) result;
        } catch (Exception e) {
            return null;
        }
    }
}

package ru.starslan.rnip_server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gov.smev.jaxb.*;
import ru.starslan.rnip_server.controller.XMLGregorianCalendarUtil;
import ru.starslan.rnip_server.exceptions.ContentNotFound;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class HandleMessagePrimaryContent implements HandleContent {
    
    @Autowired
    private ChargeService chargeService;

    @Override
    public SendRequestResponse handle(MessagePrimaryContent mpc) throws ContentNotFound {

        if (mpc.getImportChargesRequest() != null) {
            log.info("Получен запрос ImportChargesRequest");
            return handleImportCharges(mpc.getImportChargesRequest());
        }

        throw new ContentNotFound();
    }

    @SuppressWarnings("unchecked")
    private SendRequestResponse handleImportCharges(ImportChargesRequest importChargesRequest) {
        SendRequestResponse response = new SendRequestResponse();
        MessageMetadata metadata = new MessageMetadata();
        metadata.setMessageId(UUID.randomUUID().toString());
        metadata.setSendingTimestamp(XMLGregorianCalendarUtil.now());
        metadata.setStatus(InteractionStatusType.REQUEST_IS_QUEUED);
        response.setMessageMetadata(metadata);

        try {
            Object chargesPackage = getValue(importChargesRequest, "getChargesPackage");
            if (chargesPackage != null) {
                List<Object> importedCharges = getValue(chargesPackage, "getImportedCharge");
                if (importedCharges != null) {
                    log.info("Получено начислений для обработки: {}", importedCharges.size());
                    int successCount = 0;
                    int errorCount = 0;
                    
                    for (Object importedCharge : importedCharges) {
                        try {
                            String supplierBillId = getValue(importedCharge, "getSupplierBillID");
                            chargeService.saveCharge(importedCharge);
                            log.info("Начисление с УИН {} успешно сохранено", supplierBillId);
                            successCount++;
                        } catch (Exception e) {
                            String supplierBillId = getValue(importedCharge, "getSupplierBillID");
                            log.error("Ошибка при сохранении начисления с УИН {}: {}", supplierBillId, e.getMessage(), e);
                            errorCount++;
                        }
                    }
                    
                    log.info("Обработка начислений завершена. Успешно: {}, Ошибок: {}", successCount, errorCount);
                } else {
                    log.warn("В запросе ImportChargesRequest отсутствуют начисления для обработки");
                }
            }
        } catch (Exception e) {
            log.error("Ошибка при обработке ImportChargesRequest: {}", e.getMessage(), e);
        }

        return response;
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

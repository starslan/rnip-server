package ru.starslan.rnip_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gov.smev.jaxb.*;
import ru.starslan.rnip_server.controller.XMLGregorianCalendarUtil;
import ru.starslan.rnip_server.exceptions.ContentNotFound;

import java.util.List;
import java.util.UUID;

@Service
public class HandleMessagePrimaryContent implements HandleContent {
    
    @Autowired
    private ChargeService chargeService;

    @Override
    public SendRequestResponse handle(MessagePrimaryContent mpc) throws ContentNotFound {

        if (mpc.getImportChargesRequest() != null) {
            System.out.println("Пришел ImportChargesRequest");
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
                    for (Object importedCharge : importedCharges) {
                        try {
                            chargeService.saveCharge(importedCharge);
                            System.out.println("Charge saved success: " + getValue(importedCharge, "getSupplierBillID"));
                        } catch (Exception e) {
                            System.err.println("Charge saved error " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Charge error from ImportChargesRequest: " + e.getMessage());
            e.printStackTrace();
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

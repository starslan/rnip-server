package ru.starslan.rnip_server.service;

import org.springframework.stereotype.Service;
import ru.gov.smev.jaxb.*;
import ru.starslan.rnip_server.controller.XMLGregorianCalendarUtil;
import ru.starslan.rnip_server.exceptions.ContentNotFound;

import java.util.UUID;

@Service
public class HandleMessagePrimaryContent implements HandleContent {
    @Override
    public SendRequestResponse handle(MessagePrimaryContent mpc) throws ContentNotFound {
        if (mpc.getImportChargesRequest() != null) {
            System.out.println("Пришел ImportChargesRequest");
            return handleImportCharges(mpc.getImportChargesRequest());
        }

        throw new ContentNotFound();
    }

    private SendRequestResponse handleImportCharges(ImportChargesRequest importChargesRequest) {
        SendRequestResponse response = new SendRequestResponse();
        MessageMetadata metadata = new MessageMetadata();
        metadata.setMessageId(UUID.randomUUID().toString());
        metadata.setSendingTimestamp(XMLGregorianCalendarUtil.now());
        metadata.setStatus(InteractionStatusType.REQUEST_IS_QUEUED);
//
        response.setMessageMetadata(metadata);

        return response;
    }
}

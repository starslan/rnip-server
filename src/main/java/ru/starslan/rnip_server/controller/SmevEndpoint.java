package ru.starslan.rnip_server.controller;


import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.gov.smev.jaxb.InteractionStatusType;
import ru.gov.smev.jaxb.MessageMetadata;
import ru.gov.smev.jaxb.SendRequestRequest;
import ru.gov.smev.jaxb.SendRequestResponse;

import java.util.UUID;

@Endpoint
public class SmevEndpoint {
    private static final String NAMESPACE_URI = "urn://x-artefacts-smev-gov-ru/services/message-exchange/types/1.2";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendRequestRequest")
    @ResponsePayload
    public SendRequestResponse transferMsg(@RequestPayload SendRequestRequest request) {
        System.out.println("Получен запрос SendRequest");

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

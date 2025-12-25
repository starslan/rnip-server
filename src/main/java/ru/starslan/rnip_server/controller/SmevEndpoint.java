package ru.starslan.rnip_server.controller;


import jakarta.xml.bind.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.gov.smev.jaxb.*;
import ru.starslan.rnip_server.service.HandleContent;

import java.util.UUID;

@Endpoint
public class SmevEndpoint {
    private static final String NAMESPACE_URI = "urn://x-artefacts-smev-gov-ru/services/message-exchange/types/1.2";

    @Autowired
    HandleContent handleContentService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendRequestRequest")
    @ResponsePayload
    public SendRequestResponse transferMsg(@RequestPayload SendRequestRequest request) {

        SenderProvidedRequestData data = request.getSenderProvidedRequestData();

        MessagePrimaryContent mpc = data.getMessagePrimaryContent();

        SendRequestResponse response;
        response = handleContentService.handle(mpc);

        return response;
    }
}

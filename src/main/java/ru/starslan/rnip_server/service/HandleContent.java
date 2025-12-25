package ru.starslan.rnip_server.service;

import ru.gov.smev.jaxb.MessagePrimaryContent;
import ru.gov.smev.jaxb.SendRequestResponse;
import ru.starslan.rnip_server.exceptions.ContentNotFound;

public interface HandleContent {
    SendRequestResponse handle(MessagePrimaryContent mpc) throws ContentNotFound;
}

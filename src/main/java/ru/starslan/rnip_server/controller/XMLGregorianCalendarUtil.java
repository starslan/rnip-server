package ru.starslan.rnip_server.controller;

import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.GregorianCalendar;

@Component
public class XMLGregorianCalendarUtil {

    public static XMLGregorianCalendar now() {
        try {
            DatatypeFactory factory = DatatypeFactory.newInstance();
            GregorianCalendar calendar = new GregorianCalendar();
            return factory.newXMLGregorianCalendar(calendar);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException("Error creating XMLGregorianCalendar", e);
        }
    }
}

package ru.starslan.rnip_server.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;


@EnableWs
@Configuration
public class WebServiceConfig {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(
            ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/gisgmp/*");
    }

    @Bean(name = "smev")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema smevSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("SmevGISGMPService");
        wsdl11Definition.setLocationUri("/gisgmp");
        wsdl11Definition.setTargetNamespace("urn://x-artefacts-smev-gov-ru/services/message-exchange/1.2");
        wsdl11Definition.setSchema(smevSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema smevSchema() {
        return new SimpleXsdSchema(
                new ClassPathResource("wsdl/xsd/smev/smev-message-exchange-types-1.2.xsd")
        );
    }
}


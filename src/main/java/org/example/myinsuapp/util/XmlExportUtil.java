package org.example.myinsuapp.util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.example.myinsuapp.model.InformeMedico;

import java.io.File;

public class XmlExportUtil {

    public void exportarXmlInforme(InformeMedico informeMedico){
        try {
            JAXBContext context = JAXBContext.newInstance(InformeMedico.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(informeMedico, new File("docs/xml/informe.xml")); //todo luego trato de ponerle un nombre basado en la fecha del informe
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

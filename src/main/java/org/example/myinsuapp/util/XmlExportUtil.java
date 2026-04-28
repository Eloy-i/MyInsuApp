package org.example.myinsuapp.util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.example.myinsuapp.model.InformeMedico;
import org.example.myinsuapp.model.dto.InformeMedicoDTO;

import java.io.File;

public class XmlExportUtil {

    public void exportarXmlInforme(InformeMedicoDTO informeMedicoDTO){
        try {
            JAXBContext context = JAXBContext.newInstance(InformeMedicoDTO.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(informeMedicoDTO, new File("docs/xml/informe.xml")); //todo luego trato de ponerle un nombre basado en la fecha del informe
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

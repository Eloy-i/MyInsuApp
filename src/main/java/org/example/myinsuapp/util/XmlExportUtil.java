package org.example.myinsuapp.util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.example.myinsuapp.model.dto.InformeMedicoDTO;

import java.io.File;

public class XmlExportUtil {

    public void exportarXmlInforme(InformeMedicoDTO informeMedicoDTO){
        try {
            JAXBContext context = JAXBContext.newInstance(InformeMedicoDTO.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "esquema.xsd");

            int dias = informeMedicoDTO.getRangoDias();
            String fechaInforme = informeMedicoDTO.getFechaInforme();

            //Hago esto para no machacar y saber exactamente fecha y rango de días del informe. Si se generan dos iguales el mismo día, si se machacan.
            String nombreArchivo = "informe_"+fechaInforme+"_"+dias+"días.xml";

            marshaller.marshal(informeMedicoDTO, new File("docs/xml/"+nombreArchivo));
        } catch (JAXBException e) { //todo comenzar con personalizacion de excepciones.
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

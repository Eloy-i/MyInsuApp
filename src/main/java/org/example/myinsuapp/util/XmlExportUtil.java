package org.example.myinsuapp.util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.example.myinsuapp.exceptions.ExportException;
import org.example.myinsuapp.model.dto.InformeMedicoDTO;

import java.io.File;
import java.io.IOException;

public class XmlExportUtil {

    public void exportarXmlInforme(InformeMedicoDTO informeMedicoDTO){
        try {
            JAXBContext context = JAXBContext.newInstance(InformeMedicoDTO.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "esquema.xsd");

            int dias = informeMedicoDTO.getRangoDias();
            String fechaInforme = informeMedicoDTO.getFechaInforme();
            String nombre = informeMedicoDTO.getUsuarioDTO().getNombreCompleto().replace(" ", "");

            //Hago esto para no machacar y saber exactamente fecha y rango de días del informe. Si se generan dos iguales el mismo día, si se machacan. PD: y el nombre por si permito otro user
            String nombreArchivo = "informe_"+nombre+"_"+fechaInforme+"_"+dias+"días.xml";

            marshaller.marshal(informeMedicoDTO, new File("docs/xml/"+nombreArchivo));
        } catch (JAXBException e) {
            throw new ExportException("Error al escribir el archivo XML.", e);
        }
    }
}

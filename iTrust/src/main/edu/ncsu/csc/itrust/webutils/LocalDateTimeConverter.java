package edu.ncsu.csc.itrust.webutils;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter("localDateTimeConverter")
public class LocalDateTimeConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("M/d/yyyy h:mm a"));
        } catch (IllegalArgumentException | DateTimeException e) {
        	FacesMessage throwMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid date format", "Date format must be M/d/yyyy hh:mm AM/PM");
         	throw new ConverterException(throwMsg);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }

        if (!(value instanceof LocalDateTime)) {
            throw new ConverterException("Message");
        }

        return DateTimeFormatter.ofPattern("M/d/yyyy h:mm a").format((LocalDateTime) value);
    }
}
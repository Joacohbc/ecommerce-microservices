package com.microecommerce.utilitymodule.models;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ToTitleCase implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if(attribute == null) return null;
        if(attribute.isBlank()) return attribute;

        if(attribute.length() > 1) {
            var atr = attribute.trim();
            return atr.substring(0, 1).toUpperCase() + atr.substring(1);
        }
        return attribute.trim().toUpperCase();
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData;
    }
}

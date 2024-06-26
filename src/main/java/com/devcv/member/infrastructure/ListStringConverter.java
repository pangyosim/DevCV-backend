package com.devcv.member.infrastructure;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
******************************** Converter ********************************
* convertToDatabaseColumn : DB에 저장할 때 String값으로 바꿔주는 method
* convertToEntityAttribute : DB에서 꺼내올 때 해당 Attribute로 바꿔주는 method
******************************** Converter ********************************
* */
@Converter
public class ListStringConverter implements AttributeConverter<List<String>, String> {
    @Override
    public  String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null; // 또는 적절한 기본값 반환
        }
        return String.join(",", attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(dbData.split(","));
    }
}

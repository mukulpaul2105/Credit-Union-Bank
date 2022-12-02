package com.CUBank.creditunionbank.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MapperUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.findAndRegisterModules();
    }

    public static <T, F> T convertTo(final F from, final Class<T> toClass) {
        return MAPPER.convertValue(from, toClass);
    }
}

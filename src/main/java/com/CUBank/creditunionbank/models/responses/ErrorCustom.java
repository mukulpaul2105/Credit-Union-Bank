package com.CUBank.creditunionbank.models.responses;

import lombok.Data;

@Data
public class ErrorCustom {

    private String code;
    private String description;

    public static ErrorCustom create(final String code, final String desc) {
        ErrorCustom error = new ErrorCustom();
        error.setCode(code);
        error.setDescription(desc);
        return error;
    }
}

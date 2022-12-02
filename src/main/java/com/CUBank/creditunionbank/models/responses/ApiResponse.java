package com.CUBank.creditunionbank.models.responses;

import com.CUBank.creditunionbank.enums.ReqStatus;
import lombok.Data;

@Data
public class ApiResponse<T> {

    private ReqStatus status;
    private T data;
    private ErrorCustom error;

    public static <T> ApiResponse<T> success(final T data) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(ReqStatus.SUCCESS);
        apiResponse.setData(data);
        apiResponse.setError(null);
        return apiResponse;
    }

    public static <T> ApiResponse<T> error(final ErrorCustom err) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(ReqStatus.SUCCESS);
        apiResponse.setData(null);
        apiResponse.setError(err);
        return apiResponse;
    }
}

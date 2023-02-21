package com.example.blogservice.util;


import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
// 실제로 Client 에게 보내는 Format
public class ErrorResponse {
    private final String msg;
    private final int statuscode;


    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .msg(errorCode.getMsg())
                        .statuscode(errorCode.getHttpStatus().value())
                        .build());
    }
}

package com.devcv.common.exception;

import com.devcv.common.exception.dto.ErrorResponse;
import com.devcv.common.util.exception.S3Exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorResponse> handle(InternalServerException e) {
        System.out.println("dd");
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(TestErrorException.class)
    public ResponseEntity<ErrorResponse> handle(TestErrorException e) {
        System.out.println("dd");
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.from(ErrorCode.TEST_ERROR));
    }

    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<ErrorResponse>handle(S3Exception e) {
        System.out.println("s3 upload error");
        log.error(e.getMessage());

        ErrorCode errorCode = e.getErrorCode();
        HttpStatus status;

        switch (errorCode) {
            case EMPTY_FILE_EXCEPTION:
            case NO_FILE_EXTENTION:
            case INVALID_FILE_EXTENTION:
                status = HttpStatus.BAD_REQUEST;
                break;
            case IO_EXCEPTION_ON_IMAGE_UPLOAD:
            case PUT_OBJECT_EXCEPTION:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
            default:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(status)
                .body(ErrorResponse.from(errorCode));
    }
}

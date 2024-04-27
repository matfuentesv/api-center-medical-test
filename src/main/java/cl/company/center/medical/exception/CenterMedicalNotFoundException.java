package cl.company.center.medical.exception;

import org.springframework.http.HttpStatus;

public class CenterMedicalNotFoundException extends RuntimeException{

    private HttpStatus status;
    private ErrorResponse errorResponse;

    public CenterMedicalNotFoundException(ErrorResponse errorResponse, HttpStatus status) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}

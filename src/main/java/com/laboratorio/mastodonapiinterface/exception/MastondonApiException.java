package com.laboratorio.mastodonapiinterface.exception;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 10/07/2024
 * @updated 05/06/2025
 */
public class MastondonApiException extends RuntimeException {
    private Throwable causaOriginal = null;
    
    public MastondonApiException(String message) {
        super(message);
    }
    
    public MastondonApiException(String message, Throwable causaOriginal) {
        super(message);
        this.causaOriginal = causaOriginal;
    }
    
    @Override
    public String getMessage() {
        if (this.causaOriginal != null) {
            return super.getMessage() + " | Causa original: " + this.causaOriginal.getMessage();
        }
        
        return super.getMessage();
    }
}
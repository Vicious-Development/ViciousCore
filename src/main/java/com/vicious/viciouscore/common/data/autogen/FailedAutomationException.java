package com.vicious.viciouscore.common.data.autogen;

public class FailedAutomationException extends RuntimeException{
    public FailedAutomationException() {
    }

    public FailedAutomationException(String message) {
        super(message);
    }

    public FailedAutomationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedAutomationException(Throwable cause) {
        super(cause);
    }

    public FailedAutomationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

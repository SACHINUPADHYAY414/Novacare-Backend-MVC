package com.healthcare.exception;

public class CustomExceptions {

    // ⛔ 409 - Time Slot Already Booked
    public static class TimeSlotAlreadyBookedException extends RuntimeException {
        public TimeSlotAlreadyBookedException(String message) {
            super(message);
        }
    }

    // 🔍 404 - Resource Not Found
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    // 🔐 403 - Access Denied
    public static class AccessDeniedException extends RuntimeException {
        public AccessDeniedException(String message) {
            super(message);
        }
    }

    // 🚫 401 - Unauthorized
    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }

    // ⚠️ 400 - Bad Request
    public static class BadRequestException extends RuntimeException {
        public BadRequestException(String message) {
            super(message);
        }
    }
}

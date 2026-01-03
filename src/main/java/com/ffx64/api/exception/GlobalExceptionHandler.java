package com.ffx64.api.exception;

import java.time.LocalDateTime;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ffx64.api.dto.ExceptionResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionResponseDTO> handlerException(HttpRequestMethodNotSupportedException ex) {
        return build(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage());
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<ExceptionResponseDTO> handlerPlayersExistsException(UserExistsException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handlerPlayersNotFoundException(UserNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ClanExistsException.class)
    public ResponseEntity<ExceptionResponseDTO> handlerClansExistsException(ClanExistsException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ClanMembersLimitException.class)
    public ResponseEntity<ExceptionResponseDTO> handlerClanMembersLimitException(ClanMembersLimitException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ClanMembersNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handlerClanMembersNotFoundException(ClanMembersNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(TermsNotAccepted.class)
    public ResponseEntity<ExceptionResponseDTO> handlerTermsNotAcceptedException(TermsNotAccepted ex) {
        return build(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(PlayerVerificationException.class)
    public ResponseEntity<ExceptionResponseDTO> handlerPlayerVerificationException(PlayerVerificationException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ExceptionResponseDTO> handlerInternalServerException(InternalServerErrorException ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<ExceptionResponseDTO> handlerInvalidDataAccessApiUsageException(
            InvalidDataAccessApiUsageException ex) {
        return build(HttpStatus.BAD_REQUEST,
                "Ocorreu um erro no campo de entrada dos dados, por favor verifique ou tente novamente mais tarde.");
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<ExceptionResponseDTO> handleGenericException(GenericException ex) {
        return build(ex.getStatus(), ex.getMessage());
    }

    /*
     * API RESPONSE HANDLER ERRORS
     */

    @ExceptionHandler(ServerExistsException.class)
    public ResponseEntity<ExceptionResponseDTO> handlerServersExistsException(ServerExistsException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ServerNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handlerServerNotFoundException(ServerNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(RoundNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handlerRoundNotFoundException(RoundNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handlerPlayerNotFoundException(PlayerNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    private ResponseEntity<ExceptionResponseDTO> build(HttpStatus status, String message) {
        var response = new ExceptionResponseDTO(
                status.getReasonPhrase(),
                message,
                status.value(),
                LocalDateTime.now());

        return new ResponseEntity<>(response, status);
    }
}

package ru.mephi.giftparser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
//    @ExceptionHandler({UserNotFoundException.class,
//                       CategoryNotFoundException.class,
//                       EventNotFoundException.class,
//                       RequestNotFoundException.class,
//                       CompilationNotFoundException.class,
//                       CommentNotFoundException.class})
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ErrorResponse handle404Exception(final Exception e) {
//        log.error("{} {}", HttpStatus.NOT_FOUND, e.getMessage());
//        return ErrorResponse.builder()
//                .status(HttpStatus.NOT_FOUND.name())
//                .reason("The required object was not found.")
//                .message(e.getMessage())
//                .timestamp(LocalDateTime.now().format(formatter))
//                .build();
//    }
//
//    @ExceptionHandler({EventCancelException.class,
//                       EventValidationException.class,
//                       RequestValidationException.class,
//                       CommentValidationException.class})
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public ErrorResponse handle403Exception(final Exception e) {
//        log.error("{} {}", HttpStatus.FORBIDDEN, e.getMessage());
//        return ErrorResponse.builder()
//                .status(HttpStatus.FORBIDDEN.name())
//                .reason("For the requested operation the conditions are not met.")
//                .message(e.getMessage())
//                .timestamp(LocalDateTime.now().format(formatter))
//                .build();
//    }
//
//    @ExceptionHandler({MissingServletRequestParameterException.class,
//                       MethodArgumentNotValidException.class,
//                       org.hibernate.exception.ConstraintViolationException.class,
//                       javax.validation.ConstraintViolationException.class,
//                       DataIntegrityViolationException.class})
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorResponse handle400Exception(final Exception e) {
//        log.error("{} {}", HttpStatus.BAD_REQUEST, e.getMessage());
//        return ErrorResponse.builder()
//                .status(HttpStatus.BAD_REQUEST.name())
//                .reason("For the requested operation the conditions are not met.")
//                .message(e.getMessage())
//                .timestamp(LocalDateTime.now().format(formatter))
//                .build();
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorResponse handle500Exception(final Exception e) {
//        log.error("{} {}", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
//        return ErrorResponse.builder()
//                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
//                .reason("Error occurred")
//                .message(e.getMessage())
//                .timestamp(LocalDateTime.now().format(formatter))
//                .build();
//    }
}

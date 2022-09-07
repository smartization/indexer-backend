package cloud.ptl.indexer.api.exceptions;

import cloud.ptl.indexer.api.profile.ProfileService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.stream.Collectors;


@RestController
@EnableWebMvc
@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ExceptionController {
    private final ProfileService profileService;

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handler(HttpServletRequest request, BindException ex) {
        final String errorMessage =
                ex.getBindingResult().getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.joining(", "));

        Runnable exceptionLog = ex::printStackTrace;
        profileService.ifDevProfileRun(exceptionLog);

        return ErrorResponse.builder()
                .timestamp(new Date().getTime())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(errorMessage)
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handler(HttpServletRequest request, NumberFormatException ex) {
        return ErrorResponse.builder()
                .timestamp(new Date().getTime())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Number inconvertible " + ex.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    @Data
    @Builder
    public static class ErrorResponse {
        private Long timestamp;
        private long status;
        private String error;
        private String message;
        private String path;
    }
}

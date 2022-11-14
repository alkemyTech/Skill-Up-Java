package com.alkemy.wallet.model.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ApiErrorResponse {
    private HttpStatus status;
    private LocalDateTime timestamp;
    private List<String> errors;
}

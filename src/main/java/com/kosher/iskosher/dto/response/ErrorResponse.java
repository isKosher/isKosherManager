package com.kosher.iskosher.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    private String message;
    private String error;
    private String path;
    private LocalDateTime timestamp;

}

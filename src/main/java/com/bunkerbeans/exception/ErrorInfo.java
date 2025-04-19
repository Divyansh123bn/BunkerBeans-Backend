package com.bunkerbeans.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorInfo {
    private String errorMessage;
    private int errorCode;
    private LocalDateTime timeStamp;
}

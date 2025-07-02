package com.cotech.systemcoreapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenRequest {
    Long id;
    String accessToken;
    private String refreshToken;
}
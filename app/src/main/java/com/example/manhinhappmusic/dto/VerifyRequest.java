package com.example.manhinhappmusic.dto;

import lombok.Data;

@Data
public class VerifyRequest {
    private String email;
    private String otp;

    public VerifyRequest(String email, String otp){
        this.email = email;
        this.otp = otp;
    }
}

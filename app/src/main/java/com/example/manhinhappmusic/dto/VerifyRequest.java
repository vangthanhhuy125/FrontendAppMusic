package com.example.manhinhappmusic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyRequest {
 private String email;
 private String otp;
}

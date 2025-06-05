package com.example.manhinhappmusic.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Otp {
 private String otp;
 private LocalDateTime expiresAt;
 public Otp(String otp, LocalDateTime ex) {
  this.otp = otp;
  expiresAt = ex;
 }
}

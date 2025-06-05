package com.example.manhinhappmusic.model;

import lombok.Data;
import java.time.Instant;

@Data
public class PasswordResetToken {
 private String id;
 private String token;
 private String email;
 private Instant expiresAt;
 private Instant createdAt;

 public PasswordResetToken(String id, String token, String email) {
     this.id = id;
     this.token = token;
     this.email = email;
     this.expiresAt = Instant.now().plusSeconds(86400);
     this.createdAt = Instant.now();
 }
}

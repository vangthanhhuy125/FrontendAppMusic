package com.example.manhinhappmusic.dto;

import lombok.Data;

@Data
public class ChangePasswordRequest {
 private String oldPassword;
 private String newPassword;
}

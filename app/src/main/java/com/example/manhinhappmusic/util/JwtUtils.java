package com.example.manhinhappmusic.util;

import android.util.Base64;

import org.json.JSONObject;

public class JwtUtils {
    public static String getEmailFromToken(String token) {
        try {
            // Cắt phần payload (phần thứ 2 sau dấu .)
            String[] parts = token.split("\\.");
            if (parts.length < 2) return null;

            String payload = parts[1];

            // Base64 decode
            byte[] decodedBytes = Base64.decode(payload, Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);
            String decodedPayload = new String(decodedBytes, "UTF-8");

            // Chuyển sang JSON và lấy email
            JSONObject jsonObject = new JSONObject(decodedPayload);
            return jsonObject.getString("sub"); // hoặc "email" tùy backend

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

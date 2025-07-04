package com.example.manhinhappmusic.repository;

import java.util.HashMap;
import java.util.Map;

public class GlobalVars {
    private static Map<String, String> vars = new HashMap<>();

    public static Map<String, String> getVars() {
        return vars;
    }
}

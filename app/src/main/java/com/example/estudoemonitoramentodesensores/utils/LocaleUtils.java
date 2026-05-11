package com.example.estudoemonitoramentodesensores.utils;

import java.util.Locale;

public class LocaleUtils {

    public static String getAppLanguage() {
        String lang = Locale.getDefault().getLanguage();

        switch (lang) {
            case "de": return "de";
            case "fr": return "fr";
            case "pt": return "pt";
            case "zh": return "zh-CN";
            default: return "en";
        }
    }
}
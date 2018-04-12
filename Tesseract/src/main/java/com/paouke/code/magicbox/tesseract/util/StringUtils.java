package com.paouke.code.magicbox.tesseract.util;

public class StringUtils {

    public static String cleanToNumberOrLetterString(String src) {
        StringBuilder sb = new StringBuilder();
        for(char c : src.toCharArray()) {
            if((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}

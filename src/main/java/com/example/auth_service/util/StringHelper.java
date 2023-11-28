package com.example.auth_service.util;

import java.util.Objects;

public class StringHelper {

    private StringHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static String toSlug(String input) {
        return input.toLowerCase()
                .replaceAll("[áàảạãăắằẳẵặâấầẩẫậäåæą]", "a")
                .replaceAll("[óòỏõọôốồổỗộơớờởỡợöœø]", "o")
                .replaceAll("[éèẻẽẹêếềểễệěėëę]", "e")
                .replaceAll("[úùủũụưứừửữự]", "u")
                .replaceAll("[iíìỉĩịïîį]", "i")
                .replaceAll("[ùúüûǘůűūų]", "u")
                .replaceAll("[ßşśšș]", "s")
                .replaceAll("[źžż]", "z")
                .replaceAll("[ýỳỷỹỵÿ]", "y")
                .replaceAll("[ǹńňñ]", "n")
                .replaceAll("[çćč]", "c")
                .replaceAll("[ğǵ]", "g")
                .replaceAll("[ŕř]", "r")
                .replaceAll("[·/_,:;]", "-")
                .replaceAll("[ťț]", "t")
                // "String#replace" should be preferred to "String#replaceAll" (java:S5361)
                .replace("ḧ", "h")
                .replace("ẍ", "x")
                .replace("ẃ", "w")
                .replace("ḿ", "m")
                .replace("ṕ", "p")
                .replace("ł", "l")
                .replace("đ", "d")
                .replace("\\s+", "-")
                .replace("&", "-and-")
                .replaceAll("[^\\w\\-]+", "")
                .replaceAll("--+", "-")
                .replaceAll("^-+", "")
                .replaceAll("-+$", "");
    }

    public static String getCleanFileName(String fileName) {
        Objects.requireNonNull(fileName);
        int indexOfDash = fileName.indexOf("-");
        return (indexOfDash != -1 && indexOfDash < fileName.length() - 1) ? fileName.substring(indexOfDash + 1) : fileName;
    }
}

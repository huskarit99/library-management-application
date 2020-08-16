package com.example.librarymanagement.activities;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class NormalizeString {
    String normalizeString;

    public NormalizeString(String s){
        s = s.replace(" ", "_");
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replace("[^\\p{ASCII}]", "");
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        normalizeString = pattern.matcher(s.toLowerCase()).replaceAll("");
    }

    public void setNormalizeString(String normalizeString) {
        this.normalizeString = normalizeString;
    }

    public String getNormalizeString() {
        return normalizeString;
    }
}

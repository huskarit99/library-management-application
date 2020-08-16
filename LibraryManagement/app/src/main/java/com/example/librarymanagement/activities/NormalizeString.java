package com.example.librarymanagement.activities;

import java.text.Normalizer;

public class NormalizeString {
    String normalizeString;

    public NormalizeString(String s){
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replace("[^\\p{ASCII}]", "");
        normalizeString = s.toLowerCase();
    }

    public void setNormalizeString(String normalizeString) {
        this.normalizeString = normalizeString;
    }

    public String getNormalizeString() {
        return normalizeString;
    }
}

package com.example.eureka.supplier;

import java.util.List;

public enum BlockedSupplierKeyword {

    OSIGURANJE(List.of("osiguranje", "insurance")),
    LEASING(List.of("leasing")),
    TELEKOM(List.of("telekom", "telecommunications","telemach")),

    BANK(List.of("bank")), // pokriva bank, banka, banking

    ELEKTRA(List.of("elektra", "hep")),
    PLIN(List.of("plin")),
    VODOOPSKRBA(List.of("vodoopskrba")),

    HRT(List.of("hrt")),
    TEHNICKI(List.of("tehnički", "tehnicki")),
    AUTO(List.of("auto", "vozilo", "vozila")),
    GOSPODARSTVO(List.of("gospodarstvo"));

    private final List<String> keywords;

    BlockedSupplierKeyword(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public static boolean matches(String name) {
        String lower = name.toLowerCase();

        for (BlockedSupplierKeyword keyword : values()) {
            for (String k : keyword.keywords) {
                if (lower.contains(k)) {
                    return true;
                }
            }
        }
        return false;
    }
}

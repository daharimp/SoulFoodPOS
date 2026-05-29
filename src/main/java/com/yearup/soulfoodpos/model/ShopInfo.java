package com.yearup.soulfoodpos.model;

public record ShopInfo(
        String name,
        String tagline,
        String addressLine1,
        String cityStateZip,
        String phone,
        String hours
) {
}

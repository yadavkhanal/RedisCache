package com.yadavkhanal.rediscache.dto;

public record MovieResponse(
        String title,
        Integer year,
        String imdbId
) {
}
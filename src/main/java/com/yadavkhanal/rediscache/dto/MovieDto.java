package com.yadavkhanal.rediscache.dto;

public record MovieDto(
        String Title,
        Integer Year,
        String imdbID
) {
}
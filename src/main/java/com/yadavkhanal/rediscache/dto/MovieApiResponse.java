package com.yadavkhanal.rediscache.dto;

import java.util.List;

public record MovieApiResponse(
        int page,
        int per_page,
        int total,
        int total_pages,
        List<MovieDto> data
) {
}
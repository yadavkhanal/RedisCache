package com.yadavkhanal.rediscache.service;

import com.yadavkhanal.rediscache.dto.MovieResponse;
import com.yadavkhanal.rediscache.dto.PageResponse;

public interface MovieService {

    PageResponse<MovieResponse> searchMovies(
            String title,
            int page,
            int size,
            String sortField,
            String sortDirection);
}
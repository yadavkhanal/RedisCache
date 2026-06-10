package com.yadavkhanal.rediscache.controller;

import com.yadavkhanal.rediscache.dto.MovieResponse;
import com.yadavkhanal.rediscache.dto.PageResponse;
import com.yadavkhanal.rediscache.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<PageResponse<MovieResponse>> searchMovies(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {

        /*1. Bad request (invalid params)*/
/*        if (title == null || title.isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body("Title must not be empty");
        }*/

        PageResponse<MovieResponse> response =
                movieService.searchMovies(title, page, size, sort, direction);

        /*2. No data found*/
        if (response.content().isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }


        return ResponseEntity
                .ok()
                .header("X-Total-Elements", String.valueOf(response.totalElements()))
                .header("X-Total-Pages", String.valueOf(response.totalPages()))
                .body(response);
    }
}
package com.yadavkhanal.rediscache.service;

import com.yadavkhanal.rediscache.dto.MovieApiResponse;
import com.yadavkhanal.rediscache.dto.MovieDto;
import com.yadavkhanal.rediscache.dto.MovieResponse;
import com.yadavkhanal.rediscache.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieServiceImpl implements MovieService {

    private final RestClient restClient;

    private final Executor virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();

    private final Semaphore semaphore = new Semaphore(10);

    public PageResponse<MovieResponse> searchMovies(String title, int page, int size, String sortField, String sortDirection) {

        List<MovieResponse> allMovies = fetchAllMoviesParallel(title);

        Comparator<MovieResponse> comparator = buildComparator(sortField);

        if ("desc".equalsIgnoreCase(sortDirection)) {
            comparator = comparator.reversed();
        }

        List<MovieResponse> sorted = allMovies.stream().sorted(comparator).toList();

        int start = page * size;
        int end = Math.min(start + size, sorted.size());

        List<MovieResponse> paged = start >= sorted.size() ? Collections.emptyList() : sorted.subList(start, end);

        return new PageResponse<>(paged, sorted.size(), (int) Math.ceil((double) sorted.size() / size), page, size);
    }


    private  List<MovieResponse> fetchAllMovies(String title) {
//        CompleteableFuture

        MovieApiResponse firstPage = restClient.get().uri(uriBuilder -> uriBuilder.path("/movies/search").queryParam("title", title).queryParam("page", 1).build()).retrieve().body(MovieApiResponse.class);

        List<MovieResponse> movies = new ArrayList<>(map(firstPage.data()));

        for (int page = 2; page <= firstPage.total_pages(); page++) {
            final int currentPage = page;

            MovieApiResponse response = restClient.get().uri(uriBuilder -> uriBuilder.path("/movies/search").queryParam("title", title)
                    .queryParam("page", currentPage).build()).retrieve().body(MovieApiResponse.class);

            movies.addAll(map(response.data()));
        }

        return movies;
    }

    private List<MovieResponse> fetchAllMoviesParallel(String title) {

        MovieApiResponse firstPage = fetchPage(title, 1);

        List<MovieResponse> movies =
                new ArrayList<>(map(firstPage.data()));

        int totalPages = firstPage.total_pages();

        List<CompletableFuture<List<MovieResponse>>> futures =
                new ArrayList<>();

        for (int page = 2; page <= totalPages; page++) {

            final int currentPage = page;

            CompletableFuture<List<MovieResponse>> future =
                    CompletableFuture.supplyAsync(
                                    () -> {
                                        try {
                                            semaphore.acquire();
                                            return fetchPage(title, currentPage);
                                        } catch (InterruptedException e) {
                                            Thread.currentThread().interrupt();
                                            throw new RuntimeException(e);
                                        } finally {
                                            semaphore.release();
                                        }
                                    }, virtualThreadExecutor).orTimeout(2, TimeUnit.SECONDS)
                            .exceptionally(ex -> {
                                log.error("Failed page {}", currentPage, ex);
                                return new MovieApiResponse(0, 0, 0, 0, List.of());
                            })
                            .thenApply(response -> map(response.data()));
            futures.add(future);
        }

        List<MovieResponse> results =
                futures.stream()
                        .map(CompletableFuture::join)
                        .flatMap(List::stream)
                        .toList();

        movies.addAll(results);

        return movies;
    }

    private MovieApiResponse fetchPage(String title, int page) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movies/search")
                        .queryParam("title", title)
                        .queryParam("page", page)
                        .build())
                .retrieve()
                .body(MovieApiResponse.class);
    }

    private List<MovieResponse> map(List<MovieDto> data) {
        return data.stream().map(movie -> new MovieResponse(movie.Title(), movie.Year(), movie.imdbID())).toList();
    }

    private Comparator<MovieResponse> buildComparator(String field) {

        return switch (field.toLowerCase()) {
            case "year" -> Comparator.comparing(MovieResponse::year);

            case "title" ->  Comparator.comparing(
                    MovieResponse::title,
                   String.CASE_INSENSITIVE_ORDER);

            default -> Comparator.comparing(MovieResponse::title, String.CASE_INSENSITIVE_ORDER);
        };
    }
}
package com.angular.tekflix_server.services;


import com.angular.tekflix_server.models.Movie;

import java.util.List;
import java.util.Optional;

public interface IMovieService {

    public List<Movie> getAllMovies();

    public Optional<Movie> getMovieById(Long id);

    public Movie createMovie(Movie movie);

    public Movie updateMovie(Movie movie);

    public void deleteMovie(Long id);
}

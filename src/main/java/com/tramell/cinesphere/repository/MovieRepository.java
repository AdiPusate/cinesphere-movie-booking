package com.tramell.cinesphere.repository;

import com.tramell.cinesphere.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByTitleContainingIgnoreCase(String title);
    List<Movie> findByStatus(com.tramell.cinesphere.enums.MovieStatus status);
    List<Movie> findByGenre(String genre);
    List<Movie> findByLanguage(String language);
}

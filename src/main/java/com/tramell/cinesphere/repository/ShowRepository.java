package com.tramell.cinesphere.repository;

import com.tramell.cinesphere.entity.Movie;
import com.tramell.cinesphere.entity.Show;
import com.tramell.cinesphere.entity.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByMovie(Movie movie);
    List<Show> findByTheatre(Theatre theatre);
    long countByShowTimeAfter(LocalDateTime now);
}

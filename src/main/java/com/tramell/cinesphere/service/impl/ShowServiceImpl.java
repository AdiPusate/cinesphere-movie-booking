package com.tramell.cinesphere.service.impl;

import com.tramell.cinesphere.dto.request.ShowRequest;
import com.tramell.cinesphere.dto.response.ShowResponse;
import com.tramell.cinesphere.entity.Movie;
import com.tramell.cinesphere.entity.Show;
import com.tramell.cinesphere.entity.Theatre;
import com.tramell.cinesphere.exception.ResourceNotFoundException;
import com.tramell.cinesphere.mapper.ShowMapper;
import com.tramell.cinesphere.repository.MovieRepository;
import com.tramell.cinesphere.repository.ShowRepository;
import com.tramell.cinesphere.repository.TheatreRepository;
import com.tramell.cinesphere.service.ShowService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final TheatreRepository theatreRepository;
    private final ShowMapper showMapper;

    @Override
    @Transactional
    public ShowResponse addShow(ShowRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", request.getMovieId()));
                
        Theatre theatre = theatreRepository.findByIdWithLock(request.getTheatreId())
                .orElseThrow(() -> new ResourceNotFoundException("Theatre", "id", request.getTheatreId()));

        LocalDateTime requestedStart = request.getShowTime();
        LocalDateTime requestedEnd = requestedStart.plusMinutes(movie.getDuration());

        boolean overlaps = showRepository.findByTheatre(theatre).stream()
                .anyMatch(existing -> {
                    LocalDateTime existingStart = existing.getShowTime();
                    LocalDateTime existingEnd = existingStart.plusMinutes(existing.getMovie().getDuration());
                    return existingStart.isBefore(requestedEnd)
                            && requestedStart.isBefore(existingEnd);
                });

        if (overlaps) {
            throw new com.tramell.cinesphere.exception.BadRequestException(
                    "The requested show overlaps an existing show in this theatre");
        }

        Show show = Show.builder()
                .movie(movie)
                .theatre(theatre)
                .showTime(request.getShowTime())
                .basePrice(request.getBasePrice())
                .build();
                
        Show savedShow = showRepository.save(show);
        return showMapper.toResponse(savedShow);
    }

    @Override
    public ShowResponse getShowById(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show", "id", id));
        return showMapper.toResponse(show);
    }

    @Override
    public List<ShowResponse> getShowsByMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", movieId));
        return showRepository.findByMovie(movie).stream()
                .map(showMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShowResponse> getShowsByTheatre(Long theatreId) {
        Theatre theatre = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre", "id", theatreId));
        return showRepository.findByTheatre(theatre).stream()
                .map(showMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ShowServiceImpl(ShowRepository showRepository, MovieRepository movieRepository, TheatreRepository theatreRepository, ShowMapper showMapper) {
        this.showRepository = showRepository;
        this.movieRepository = movieRepository;
        this.theatreRepository = theatreRepository;
        this.showMapper = showMapper;
    }
}

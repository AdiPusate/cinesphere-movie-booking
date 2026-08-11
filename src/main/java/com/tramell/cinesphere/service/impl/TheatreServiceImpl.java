package com.tramell.cinesphere.service.impl;

import com.tramell.cinesphere.dto.request.TheatreRequest;
import com.tramell.cinesphere.dto.response.TheatreResponse;
import com.tramell.cinesphere.entity.Theatre;
import com.tramell.cinesphere.exception.ResourceNotFoundException;
import com.tramell.cinesphere.mapper.TheatreMapper;
import com.tramell.cinesphere.repository.TheatreRepository;
import com.tramell.cinesphere.service.TheatreService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TheatreServiceImpl implements TheatreService {

    private final TheatreRepository theatreRepository;
    private final TheatreMapper theatreMapper;

    @Override
    @Transactional
    public TheatreResponse addTheatre(TheatreRequest request) {
        Theatre theatre = Theatre.builder()
                .name(request.getName())
                .city(request.getCity())
                .address(request.getAddress())
                .build();
        Theatre savedTheatre = theatreRepository.save(theatre);
        return theatreMapper.toResponse(savedTheatre);
    }

    @Override
    public List<TheatreResponse> getAllTheatres() {
        return theatreRepository.findAll().stream()
                .map(theatreMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TheatreResponse getTheatreById(Long id) {
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre", "id", id));
        return theatreMapper.toResponse(theatre);
    }

    @Override
    @Transactional
    public TheatreResponse updateTheatre(Long id, TheatreRequest request) {
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre", "id", id));
        theatre.setName(request.getName());
        theatre.setCity(request.getCity());
        theatre.setAddress(request.getAddress());
        Theatre updatedTheatre = theatreRepository.save(theatre);
        return theatreMapper.toResponse(updatedTheatre);
    }

    @Override
    @Transactional
    public void deleteTheatre(Long id) {
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre", "id", id));
        theatreRepository.delete(theatre);
    }

    public TheatreServiceImpl(TheatreRepository theatreRepository, TheatreMapper theatreMapper) {
        this.theatreRepository = theatreRepository;
        this.theatreMapper = theatreMapper;
    }
}

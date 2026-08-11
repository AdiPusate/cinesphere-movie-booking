package com.tramell.cinesphere.service.impl;

import com.tramell.cinesphere.repository.BookingRepository;
import com.tramell.cinesphere.repository.MovieRepository;
import com.tramell.cinesphere.repository.ShowRepository;
import com.tramell.cinesphere.service.AdminService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tramell.cinesphere.enums.BookingStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {

    private final MovieRepository movieRepository;
    private final ShowRepository showRepository;
    private final BookingRepository bookingRepository;

    @Override
    public Map<String, Object> getDashboardStats() {
        long totalMovies = movieRepository.count();
        long activeShows = showRepository.countByShowTimeAfter(LocalDateTime.now());
        long totalBookings = bookingRepository.countByBookingStatus(BookingStatus.CONFIRMED);
        BigDecimal revenue = bookingRepository.getTotalRevenueByStatus(BookingStatus.CONFIRMED);
        
        if (revenue == null) {
            revenue = BigDecimal.ZERO;
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalMovies", totalMovies);
        stats.put("activeShows", activeShows);
        stats.put("totalBookings", totalBookings);
        stats.put("revenue", revenue);
        
        return stats;
    }

    public AdminServiceImpl(MovieRepository movieRepository, ShowRepository showRepository, BookingRepository bookingRepository) {
        this.movieRepository = movieRepository;
        this.showRepository = showRepository;
        this.bookingRepository = bookingRepository;
    }
}

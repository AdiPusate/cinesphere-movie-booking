package com.tramell.cinesphere.repository;

import com.tramell.cinesphere.entity.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Theatre t WHERE t.theatreId = :id")
    Optional<Theatre> findByIdWithLock(@Param("id") Long id);
}

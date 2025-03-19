package org.example.hotappresi.repositories;

import org.example.hotappresi.models.Reservation;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ReservationRepository {
    private List<Reservation> reservations = new ArrayList<>();
    private Long currentId = 1L;


    public List<Reservation> getAllReservations() {
        return reservations;
    }

    public void addReservation(Reservation reservation) {
        reservation.setId(currentId++);
        reservations.add(reservation);
    }
    public Reservation getReservationById(Long id) {
        return reservations.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void removeReservation(Long id) {
        reservations.removeIf(r -> r.getId().equals(id));
    }
    public List<Reservation> findByUsername(String username) {
        return reservations.stream()
                .filter(r -> r.getUsername() != null && r.getUsername().equals(username))
                .collect(Collectors.toList());
    }
}
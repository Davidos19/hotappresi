package org.example.hotappresi.services;


import org.example.hotappresi.models.Hotel;
import org.example.hotappresi.models.Reservation;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final ReservationService reservationService;
    private final HotelService hotelService;

    public DashboardService(ReservationService reservationService, HotelService hotelService) {
        this.reservationService = reservationService;
        this.hotelService = hotelService;
    }

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        List<Reservation> reservations = reservationService.getAllReservationsForAdmin();
        List<Hotel> hotels = hotelService.getAllHotels();
        stats.put("totalReservations", reservations.size());
        stats.put("totalHotels", hotels.size());
        int totalAvailableRooms = hotels.stream().mapToInt(Hotel::getAvailableRooms).sum();
        stats.put("totalAvailableRooms", totalAvailableRooms);
        return stats;
    }
}
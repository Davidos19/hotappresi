package org.example.hotappresi.services;

import org.example.hotappresi.models.Hotel;
import org.example.hotappresi.models.Reservation;
import org.example.hotappresi.repositories.HotelRepository;
import org.example.hotappresi.repositories.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    // Możesz usunąć lokalną listę, jeśli korzystasz z repository:
    // private List<Reservation> reservations = new ArrayList<>();

    private final HotelRepository hotelRepository;
    private final ReservationRepository reservationRepository;
    private final UserService userService;

    // Konstruktor bez wstrzykiwania samego siebie
    public ReservationService(HotelRepository hotelRepository, ReservationRepository reservationRepository, UserService userService) {
        this.hotelRepository = hotelRepository;
        this.reservationRepository = reservationRepository;
        this.userService = userService;

    }

    public List<Hotel> getAllHotels() {
        return hotelRepository.getAllHotels();
    }

    public Hotel getHotelById(Long id) {
        return hotelRepository.getHotelById(id);
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.getReservationById(id);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.getAllReservations();
    }

    public List<Hotel> searchHotels(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return hotelRepository.getAllHotels();
        }
        String lowerKeyword = keyword.toLowerCase();
        return hotelRepository.getAllHotels().stream()
                .filter(hotel -> hotel.getName().toLowerCase().contains(lowerKeyword)
                        || hotel.getLocation().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.getReservationById(id);
        if (reservation != null) {
            Hotel hotel = hotelRepository.getHotelById(reservation.getHotelId());
            if (hotel != null) {
                hotel.setAvailableRooms(hotel.getAvailableRooms() + 1);
            }
            reservationRepository.removeReservation(id);
        } else {
            throw new RuntimeException("Rezerwacja o ID: " + id + " nie została znaleziona!");
        }
    }

    public void makeReservation(Reservation reservation) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (reservation.getUsername() == null || reservation.getUsername().trim().isEmpty()) {
            reservation.setUsername(userService.getCurrentUser().getUsername());
        }


        if (reservation.getCheckIn().isAfter(reservation.getCheckOut())) {
            throw new RuntimeException("Data przyjazdu musi być przed datą wyjazdu!");
        }
        if (reservation.getCheckIn().isBefore(LocalDate.now())) {
            throw new RuntimeException("Data przyjazdu nie może być w przeszłości!");
        }
        Hotel hotel = hotelRepository.getHotelById(reservation.getHotelId());
        if (hotel != null && hotel.getAvailableRooms() > 0) {
            hotel.setAvailableRooms(hotel.getAvailableRooms() - 1);
            reservationRepository.addReservation(reservation);
        } else {
            throw new RuntimeException("Brak dostępnych pokoi!");
        }
    }

    public void updateReservation(Reservation updatedReservation) {
        Reservation existing = reservationRepository.getReservationById(updatedReservation.getId());
        if (existing == null) {
            throw new RuntimeException("Rezerwacja nie znaleziona!");
        }
        if (updatedReservation.getCheckIn().isAfter(updatedReservation.getCheckOut())) {
            throw new RuntimeException("Data przyjazdu musi być wcześniejsza niż data wyjazdu!");
        }
        if (updatedReservation.getCheckIn().isBefore(LocalDate.now())) {
            throw new RuntimeException("Data przyjazdu nie może być w przeszłości!");
        }
        existing.setCheckIn(updatedReservation.getCheckIn());
        existing.setCheckOut(updatedReservation.getCheckOut());
    }

    public List<Reservation> getUserReservations() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return reservationRepository.getAllReservations().stream()
                .filter(r -> username.equals(r.getUsername()))
                .collect(Collectors.toList());
    }

    public List<Reservation> getAllReservationsForAdmin() {
        return reservationRepository.getAllReservations();
    }

    public List<Reservation> getReservationsByHotelId(Long hotelId) {
        return reservationRepository.getAllReservations().stream()
                .filter(reservation -> reservation.getHotelId().equals(hotelId))
                .collect(Collectors.toList());
    }

    public List<Reservation> getUserReservationsByDate(LocalDate date) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return reservationRepository.findByUsername(username).stream()
                .filter(r -> r.getCheckIn().equals(date) || r.getCheckOut().equals(date))
                .collect(Collectors.toList());
    }
    public void deleteReservation(Long reservationId) {
        Reservation reservation = reservationRepository.getReservationById(reservationId);
        if (reservation != null) {
            reservationRepository.removeReservation(reservationId);

        }

    }
    public List<Reservation> getReservationsByUsername(String username) {
        return reservationRepository.findByUsername(username);
    }

}

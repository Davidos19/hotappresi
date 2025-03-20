package org.example.hotappresi.controllers;

import org.example.hotappresi.models.Room;
import org.example.hotappresi.services.HotelService;
import org.example.hotappresi.services.RoomService;
import org.springframework.ui.Model;
import org.example.hotappresi.models.Hotel;
import org.example.hotappresi.models.Reservation;
import org.example.hotappresi.services.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ReservationController {
    private final ReservationService reservationService;
    private final HotelService hotelService;
    private final RoomService roomService;
    public ReservationController(ReservationService reservationService, HotelService hotelService, RoomService roomService) {
        this.reservationService = reservationService;
        this.hotelService = hotelService;
        this.roomService = roomService;
    }

    @GetMapping("/hotels")
    public List<Hotel> getHotels() {
        return reservationService.getAllHotels();
    }
    @GetMapping("/reservations")
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }
    @DeleteMapping("/reservations/{id}")
    public String cancelReservation(@PathVariable Long id) {
        try {
            reservationService.cancelReservation(id);
            return "Rezerwacja anulowana!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    @PostMapping("/reservations")
    public String makeReservation(@RequestBody Reservation reservation) {
        try {
            reservationService.makeReservation(reservation);
            return "Rezerwacja udana!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    @GetMapping("/myReservations/filter")
    public String filterMyReservations(@RequestParam("date") String dateStr, Model model) {
        LocalDate date = LocalDate.parse(dateStr);
        model.addAttribute("reservations", reservationService.getUserReservationsByDate(date));
        return "my_reservations";
    }
    @GetMapping("/reservation/edit/{id}")
    public String editReservation(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.getReservationById(id);
        if (reservation == null) {
            return "redirect:/"; // lub komunikat błędu
        }
        model.addAttribute("reservation", reservation);

        // Pobierz hotel na podstawie hotelId z rezerwacji
        if (reservation.getHotelId() != null) {
            Hotel hotel = hotelService.getHotelById(reservation.getHotelId());
            model.addAttribute("hotel", hotel);
        }

        // Pobierz listę pokoi, jeśli chcesz umożliwić edycję pokoju
        if (reservation.getHotelId() != null) {
            List<Room> rooms = roomService.getRoomsByHotelId(reservation.getHotelId());
            model.addAttribute("rooms", rooms);
        }

        return "edit_reservation"; // widok edycji rezerwacji
    }



}
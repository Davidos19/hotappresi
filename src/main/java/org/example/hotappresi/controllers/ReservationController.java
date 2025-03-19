package org.example.hotappresi.controllers;

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

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
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

}
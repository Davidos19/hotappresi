package org.example.hotappresi.controllers;

import org.example.hotappresi.models.Reservation;
import org.example.hotappresi.services.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/reservations")
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // Lista rezerwacji dla admina (już istniejąca)
    @GetMapping
    public String listReservations(Model model) {
        model.addAttribute("reservations", reservationService.getAllReservationsForAdmin());
        return "admin_reservations";
    }

    // Formularz edycji rezerwacji
    @GetMapping("/edit/{id}")
    public String editReservation(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.getReservationById(id);
        if (reservation == null) {
            return "redirect:/admin/reservations?error=ReservationNotFound";
        }
        model.addAttribute("reservation", reservation);
        return "admin_edit_reservation";
    }

    // Aktualizacja rezerwacji – zapis zmian
    @PostMapping("/update")
    public String updateReservation(@ModelAttribute("reservation") Reservation reservation) {
        try {
            reservationService.updateReservation(reservation);
        } catch (RuntimeException ex) {
            return "redirect:/admin/reservations/edit/" + reservation.getId() + "?error=" + ex.getMessage();
        }
        return "redirect:/admin/reservations";
    }
}
package org.example.hotappresi.controllers;


//import ch.qos.logback.core.model.Model;
import jakarta.validation.Valid;
import org.example.hotappresi.models.Hotel;
import org.example.hotappresi.models.Review;
import org.example.hotappresi.models.Room;
import org.example.hotappresi.services.HotelService;
import org.example.hotappresi.services.ReviewService;
import org.example.hotappresi.services.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.example.hotappresi.models.Reservation;
import org.example.hotappresi.services.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class WebController {
    private static final Logger logger = LoggerFactory.getLogger(WebController.class);
    private final ReservationService reservationService;
    private final ReviewService reviewService;  // dodaj to pole
    private final HotelService hotelService;
    private final RoomService roomService;

    public WebController(HotelService hotelService, ReservationService reservationService, ReviewService reviewService, RoomService roomService) {
        this.hotelService = hotelService;
        this.reservationService = reservationService;
        this.reviewService = reviewService;// wstrzyknięcie zależności
        this.roomService = roomService;
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
        // 1. Dodaj do modelu listę hoteli do sekcji "Dostępne Hotele"
        List<Hotel> hotels = hotelService.getAllHotels();
        model.addAttribute("hotels", hotels);

        // 2. Dodaj pusty obiekt rezerwacji do formularza "Dodaj Rezerwację"
        model.addAttribute("reservation", new Reservation());

        // 3. Pobierz rezerwacje zalogowanego użytkownika
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Reservation> reservations = reservationService.getReservationsByUsername(username);

        // 4. Zbuduj mapy:
        //    hotelDetails (hotelId -> obiekt Hotel)
        //    roomDetails (roomId -> obiekt Room)
        Map<Long, Hotel> hotelDetails = new HashMap<>();
        Map<Long, Room> roomDetails = new HashMap<>();

        for (Reservation res : reservations) {
            // Hotel
            if (res.getHotelId() != null) {
                Hotel hotel = hotelService.getHotelById(res.getHotelId());
                if (hotel != null) {
                    hotelDetails.put(res.getHotelId(), hotel);
                }
            }
            // Pokój
            if (res.getRoomId() != null) {
                Room room = roomService.getRoomById(res.getRoomId());
                if (room != null) {
                    roomDetails.put(res.getRoomId(), room);
                }
            }
        }

        // 5. Dodaj rezerwacje i mapy do modelu
        model.addAttribute("reservations", reservations);
        model.addAttribute("hotelDetails", hotelDetails);
        model.addAttribute("roomDetails", roomDetails);

        // Zwróć nazwę widoku
        return "index"; // index.html
    }

    // Obsługa wysłania formularza rezerwacji (POST)
    @PostMapping("/reservation")
    public String createReservation(@Valid @ModelAttribute("reservation") Reservation reservation,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            // W przypadku błędów walidacji musimy ponownie doładować dane potrzebne do formularza
            model.addAttribute("hotels", reservationService.getAllHotels());
            model.addAttribute("reservations", reservationService.getAllReservations());
            return "index";  // Powrót do widoku głównego z komunikatami błędów
        }

        try {
            reservationService.makeReservation(reservation);
            redirectAttributes.addFlashAttribute("message", "Rezerwacja udana!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Błąd przy rezerwacji: " + e.getMessage());
        }
        return "redirect:/";
    }


    @PostMapping("/cancelReservation")
    public String cancelReservation(@RequestParam("reservationId") Long reservationId,
                                    RedirectAttributes redirectAttributes) {
        try {
            reservationService.cancelReservation(reservationId);
            redirectAttributes.addFlashAttribute("message", "Rezerwacja anulowana!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Błąd przy anulowaniu rezerwacji: " + e.getMessage());
        }
        return "redirect:/";
    }
    @GetMapping("/hotel/{id}")
    public String hotelDetails(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Hotel hotel = reservationService.getHotelById(id);
        if (hotel == null) {
            redirectAttributes.addFlashAttribute("error", "Hotel o podanym ID nie został znaleziony!");
            return "redirect:/";
        }
        // Pobierz recenzje i średnią ocenę (opcjonalnie)
        double averageRating = reviewService.getAverageRating(id);
        List<Review> reviews = reviewService.getReviewsForHotel(id);

        // Pobierz pokoje
        List<Room> rooms = roomService.getRoomsByHotelId(id);

        model.addAttribute("hotel", hotel);
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("reviews", reviews);
        model.addAttribute("rooms", rooms);

        // Jeśli chcesz formularz recenzji:
        model.addAttribute("review", new Review());

        return "hotel_details"; // nazwa szablonu
    }

    @GetMapping("/reservation/edit/{id}")
    public String editReservation(@PathVariable Long id, Model model) {
        // Pobierz istniejącą rezerwację
        Reservation reservation = reservationService.getReservationById(id);
        if (reservation == null) {
            // Jeśli rezerwacja nie istnieje – przekieruj np. na stronę główną
            return "redirect:/";
        }
        // Dodaj rezerwację do modelu
        model.addAttribute("reservation", reservation);

        // Pobierz listę dostępnych pokoi dla hotelu rezerwacji
        List<Room> rooms = roomService.getRoomsByHotelId(reservation.getHotelId());
        model.addAttribute("rooms", rooms);

        return "edit_reservation"; // widok edycji rezerwacji
    }


    @PostMapping("/reservation/update")
    public String updateReservation(@ModelAttribute("reservation") Reservation updatedRes) {
        // 1. Wywołaj serwis, aby zaktualizować dane rezerwacji
        reservationService.updateReservation(updatedRes);

        // 2. Przekieruj np. na stronę główną
        return "redirect:/";
    }

    @GetMapping("/search")
    public String searchHotels(@RequestParam("keyword") String keyword, Model model) {
        model.addAttribute("hotels", reservationService.searchHotels(keyword));
        // Dodajemy też pozostałe atrybuty potrzebne w widoku – rezerwacje oraz pusty obiekt rezerwacji
        model.addAttribute("reservations", reservationService.getAllReservations());
        model.addAttribute("reservation", new Reservation());
        // Przekazujemy również aktualną frazę wyszukiwania, żeby np. umieścić ją w polu formularza
        model.addAttribute("keyword", keyword);
        return "index";
    }

    @GetMapping("/myReservations")
    public String myReservations(Model model) {
        model.addAttribute("reservations", reservationService.getUserReservations());
        return "my_reservations";
    }

}
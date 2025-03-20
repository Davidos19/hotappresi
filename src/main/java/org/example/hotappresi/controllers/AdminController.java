package org.example.hotappresi.controllers;

import org.example.hotappresi.models.Hotel;
import org.example.hotappresi.models.Reservation;
import org.example.hotappresi.models.Room;
import org.example.hotappresi.services.HotelService;
import org.example.hotappresi.services.ReservationService;
import org.example.hotappresi.services.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/hotels")
public class AdminController {

    private final HotelService hotelService;
    private final ReservationService reservationService;
    private final RoomService roomService;

    public AdminController(HotelService hotelService, ReservationService reservationService, RoomService roomService) {
        this.hotelService = hotelService;
        this.reservationService = reservationService;
        this.roomService = roomService;
    }

    // Wyświetlenie listy hoteli dla administratora
    @GetMapping
    public String listHotels(@RequestParam(required = false) String sort, Model model) {
        System.out.println("Sort parameter: " + sort);
        List<Hotel> hotels = hotelService.getAllHotelsSorted(sort);
        model.addAttribute("hotels", hotels);
        model.addAttribute("sort", sort);
        return "admin_hotels";
    }
    @GetMapping("/admin/reservations")
    public String listReservations(Model model) {
        List<Reservation> reservations = reservationService.getAllReservationsForAdmin();
        Map<Long, Room> roomDetails = new HashMap<>();
        for (Reservation res : reservations) {
            if (res.getRoomId() != null) {
                Room room = roomService.getRoomById(res.getRoomId());
                if (room != null) {
                    roomDetails.put(res.getRoomId(), room);
                }
            }
        }
        // Dodaj logowanie
        System.out.println("Rezerwacje: " + reservations);
        System.out.println("Room Details: " + roomDetails);

        model.addAttribute("reservations", reservations);
        model.addAttribute("roomDetails", roomDetails);
        return "admin_reservations";
    }
    // Wyświetlenie listy pokoi dla danego hotelu
    @GetMapping("/{id}/rooms")
    public String listRooms(@PathVariable Long id, Model model) {
        var hotel = hotelService.getHotelById(id);
        if (hotel == null) {
            return "redirect:/admin/hotels";
        }
        var rooms = roomService.getRoomsByHotelId(id);
        model.addAttribute("hotel", hotel);
        model.addAttribute("rooms", rooms);
        return "edit_hotel";  // Widok, który wyświetla listę pokoi dla hotelu
    }

    // Formularz dodawania nowego pokoju do hotelu
    @GetMapping("/{hotelId}/rooms/new")
    public String newRoom(@PathVariable Long hotelId, Model model) {
        Hotel hotel = hotelService.getHotelById(hotelId);
        if (hotel == null) {
            return "redirect:/admin/hotels";
        }
        Room room = new Room();
        room.setHotelId(hotelId);  // Ustawienie hotelId!
        model.addAttribute("hotel", hotel);
        model.addAttribute("room", room);
        return "admin_new_room";
    }


    // Zapis nowego pokoju
    @PostMapping("/{hotelId}/rooms/save")
    public String saveRoom(@PathVariable Long hotelId, @ModelAttribute("room") Room room, RedirectAttributes redirectAttributes) {
        roomService.addRoom(room);
        redirectAttributes.addFlashAttribute("message", "Pokój został dodany");
        return "redirect:/admin/hotels/" + hotelId + "/rooms";
    }



    // Formularz dodawania nowej rezerwacji przez admina
    @GetMapping("/{hotelId}/reservations/new")
    public String newReservationForHotel(@PathVariable Long hotelId, Model model) {
        Hotel hotel = hotelService.getHotelById(hotelId);
        if (hotel == null) {
            return "redirect:/admin/hotels";
        }

        // Nowa rezerwacja z ustawionym hotelId
        Reservation reservation = new Reservation();
        reservation.setHotelId(hotelId);

        // Pobierz listę pokoi danego hotelu
        List<Room> rooms = roomService.getRoomsByHotelId(hotelId);

        model.addAttribute("hotel", hotel);
        model.addAttribute("rooms", rooms);
        model.addAttribute("reservation", reservation);

        return "admin_new_reservation";
    }




    // Zapis rezerwacji przez admina
    @PostMapping("/reservations/save")
    public String saveReservation(@ModelAttribute("reservation") Reservation reservation,
                                  RedirectAttributes redirectAttributes) {
        try {
            reservationService.makeReservation(reservation);
            redirectAttributes.addFlashAttribute("message", "Rezerwacja została dodana.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        // Po zapisaniu wracamy do rezerwacji konkretnego hotelu
        return "redirect:/admin/hotels/" + reservation.getHotelId() + "/reservations";
    }




    // Formularz dodawania nowego hotelu
    @GetMapping("/new")
    public String newHotel(Model model) {
        model.addAttribute("hotel", new Hotel());
        return "edit_hotel";  // Widok formularza tworzenia nowego hotelu
    }


    // Zapis nowego hotelu (lub aktualizacja istniejącego)
    @PostMapping("/save")
    public String saveHotel(@ModelAttribute("hotel") Hotel hotel) {
        hotelService.saveHotel(hotel);
        return "redirect:/admin/hotels";
    }

    @GetMapping("/edit/{id}")
    public String editHotel(@PathVariable Long id, Model model) {
        Hotel hotel = hotelService.getHotelById(id);
        if (hotel == null) {
            return "redirect:/admin/hotels";
        }
        model.addAttribute("hotel", hotel);
        // Pobierz pokoje związane z hotelem
        model.addAttribute("rooms", roomService.getRoomsByHotelId(id));
        return "edit_hotel";
    }

    @GetMapping("/admin/reservations/delete/{id}")
    public String deleteReservation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reservationService.cancelReservation(id);
            redirectAttributes.addFlashAttribute("message", "Rezerwacja została anulowana!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Nie udało się anulować rezerwacji: " + ex.getMessage());
        }
        return "redirect:/admin_hotel_reservations"; // lub inny widok z listą rezerwacji
    }

    // Usunięcie hotelu
    @GetMapping("/delete/{id}")
    public String deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return "redirect:/admin/hotels";
    }
    @GetMapping("/{id}/reservations")
    public String hotelReservations(@PathVariable Long id, Model model ) {
        Hotel hotel = hotelService.getHotelById(id);
        if (hotel == null) {
            return "redirect:/admin/hotels";
        }

        List<Reservation> reservations = reservationService.getReservationsByHotelId(id);
        System.out.println("Rezerwacje: " + reservations);
        Map<Long, Room> roomDetails = new HashMap<>();
        for (Reservation res : reservations) {
            if (res.getRoomId() != null) {
                Room room = roomService.getRoomById(res.getRoomId());
                if (room != null) {
                    roomDetails.put(res.getRoomId(), room);
                }
            }
        }
        System.out.println("Room Details: " + roomDetails);

        model.addAttribute("hotel", hotel);
        model.addAttribute("reservations", reservationService.getReservationsByHotelId(id));
        model.addAttribute("roomDetails", roomDetails);

        return "admin_hotel_reservations";
    }

}
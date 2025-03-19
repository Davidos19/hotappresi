package org.example.hotappresi.services;

import org.example.hotappresi.models.Hotel;
import org.example.hotappresi.repositories.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }
    public List<Hotel> getAllHotels() {
        return hotelRepository.getAllHotels().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Hotel getHotelById(Long id) {
        return hotelRepository.getAllHotels().stream()
                .filter(hotel -> hotel.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void saveHotel(Hotel hotel) {
        // Jeśli hotel posiada ID, zaktualizuj istniejący wpis
        if (hotel.getId() != null) {
            hotelRepository.updateHotel(hotel);
        } else {
            hotelRepository.addHotel(hotel);
        }
    }

    public void deleteHotel(Long id) {
        hotelRepository.deleteHotel(id);
    }


    public List<Hotel> getAllHotelsSorted(String sortBy) {
        List<Hotel> hotels = getAllHotels();
        if ("name".equalsIgnoreCase(sortBy)) {
            hotels.sort(Comparator.comparing(Hotel::getName));
        } else if ("location".equalsIgnoreCase(sortBy)) {
            hotels.sort(Comparator.comparing(Hotel::getLocation));
        }
        return hotels;
    }
    public List<Hotel> getRecommendedHotels() {
        // Tutaj możesz na przykład zwrócić pierwsze 3 hotele lub posortować po ocenie
        List<Hotel> hotels = getAllHotels();
        return hotels.stream().limit(3).collect(Collectors.toList());
    }
}
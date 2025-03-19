package org.example.hotappresi.services;

import org.example.hotappresi.models.Room;
import org.example.hotappresi.repositories.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getRoomsByHotelId(Long hotelId) {
        return roomRepository.getRoomsByHotelId(hotelId);
    }
    public Room getRoomById(Long roomId) {
        return roomRepository.getAllRooms().stream()
                .filter(r -> r.getId().equals(roomId))
                .findFirst()
                .orElse(null);
    }
    public void addRoom(Room room) {
        roomRepository.addRoom(room);
    }
    public void removeRoom(Long id) {
        roomRepository.removeRoom(id);
    }

    // Możesz dodać metody do dodawania, aktualizacji czy usuwania pokoju.
}

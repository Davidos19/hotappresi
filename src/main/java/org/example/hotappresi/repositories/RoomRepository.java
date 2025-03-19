package org.example.hotappresi.repositories;

import org.example.hotappresi.models.Room;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RoomRepository {
    private final List<Room> rooms = new ArrayList<>();
    private Long currentId = 1L;

    public RoomRepository() {
        // Przykładowe pokoje dla hotelu o ID 1
        addRoom(new Room(null, 1L, "Standard", 2, 150.0, "https://source.unsplash.com/400x300/?standard,room", "101"));
        addRoom(new Room(null, 1L, "Deluxe", 3, 250.0, "https://source.unsplash.com/400x300/?deluxe,room", "102"));

        // Przykładowe pokoje dla hotelu o ID 2
        addRoom(new Room(null, 2L, "Standard", 2, 120.0, "https://source.unsplash.com/400x300/?standard,room", "201"));
        addRoom(new Room(null, 2L, "Suite", 4, 400.0, "https://source.unsplash.com/400x300/?suite,room", "202"));
    }


    public List<Room> getAllRooms() {
        return rooms;
    }

    public void addRoom(Room room) {
        room.setId(currentId++);
        rooms.add(room);
    }

    public List<Room> findByHotelId(Long hotelId) {
        return rooms.stream()
                .filter(room -> room.getHotelId() != null && room.getHotelId().equals(hotelId))
                .collect(Collectors.toList());
    }
    public Room getRoomById(Long id) {
        return rooms.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Room> getRoomsByHotelId(Long hotelId) {
        return rooms.stream()
                .filter(r -> r.getHotelId()!= null && r.getHotelId().equals(hotelId))
                .collect(Collectors.toList());
    }

    public void removeRoom(Long id) {
        rooms.removeIf(r -> r.getId().equals(id));
    }

}


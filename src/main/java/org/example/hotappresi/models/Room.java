package org.example.hotappresi.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long hotelId;

    private String roomType;   // np. "Standard", "Deluxe", "Suite"
    private int capacity;      // Liczba osób
    private double price;      // Cena za noc
    private String imageUrl;   // URL do zdjęcia pokoju

    // Nowe pole: numer pokoju
    private String roomNumber; // np. "101", "202A"
}

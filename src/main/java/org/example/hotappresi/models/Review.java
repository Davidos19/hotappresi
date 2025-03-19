package org.example.hotappresi.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Powiązanie recenzji z hotelem (możesz przechowywać tylko hotelId lub relację ManyToOne)
    private Long hotelId;

    // Alternatywnie, relacja:
    // @ManyToOne
    // @JoinColumn(name = "hotel_id")
    // private Hotel hotel;

    // Dane użytkownika (można zapisać username lub relację z użytkownikiem)
    private String username;

    // Ocena (np. od 1 do 5)
    private int rating;

    // Komentarz
    private String comment;
}

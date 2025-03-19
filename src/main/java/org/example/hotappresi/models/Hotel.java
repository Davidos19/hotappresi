package org.example.hotappresi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {
    private Long id;
    private String name;
    private String location;
    private int availableRooms;
}
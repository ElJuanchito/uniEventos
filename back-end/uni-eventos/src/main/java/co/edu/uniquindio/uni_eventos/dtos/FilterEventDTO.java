package co.edu.uniquindio.uni_eventos.dtos;

import co.edu.uniquindio.uni_eventos.entities.EventType;

public record FilterEventDTO(
        String name,
        EventType type,
        String city

) {
}

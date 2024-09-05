package co.edu.uniquindio.uni_eventos.dtos;

import co.edu.uniquindio.uni_eventos.entities.EventStatus;
import co.edu.uniquindio.uni_eventos.entities.EventType;
import co.edu.uniquindio.uni_eventos.entities.Section;

import java.time.LocalDateTime;
import java.util.List;

public record InfoEventDTO(
        String id,
        String coverImg,
        EventStatus status,
        String name,
        String description,
        String address,
        String sectionImg,
        EventType type,
        LocalDateTime date,
        String city,
        List<Section>sections
) {

}

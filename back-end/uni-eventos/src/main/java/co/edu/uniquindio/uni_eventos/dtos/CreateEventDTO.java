package co.edu.uniquindio.uni_eventos.dtos;

import co.edu.uniquindio.uni_eventos.entities.EventStatus;
import co.edu.uniquindio.uni_eventos.entities.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateEventDTO(
        @NotBlank String coverImg,
        @NotNull EventStatus status,
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String address,
        @NotBlank String sectionImg,
        @NotNull EventType type,
        @NotNull LocalDateTime date,
        @NotBlank String city
        //sections;
) {
}

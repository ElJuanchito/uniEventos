package co.edu.uniquindio.uni_eventos.entities;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ValidationCode {

    private LocalDateTime creationDate;
    private String code;
}

package co.edu.uniquindio.uni_eventos.dtos;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UpdateAccountDTO(
        @NotBlank String id,
        @NotBlank @Length(max = 50) String name,
        @NotBlank @Length(min = 7, max = 20) String password,
        @Length(max = 10) String phone,
        @Length(max = 100) String address
) {
}

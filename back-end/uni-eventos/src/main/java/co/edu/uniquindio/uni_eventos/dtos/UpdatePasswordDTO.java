package co.edu.uniquindio.uni_eventos.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UpdatePasswordDTO(
        @NotBlank @Email @Length(max = 20) String email,
        @NotBlank @Length(max = 6, min = 6) String validationCode,
        @NotBlank @Length(min = 8, max = 18) String updatedPassword
) {
}

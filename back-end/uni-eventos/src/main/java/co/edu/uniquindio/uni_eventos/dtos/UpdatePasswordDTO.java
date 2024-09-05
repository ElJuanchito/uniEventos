package co.edu.uniquindio.uni_eventos.dtos;

public record UpdatePasswordDTO(
        String email,
        String validationCode,
        String updatedPassword
) {
}

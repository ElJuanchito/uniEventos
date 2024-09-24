package co.edu.uniquindio.uni_eventos.controllers;

import co.edu.uniquindio.uni_eventos.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EnviarCorreoController {

    private final EmailService emailService;

    @GetMapping
    public String enviarCorreoTourment(){
        String to = "juanm.perdomo@uqvirtual.edu.co";
        String subject = "Conferencia, ponencia TD-MBUILD tool. mexico";
        String text = """
                Debe concretar la subida de documentos antes de la fecha de cierre,\s
                La fecha ultima para cargar los documentos es 2024/09/11 hasta las 6:00 pm.\s
                Los documentos deben ser cargados en formato pdf.
                Debe subir los documentos mediante el siguiente link https://bit.ly/sinfoci_documentos
                """;
        emailService.sendEmail(to, subject, text);
        return "El correo se envio con exito";
    }
}

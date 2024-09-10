package co.edu.uniquindio.uni_eventos.controllers;

import co.edu.uniquindio.uni_eventos.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EnviarCorreoController {

    private final EmailService emailService;

    @GetMapping
    public String enviarCorreoTourment(){
        String to = "santiago.quinterou@uqvirtual.edu.co";
        String subject = "Conferencia, ponencia TD-MBUILD tool. mexico";
        String text = "Debe concretar la subida de documentos antes de la fecha de cierre";
        emailService.sendEmail(to, subject, text);
        return "El correo se envio con exito";
    }
}

package co.edu.uniquindio.uni_eventos.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String phone;
    private String address;
    private String cedula;
    private String name;
}

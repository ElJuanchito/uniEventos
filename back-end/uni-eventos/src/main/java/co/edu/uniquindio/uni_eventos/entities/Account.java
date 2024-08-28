package co.edu.uniquindio.uni_eventos.entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "accounts")
public class Account {

    @Id
    private String id;
    private Role role;
    private String email;
    private ValidationCode registrationCode;
    private ObjectId userId;
    private LocalDateTime registrationDate;
    private String password;
    private AccountStatus status;
    private ValidationCode passwordCode;
}

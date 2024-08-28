package co.edu.uniquindio.uni_eventos.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "payments")
public class Payment {

    @Id
    private String id;
    private String currency;
    private String paymentType;
    private String statusDetail;
    private String authorizationCode;
    private LocalDateTime date;
    private Float transactionAmount;
    private String status;
}

package co.edu.uniquindio.uni_eventos.entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@Document(collection = "orders")
public class Order {

    @Id
    private String id;
    private ObjectId customerId;
    private LocalDate date;
    private String gatewayCode;
    private List<OrderDetail> items;
    private Payment payment;
    private Float total;
    private ObjectId couponId;
}

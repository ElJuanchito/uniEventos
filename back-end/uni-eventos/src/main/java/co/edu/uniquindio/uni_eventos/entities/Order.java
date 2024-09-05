package co.edu.uniquindio.uni_eventos.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@Document(collection = "orders")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order {

    @Id
    @EqualsAndHashCode.Include
    private String id;
    private ObjectId customerId;
    private LocalDate date;
    private String gatewayCode;
    private List<OrderDetail> items;
    private Payment payment;
    private Float total;
    private ObjectId couponId;
}

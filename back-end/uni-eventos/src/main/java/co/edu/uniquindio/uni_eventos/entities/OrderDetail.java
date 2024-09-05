package co.edu.uniquindio.uni_eventos.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "order-details")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderDetail {

    @Id
    @EqualsAndHashCode.Include
    private String id;
    private ObjectId eventId;
    private Float price;
    private String sectionName;
    private Integer quantity;
}

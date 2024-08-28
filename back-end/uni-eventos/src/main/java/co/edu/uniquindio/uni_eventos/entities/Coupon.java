package co.edu.uniquindio.uni_eventos.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "coupons")
public class Coupon {

    @Id
    private String id;
    private Float discount;
    private LocalDateTime expirationDate;
    private String code;
    private CouponStatus status;
    private CouponType type;
    private String name;
}

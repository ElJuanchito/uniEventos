package co.edu.uniquindio.uni_eventos.repositories;

import co.edu.uniquindio.uni_eventos.entities.Coupon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends MongoRepository<Coupon, String> {
}

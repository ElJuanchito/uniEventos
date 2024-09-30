package co.edu.uniquindio.uni_eventos.services;

import com.mercadopago.resources.preference.Preference;

import java.util.Map;

public interface OrderService {

    Preference makePayment(String orderId) throws Exception;
    void receiveMercadoLibreNotification(Map<String, Object> request);
}

package co.edu.uniquindio.uni_eventos.services;

import co.edu.uniquindio.uni_eventos.dtos.order.CreateOrderDTO;
import co.edu.uniquindio.uni_eventos.dtos.order.OrderInfoDTO;
import co.edu.uniquindio.uni_eventos.entities.Order;
import co.edu.uniquindio.uni_eventos.exceptions.*;
import com.mercadopago.resources.preference.Preference;

import java.util.List;
import java.util.Map;

public interface OrderService {

    void createOrder(CreateOrderDTO orderDTO) throws Exception;
    void cancelOrder(String orderId) throws Exception;
    List<OrderInfoDTO> getOrderHistory(String userId) throws Exception;
    OrderInfoDTO getOrder(String id) throws OrderNotExistsException;

    Preference makePayment(String orderId) throws Exception;
    void receiveMercadoLibreNotification(Map<String, Object> request);
}

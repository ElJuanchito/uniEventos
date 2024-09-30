package co.edu.uniquindio.uni_eventos.services.impl;

import co.edu.uniquindio.uni_eventos.entities.*;
import co.edu.uniquindio.uni_eventos.exceptions.AccountNotExistsException;
import co.edu.uniquindio.uni_eventos.exceptions.OrderNotExistsException;
import co.edu.uniquindio.uni_eventos.repositories.OrderRepository;
import co.edu.uniquindio.uni_eventos.services.EventService;
import co.edu.uniquindio.uni_eventos.services.OrderService;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor //TODO Completar los datos de la pasarela de pago
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final EventService eventService;

    @Override
    public Preference makePayment(String orderId) throws Exception {
        Order order = getOrderById(orderId);
        List<PreferenceItemRequest> gatewayItems = new ArrayList<>();

        for(OrderDetail item : order.getItems()){
            Event event = eventService.getEventById(item.getEventId().toString());
            Section section = event
                    .getSections()
                    .stream()
                    .filter(s-> s.getName().equals(item.getSectionName()))
                    .findFirst().get();

            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id(event.getId())
                    .title(event.getName())
                    .pictureUrl(event.getCoverImg())
                    .categoryId(event.getType().name())
                    .quantity(item.getQuantity())
                    .currencyId("COP")
                    .unitPrice(BigDecimal.valueOf(section.getPrice()))
                    .build();

            gatewayItems.add(itemRequest);
        }

        MercadoPagoConfig.setAccessToken("ACCESS_TOKEN");


        // Configurar las urls de retorno de la pasarela (Frontend)
        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("URL PAGO EXITOSO")
                .failure("URL PAGO FALLIDO")
                .pending("URL PAGO PENDIENTE")
                .build();


        // TODO Construir la preferencia de la pasarela con los ítems, metadatos y urls de retorno
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .backUrls(backUrls)
                .items(gatewayItems)
                .metadata(Map.of("id_orden", order.getId()))
                .notificationUrl("URL NOTIFICACION")
                .build();


        // Crear la preferencia en la pasarela de MercadoPago
        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);


        // Guardar el código de la pasarela en la orden
        order.setGatewayCode(preference.getId());
        orderRepository.save(order);


        return preference;
    }

    @Override
    public void receiveMercadoLibreNotification(Map<String, Object> request) {

    }

    private Order getOrderById(@NotNull String id) throws Exception {
        Optional<Order> optionalOrder = orderRepository.findById(id);

        if(optionalOrder.isEmpty()) throw new OrderNotExistsException("La orden con el id " + id + "no existe" );
        return optionalOrder.get();
    }
}

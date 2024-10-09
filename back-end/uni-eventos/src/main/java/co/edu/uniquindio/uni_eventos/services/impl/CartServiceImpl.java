package co.edu.uniquindio.uni_eventos.services.impl;

import co.edu.uniquindio.uni_eventos.dtos.cart.AddCartDetailDTO;
import co.edu.uniquindio.uni_eventos.dtos.cart.CartDetailInfoDTO;
import co.edu.uniquindio.uni_eventos.dtos.cart.CartInfoDTO;
import co.edu.uniquindio.uni_eventos.dtos.cart.RemoveCartDetailDTO;
import co.edu.uniquindio.uni_eventos.entities.Cart;
import co.edu.uniquindio.uni_eventos.entities.CartDetail;
import co.edu.uniquindio.uni_eventos.exceptions.*;
import co.edu.uniquindio.uni_eventos.mappers.CartMapper;
import co.edu.uniquindio.uni_eventos.repositories.AccountRepository;
import co.edu.uniquindio.uni_eventos.repositories.CartRepository;
import co.edu.uniquindio.uni_eventos.services.CartService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final AccountRepository accountRepository;
    private final CartMapper cartMapper;


    @Override
    public void createCart(String userId) throws CartExistsException, AccountNotExistsException {
        if(!accountRepository.existsById(userId)) throw new AccountNotExistsException("El usuario con id: " + userId + " no existe");
        if(cartRepository.getCartByUserId(userId).isPresent()) throw new CartExistsException("El carrito del usuario ya existe");

        Cart cart = Cart.builder()
                .date(LocalDateTime.now())
                .userId(new ObjectId(userId))
                .build();

        cartRepository.save(cart);
    }

    @Override
    public void addCartDetail(AddCartDetailDTO cartDetailDTO) throws CartDetailExistsException, CartNotExistsException {
        Cart cart = getAndValidCart(cartDetailDTO.userId());

        if (cart.getItems()
                .stream()
                .anyMatch(
                        cartDetail -> detailIsPresent(cartDetail, cartDetailDTO.quantity(), cartDetailDTO.eventId(), cartDetailDTO.sectionName())
        )) throw new CartDetailExistsException("El detalle ya existe en el carrito");

        CartDetail detail = cartMapper.toCartDetail(cartDetailDTO);

        cart.getItems().add(detail);

        cartRepository.save(cart);
    }

    @Override
    public void removeCartDetail(RemoveCartDetailDTO cartDetailDTO) throws CartDetailNotExistsException, CartNotExistsException {

        Cart cart = getAndValidCart(cartDetailDTO.userId());

        CartDetail detail = cart.getItems()
                .stream()
                .filter(
                        cartDetail -> detailIsPresent(cartDetail, cartDetailDTO.quantity(), cartDetailDTO.eventId(), cartDetailDTO.sectionName())
                ).findFirst().orElseThrow(() -> new CartDetailNotExistsException("El detalle no existe en el carrito"));

        cart.getItems().remove(detail);

        cartRepository.save(cart);
    }

    @Override
    public List<CartDetailInfoDTO> getCartDetailsInfo(String userId) throws AccountNotExistsException, CartNotExistsException {
        if(accountRepository.existsById(userId)) throw new AccountNotExistsException("La cuenta de usuario con id: "+ userId + " no existe");

        Cart cart = getAndValidCart(userId);

        return cart.getItems().stream().map(cartMapper::toDetailInfoDTO).toList();
    }

    @Override
    public CartInfoDTO getCartInfo(String userId) throws AccountNotExistsException, CartNotExistsException {

        if(accountRepository.existsById(userId)) throw new AccountNotExistsException("La cuenta de usuario con id: "+ userId + " no existe");

        Cart cart = getAndValidCart(userId);

        return new CartInfoDTO(cart.getDate(), cart.getItems().stream().map(cartMapper::toDetailInfoDTO).toList());
    }

    private boolean detailIsPresent(CartDetail cartDetail, Integer quantity, String eventId, String sectionName) {
        return (cartDetail.getQuantity().equals(quantity) && cartDetail.getEventId().equals(new ObjectId(eventId)) && cartDetail.getSectionName().equals(sectionName));
    }

    private Cart getAndValidCart(String userId) throws CartNotExistsException {
        Optional<Cart> optionalCart = cartRepository.getCartByUserId(userId);

        if(optionalCart.isEmpty()) throw new CartNotExistsException("EL carrito del usuario: "+ userId + " no existe");

        return optionalCart.get();
    }
}

package co.edu.uniquindio.uni_eventos.services;

import co.edu.uniquindio.uni_eventos.dtos.account.*;
import co.edu.uniquindio.uni_eventos.dtos.security.TokenDTO;
import co.edu.uniquindio.uni_eventos.entities.Account;
import co.edu.uniquindio.uni_eventos.exceptions.AccountNotExistsException;

public interface AccountService {

    void createAccount(CreateAccountDTO accountDTO) throws Exception;
    void validateAccount(String email, String validationCode) throws Exception;
    void updateAccount(UpdateAccountDTO accountDTO) throws Exception;
    void deleteAccount(String id) throws Exception;
    AccountInfoDTO getAccount(String id) throws AccountNotExistsException;
    void sendPasswordCode(String email) throws Exception;
    void recoverPassword(UpdatePasswordDTO updatePasswordDTO) throws Exception;
    TokenDTO login(LoginDTO loginDTO) throws Exception;
    Account getAccountById(String id) throws AccountNotExistsException;
}

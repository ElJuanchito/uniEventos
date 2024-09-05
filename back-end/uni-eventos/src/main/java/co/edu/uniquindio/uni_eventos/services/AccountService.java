package co.edu.uniquindio.uni_eventos.services;

import co.edu.uniquindio.uni_eventos.dtos.*;
import co.edu.uniquindio.uni_eventos.entities.Account;

public interface AccountService {

    String createAccount(CreateAccountDTO accountDTO) throws Exception;
    String updateAccount(UpdateAccountDTO accountDTO) throws Exception;;
    String deleteAccount(String id) throws Exception;;
    InfoAccountDTO getAccount(String id) throws Exception;
    String sendPasswordCode(String email) throws Exception;
    String recoverPassword(UpdatePasswordDTO updatePasswordDTO) throws Exception;;
    String login(LoginDTO loginDTO) throws Exception;;
}

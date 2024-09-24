package co.edu.uniquindio.uni_eventos.services;

import co.edu.uniquindio.uni_eventos.dtos.*;

public interface AccountService {

    void createAccount(CreateAccountDTO accountDTO) throws Exception;
    void validateAccount(String email, String validationCode) throws Exception;
    void updateAccount(UpdateAccountDTO accountDTO) throws Exception;
    void deleteAccount(String id) throws Exception;;
    InfoAccountDTO getAccount(String id) throws Exception;
    void sendPasswordCode(String email) throws Exception;
    void recoverPassword(UpdatePasswordDTO updatePasswordDTO) throws Exception;;
    void login(LoginDTO loginDTO) throws Exception;;
}

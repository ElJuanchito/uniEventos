package co.edu.uniquindio.uni_eventos.services.impl;

import co.edu.uniquindio.uni_eventos.dtos.*;
import co.edu.uniquindio.uni_eventos.entities.*;
import co.edu.uniquindio.uni_eventos.exceptions.AccountNotExistsException;
import co.edu.uniquindio.uni_eventos.exceptions.CedulaExistsException;
import co.edu.uniquindio.uni_eventos.exceptions.EmailExistsException;
import co.edu.uniquindio.uni_eventos.repositories.AccountRepository;
import co.edu.uniquindio.uni_eventos.services.AccountService;
import co.edu.uniquindio.uni_eventos.services.CodeGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CodeGeneratorService codeService;

    @Override
    public String createAccount(CreateAccountDTO accountDTO) throws Exception {

        validateAccountCreation(accountDTO);

        Account account = Account.builder()
                .email(accountDTO.email())
                .password(accountDTO.password())
                .role(Role.CUSTOMER)
                .registrationDate(LocalDateTime.now())
                .status(AccountStatus.INACTIVE)

                .user(User.builder()
                        .phone(accountDTO.phone())
                        .address(accountDTO.address())
                        .name(accountDTO.name())
                        .cedula(accountDTO.cedula())
                        .build())

                .registrationCode(ValidationCode.builder()
                        .code(getValidationCode())
                        .creationDate(LocalDateTime.now())
                        .build())

                .build();

        account = accountRepository.save(account);
        return account.getId();
    }

    @Override
    public String updateAccount(UpdateAccountDTO accountDTO) throws Exception {

        Account account = getAccountById(accountDTO.id());
        account.getUser().setName(accountDTO.name());
        account.getUser().setAddress(accountDTO.address());
        account.getUser().setPhone(accountDTO.phone());
        account.setPassword(accountDTO.password());

        accountRepository.save(account);

        return "update";
    }

    @Override
    public String deleteAccount(String id) throws Exception {

        Account account = getAccountById(id);
        account.setStatus(AccountStatus.DELETED);

        accountRepository.save(account);

        return "Deleted";
    }

    @Override
    public InfoAccountDTO getAccount(String id) throws Exception {
        return null;
    }

    @Override
    public String sendPasswordCode(String email) throws Exception {
        Optional<Account> optionalAccount = accountRepository.findAccountByEmail(email);

        if (optionalAccount.isEmpty()) throw new Exception("El correo no esta registrado");

        Account account = optionalAccount.get();
        String code = getValidationCode();
        //TODO Enviar este codigo al usuario por correo

        account.setPasswordCode(ValidationCode.builder()
                .code(code)
                .creationDate(LocalDateTime.now())
                .build());

        accountRepository.save(account);

        return "Se ha enviado el codigo a su correo, con una duracion de 15 minutos";
    }

    @Override
    public String recoverPassword(UpdatePasswordDTO updatePasswordDTO) throws Exception {
        Optional<Account> optionalAccount = accountRepository.findAccountByEmail(updatePasswordDTO.email());

        if(optionalAccount.isEmpty()) throw new Exception("El correo no esta registrado");

        Account account = optionalAccount.get();
        ValidationCode code = account.getPasswordCode();

        if(code != null) {
            if(code.getCode().equals(updatePasswordDTO.validationCode())) {

                if(code.getCreationDate().plusMinutes(15).isBefore(LocalDateTime.now())) {
                    account.setPassword(updatePasswordDTO.updatedPassword());
                    accountRepository.save(account);
                } else {
                    account.setPasswordCode(null);
                    accountRepository.save(account);
                    throw new Exception("Su codigo de verificacion ya expiro");
                }
            }else throw new Exception("El codigo no es correcto");
        }
        return "La contrasena se ha cambiado";
    }

    @Override
    public String login(LoginDTO loginDTO) throws Exception {
        Optional<Account> optionalAccount = accountRepository.validateLogin(loginDTO.email(), loginDTO.password());

        if(optionalAccount.isEmpty()) throw new Exception("Invalid email or password");

        return "TOKEN_JWT";
    }

    private String getValidationCode() {
        return codeService.generateCode(6);
    }

    private boolean validateCedula(String cedula){
        return accountRepository.findAccountByCedula(cedula).isPresent();
    }

    private boolean validateEmail(String email) {
        return accountRepository.findAccountByEmail(email).isPresent();
    }

    private void validateAccountCreation(CreateAccountDTO accountDTO) throws CedulaExistsException, EmailExistsException {
        if(!validateCedula(accountDTO.cedula())) throw new CedulaExistsException(String.format("Account with cedula: %s exists in database", accountDTO.cedula()));

        if(!validateEmail(accountDTO.email())) throw new EmailExistsException(String.format("Account with email: %s exists in database", accountDTO.email()));
    }

    private Account getAccountById(String id) throws AccountNotExistsException {
        Optional<Account> optionalAccount = accountRepository.findById(id);

        if(optionalAccount.isEmpty()) throw new AccountNotExistsException("La cuenta con el id " + id + "no existe" );
        return optionalAccount.get();
    }

    private void sendRegistrationEmail(){

    }
}

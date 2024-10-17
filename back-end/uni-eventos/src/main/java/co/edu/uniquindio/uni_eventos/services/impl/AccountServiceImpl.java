package co.edu.uniquindio.uni_eventos.services.impl;

import co.edu.uniquindio.uni_eventos.config.JWTUtils;
import co.edu.uniquindio.uni_eventos.dtos.account.*;
import co.edu.uniquindio.uni_eventos.dtos.security.TokenDTO;
import co.edu.uniquindio.uni_eventos.entities.*;
import co.edu.uniquindio.uni_eventos.exceptions.*;
import co.edu.uniquindio.uni_eventos.repositories.AccountRepository;
import co.edu.uniquindio.uni_eventos.services.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CodeGeneratorService codeService;
    private final EmailService emailService;
    private final CartService cartService;
    private final JWTUtils jwtUtils;

    @Override
    public void createAccount(CreateAccountDTO accountDTO) throws CedulaExistsException, EmailExistsException {

        if(accountRepository.findAccountByEmail(accountDTO.email()).isPresent()) {
            System.out.println("Pailas, email");
            throw new EmailExistsException("La cuenta con email:" + accountDTO.email() + ", ya existe");
        }
        if(accountRepository.findAccountByCedula(accountDTO.cedula()).isPresent()) {
            System.out.println("Pailas, cedula");
            throw new CedulaExistsException("La cuenta con cedula:" + accountDTO.cedula() + ", ya existe");
        }

        Account account = mapCreateDTOToEntity(accountDTO);
        account = accountRepository.save(account);

        sendRegistrationCode(account.getEmail(), account.getRegistrationCode().getCode());
    }

    @Override
    public void validateAccount(String email, String validationCode) throws AccountNotExistsException, CodeExpiredException, WrongCodeException, CartExistsException, MailNotExistsException {
        Account account = getAccountByEmail(email);

        ValidationCode code = account.getRegistrationCode();

        if(code != null) {
            if(code.getCode().equals(validationCode)) {

                if(code.getCreationDate().plusMinutes(15).isAfter(LocalDateTime.now())) {
                    account.setStatus(AccountStatus.ACTIVE);
                    accountRepository.save(account);
                    cartService.createCart(account.getId());
                } else {
                    account.setRegistrationCode(null);
                    accountRepository.save(account);
                    throw new CodeExpiredException("Su codigo de verificacion ya expiro");
                }
            }else throw new WrongCodeException("El codigo no es correcto");
        }
    }

    @Override
    public void updateAccount(UpdateAccountDTO accountDTO) throws AccountNotExistsException {

        Account account = getAccountById(accountDTO.id());
        account.getUser().setName(accountDTO.name());
        account.getUser().setAddress(accountDTO.address());
        account.getUser().setPhone(accountDTO.phone());
        account.setPassword(accountDTO.password());

        accountRepository.save(account);
    }

    @Override
    public void deleteAccount(String id) throws AccountNotExistsException {

        Account account = getAccountById(id);
        account.setStatus(AccountStatus.DELETED);

        accountRepository.save(account);

    }

    @Override
    public AccountInfoDTO getAccount(String id) throws AccountNotExistsException {
        Account account = getAccountById(id);
        return mapToInfoDTO(account);
    }

    @Override
    public void sendPasswordCode(String email) throws MailNotExistsException {

        Account account = getAccountByEmail(email);

        String code = getValidationCode();
        sendPasswordCodeEmail(email, code);


        account.setPasswordCode(ValidationCode.builder()
                .code(code)
                .creationDate(LocalDateTime.now())
                .build());

        accountRepository.save(account);
    }

    private Account getAccountByEmail(String email) throws MailNotExistsException {
        Optional<Account> optionalAccount = accountRepository.findAccountByEmail(email);

        if (optionalAccount.isEmpty()) throw new MailNotExistsException("El correo no esta registrado");
        return optionalAccount.get();
    }

    @Override
    public void recoverPassword(UpdatePasswordDTO updatePasswordDTO) throws MailNotExistsException, WrongCodeException, CodeExpiredException {

        Account account = getAccountByEmail(updatePasswordDTO.email());

        ValidationCode code = account.getPasswordCode();

        if(code != null) {
            if(code.getCode().equals(updatePasswordDTO.validationCode())) {

                if(code.getCreationDate().plusMinutes(15).isAfter(LocalDateTime.now())) {
                    account.setPassword(new BCryptPasswordEncoder().encode(updatePasswordDTO.updatedPassword()));
                    accountRepository.save(account);
                } else {
                    account.setPasswordCode(null);
                    accountRepository.save(account);
                    throw new CodeExpiredException("Su codigo de verificacion ya expiro");
                }
            }else throw new WrongCodeException("El codigo no es correcto");
        }
    }

    @Override
    public TokenDTO login(LoginDTO loginDTO) throws Exception {

        Account account = getAccountByEmail(loginDTO.email());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if(!passwordEncoder.matches(loginDTO.password(), account.getPassword())) throw new WrongPasswordException("La contrasena es incorrecta");

        Map<String, Object> map = buildClaims(account);
        return new TokenDTO(account.getId(),jwtUtils.generateToken(account.getEmail(), map));
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
        if(validateCedula(accountDTO.cedula())) throw new CedulaExistsException(String.format("Account with cedula: %s exists in database", accountDTO.cedula()));

        if(validateEmail(accountDTO.email())) throw new EmailExistsException(String.format("Account with email: %s exists in database", accountDTO.email()));
    }

    @Override
    public Account getAccountById(@NotNull String id) throws AccountNotExistsException {
        Optional<Account> optionalAccount = accountRepository.findById(id);

        if(optionalAccount.isEmpty()) throw new AccountNotExistsException("La cuenta con el id " + id + "no existe" );
        return optionalAccount.get();
    }

    private void sendRegistrationCode(String email, String code) {
        String subject = "Registration confirmation code";
        emailService.sendEmail(email, subject, code);
    }

    private void sendPasswordCodeEmail(String email, String code) {
        String subject = "Password update code";
        emailService.sendEmail(email, subject, code);
    }

    private AccountInfoDTO mapToInfoDTO(Account account){
        return new AccountInfoDTO(
                account.getUser().getCedula(),
                account.getUser().getName(),
                account.getUser().getPhone(),
                account.getUser().getAddress(),
                account.getEmail()
        );
    }

    private Account mapCreateDTOToEntity(CreateAccountDTO accountDTO){
        return Account.builder()
                .email(accountDTO.email())
                .password(passwordEncode(accountDTO.password()))
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
    }

    private String passwordEncode(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode( password );
    }

    private Map<String, Object> buildClaims(Account account) {
        return Map.of(
                "role", account.getRole(),
                "name", account.getUser().getName(),
                "id", account.getId()
        );
    }


}

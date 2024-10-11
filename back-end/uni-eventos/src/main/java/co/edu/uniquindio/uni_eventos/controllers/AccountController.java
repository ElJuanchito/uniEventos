package co.edu.uniquindio.uni_eventos.controllers;

import co.edu.uniquindio.uni_eventos.dtos.MessageDTO;
import co.edu.uniquindio.uni_eventos.dtos.account.AccountInfoDTO;
import co.edu.uniquindio.uni_eventos.dtos.account.CreateAccountDTO;
import co.edu.uniquindio.uni_eventos.dtos.account.UpdateAccountDTO;
import co.edu.uniquindio.uni_eventos.dtos.account.UpdatePasswordDTO;
import co.edu.uniquindio.uni_eventos.services.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create-account")
    public ResponseEntity<MessageDTO<String>> createAccount(@Valid @RequestBody CreateAccountDTO cuenta) throws Exception{
        accountService.createAccount(cuenta);
        return ResponseEntity.ok(new MessageDTO<>(false, "Cuenta creada exitosamente"));
    }

    @PutMapping("/validateAccount")
    @ResponseStatus(HttpStatus.OK)
    public MessageDTO<String> validateAccount(@RequestParam @NotBlank @Email String email, @RequestParam @NotBlank String validationCode) throws Exception{
        accountService.validateAccount(email, validationCode);
        return new MessageDTO<>(false, "Cuenta validada correctamente");
    }

    @PutMapping("/update-account")
    @ResponseStatus(HttpStatus.OK)
    public MessageDTO<String> updateAccount(@Valid @RequestBody UpdateAccountDTO accountDTO) throws Exception{
        accountService.updateAccount(accountDTO);
        return new MessageDTO<>(false, "Cuenta actualizada exitosamente");
    }

    @DeleteMapping("/delete-account/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MessageDTO<String> deleteAccount(@NotBlank @PathVariable String id) throws Exception{
        accountService.deleteAccount(id);
        return new MessageDTO<>(false, "Cuenta eliminada correctamente");
    }

    @GetMapping("/get/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MessageDTO<AccountInfoDTO> getAccountInfo(@NotBlank @PathVariable String id) throws Exception{
        AccountInfoDTO info = accountService.getAccount(id);
        return new MessageDTO<>(false, info);
    }

    @PutMapping("/send-password-code/{email}")
    @ResponseStatus(HttpStatus.OK)
    public MessageDTO<String> sendPasswordCode(@Email @PathVariable String email) throws Exception{
        accountService.sendPasswordCode(email);
        return new MessageDTO<>(false, "Password code sent");
    }

    @PutMapping("/send-password-code/{email}")
    @ResponseStatus(HttpStatus.OK)
    public MessageDTO<String> recoverPassword(@RequestBody @Valid UpdatePasswordDTO updatePasswordDTO) throws Exception{
        accountService.recoverPassword(updatePasswordDTO);
        return new MessageDTO<>(false, "Contrasena recuperada correctamente");
    }







}

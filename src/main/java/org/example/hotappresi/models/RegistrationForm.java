package org.example.hotappresi.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationForm {

    @NotBlank(message = "Nazwa użytkownika jest wymagana")
    private String username;

    @NotBlank(message = "Adres e-mail jest wymagany")
    @Email(message = "Podaj poprawny adres e-mail")
    private String email;

    @NotBlank(message = "Hasło jest wymagane")
    private String password;

    @NotBlank(message = "Potwierdzenie hasła jest wymagane")
    private String confirmPassword;

    // Dodatkowe dane osobowe –
    private String firstName;
    private String lastName;
    private String phoneNumber;
}

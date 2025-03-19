package org.example.hotappresi.controllers;

import jakarta.validation.Valid;
import org.example.hotappresi.models.RegistrationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrationController {

    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(InMemoryUserDetailsManager inMemoryUserDetailsManager, PasswordEncoder passwordEncoder) {
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new RegistrationForm());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid RegistrationForm form,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("error", "Hasła nie są zgodne");
            return "register";
        }
        if (inMemoryUserDetailsManager.userExists(form.getUsername())) {
            model.addAttribute("error", "Użytkownik o tej nazwie już istnieje");
            return "register";
        }

        // Używamy wstrzykniętego passwordEncoder do zakodowania hasła
        UserDetails user = User.builder()
                .passwordEncoder(passwordEncoder::encode)
                .username(form.getUsername())
                .password(form.getPassword())
                .roles("USER")
                .build();
        inMemoryUserDetailsManager.createUser(user);

        return "redirect:/login?registered=true";
    }
}

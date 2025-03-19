package org.example.hotappresi.controllers;
import jakarta.validation.Valid;
import org.example.hotappresi.models.AppUser;
import org.example.hotappresi.models.PasswordUpdateForm;
import org.example.hotappresi.services.HotelService;
import org.example.hotappresi.services.ReservationService;
import org.example.hotappresi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class ProfileController {

    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Autowired
    public ProfileController(InMemoryUserDetailsManager inMemoryUserDetailsManager,
                             PasswordEncoder passwordEncoder,
                             UserService userService) {
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);
        model.addAttribute("passwordUpdateForm", new PasswordUpdateForm());
        return "profile";  // Widok profile.html
    }


    @PostMapping("/profile/update")
    public String updatePassword(@ModelAttribute("passwordUpdateForm") @Valid PasswordUpdateForm form,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "profile";
        }
        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("error", "Nowe hasło i potwierdzenie nie są zgodne!");
            return "redirect:/profile";
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDetails user = inMemoryUserDetailsManager.loadUserByUsername(username);
        if (!passwordEncoder.matches(form.getOldPassword(), user.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Stare hasło jest nieprawidłowe!");
            return "redirect:/profile";
        }
        // Aktualizacja użytkownika z nowym zakodowanym hasłem
        UserDetails updatedUser = User.withUserDetails(user)
                .password(passwordEncoder.encode(form.getNewPassword()))
                .build();
        inMemoryUserDetailsManager.updateUser(updatedUser);
        redirectAttributes.addFlashAttribute("message", "Hasło zostało zmienione!");
        return "redirect:/profile";
    }
}

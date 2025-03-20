package org.example.hotappresi.services;

import org.example.hotappresi.models.AppUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final Map<String, AppUser> userStore = new HashMap<>();
    public void registerUser(AppUser newUser) {
        userStore.put(newUser.getUsername(), newUser);
    }
    public AppUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails ud = (UserDetails) principal;
            AppUser user = new AppUser();
            user.setUsername(ud.getUsername());
            // W przypadku InMemoryUserDetailsManager nie masz dodatkowych danych,
            // więc ustawiamy domyślne wartości.
            user.setEmail(ud.getUsername() + "@example.com");
            user.setFirstName("Brak");
            user.setLastName("Brak");
            user.setPhoneNumber("Brak");
            user.setProfileImageUrl("https://via.placeholder.com/150");
            return user;
        }
        return null;
    }
    public void updateUser(AppUser updatedUser) {
        // Założenie: username się nie zmienia, więc możemy nadpisać wpis
        userStore.put(updatedUser.getUsername(), updatedUser);
    }
}

package org.example.hotappresi.services;

import org.example.hotappresi.models.AppUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserService {

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
}

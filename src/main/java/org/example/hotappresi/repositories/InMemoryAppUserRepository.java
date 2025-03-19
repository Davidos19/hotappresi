package org.example.hotappresi.repositories;

import org.example.hotappresi.models.AppUser;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryAppUserRepository implements AppUserRepository {

    private final List<AppUser> users = new ArrayList<>();

    @Override
    public AppUser findByUsername(String username) {
        Optional<AppUser> user = users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();
        return user.orElse(null);
    }

    @Override
    public AppUser save(AppUser user) {
        // Możesz dodać logikę generowania ID, jeśli to potrzebne
        users.add(user);
        return user;
    }
}

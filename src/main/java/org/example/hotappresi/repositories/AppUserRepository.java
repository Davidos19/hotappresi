package org.example.hotappresi.repositories;

import org.example.hotappresi.models.AppUser;

public interface AppUserRepository {
    AppUser findByUsername(String username);
    AppUser save(AppUser user);
}

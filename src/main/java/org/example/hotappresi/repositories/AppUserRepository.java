package org.example.hotappresi.repositories;

import org.example.hotappresi.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface AppUserRepository {
    AppUser findByUsername(String username);
    AppUser save(AppUser user);
}

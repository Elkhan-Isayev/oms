package com.oms.auth.repository;

import com.oms.auth.model.Role;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class RoleRepository {
    
    private final AtomicLong idCounter = new AtomicLong(1);
    private final List<Role> roles = new ArrayList<>();

    @PostConstruct
    public void init() {
        // Initialize default roles
        if (roles.isEmpty()) {
            Role adminRole = new Role();
            adminRole.setId(idCounter.getAndIncrement());
            adminRole.setName("ADMIN");
            roles.add(adminRole);
            
            Role userRole = new Role();
            userRole.setId(idCounter.getAndIncrement());
            userRole.setName("USER");
            roles.add(userRole);
        }
    }

    public Role save(Role role) {
        if (role.getId() == null) {
            role.setId(idCounter.getAndIncrement());
        } else {
            roles.removeIf(existingRole -> existingRole.getId().equals(role.getId()));
        }
        roles.add(role);
        return role;
    }

    public Optional<Role> findById(Long id) {
        return roles.stream()
                .filter(role -> role.getId().equals(id))
                .findFirst();
    }

    public Optional<Role> findByName(String name) {
        return roles.stream()
                .filter(role -> role.getName().equals(name))
                .findFirst();
    }

    public List<Role> findAll() {
        return new ArrayList<>(roles);
    }

    public void deleteById(Long id) {
        roles.removeIf(role -> role.getId().equals(id));
    }
}

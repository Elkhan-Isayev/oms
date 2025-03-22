package com.oms.auth.repository;

import com.oms.auth.model.Permission;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PermissionRepository {
    
    private final AtomicLong idCounter = new AtomicLong(1);
    private final List<Permission> permissions = new ArrayList<>();

    @PostConstruct
    public void init() {
        // Initialize default permissions
        if (permissions.isEmpty()) {
            String[] defaultPermissions = {
                "READ_ORDERS", "WRITE_ORDERS", "READ_INVENTORY", "WRITE_INVENTORY", "MANAGE_USERS"
            };
            
            for (String permName : defaultPermissions) {
                Permission perm = new Permission();
                perm.setId(idCounter.getAndIncrement());
                perm.setName(permName);
                permissions.add(perm);
            }
        }
    }

    public Permission save(Permission permission) {
        if (permission.getId() == null) {
            permission.setId(idCounter.getAndIncrement());
        } else {
            permissions.removeIf(existingPermission -> existingPermission.getId().equals(permission.getId()));
        }
        permissions.add(permission);
        return permission;
    }

    public Optional<Permission> findById(Long id) {
        return permissions.stream()
                .filter(permission -> permission.getId().equals(id))
                .findFirst();
    }

    public Optional<Permission> findByName(String name) {
        return permissions.stream()
                .filter(permission -> permission.getName().equals(name))
                .findFirst();
    }

    public List<Permission> findAll() {
        return new ArrayList<>(permissions);
    }

    public void deleteById(Long id) {
        permissions.removeIf(permission -> permission.getId().equals(id));
    }
}

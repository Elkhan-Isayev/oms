package com.oms.auth.repository;

import com.oms.auth.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {
    
    private final AtomicLong idCounter = new AtomicLong(1);
    private final List<User> users = new ArrayList<>();

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idCounter.getAndIncrement());
        } else {
            users.removeIf(existingUser -> existingUser.getId().equals(user.getId()));
        }
        users.add(user);
        return user;
    }

    public Optional<User> findById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    public Optional<User> findByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    public void deleteById(Long id) {
        users.removeIf(user -> user.getId().equals(id));
    }
}

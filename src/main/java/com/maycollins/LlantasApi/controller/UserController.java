package com.maycollins.LlantasApi.controller;

import com.maycollins.LlantasApi.model.UserAccount;
import com.maycollins.LlantasApi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Obtener todos los usuarios
    @GetMapping
    public List<UserAccount> getAllUsers() {
        return userService.getAllUsers();
    }

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UserAccount> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear nuevo usuario
    @PostMapping
    public UserAccount createUser(@RequestBody UserAccount user) {
        return userService.createUser(user);
    }

    // Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<UserAccount> updateUser(@PathVariable Integer id, @RequestBody UserAccount user) {
        return userService.getUserById(id)
                .map(existingUser -> {
                    user.setUserId(id);
                    return ResponseEntity.ok(userService.updateUser(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        return userService.getUserById(id)
                .map(user -> {
                    userService.deleteUser(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<UserAccount> login(@RequestParam String email, @RequestParam String password) {
        return userService.login(email, password)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    // Bloquear usuario
    @PutMapping("/{id}/block")
    public ResponseEntity<UserAccount> blockUser(@PathVariable Integer id) {
        try {
            UserAccount blockedUser = userService.blockUser(id);
            return ResponseEntity.ok(blockedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar perfil
    @PutMapping("/{id}/profile")
    public ResponseEntity<UserAccount> updateProfile(@PathVariable Integer id, @RequestBody UserAccount user) {
        try {
            UserAccount updatedUser = userService.updateProfile(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
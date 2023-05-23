package com.app.data;

import com.app.entities.Role;
import com.app.entities.User;
import com.app.repositories.RoleRepository;
import com.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SeedData implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        seedUserAndRoles();
    }

    public void seedUserAndRoles() {
        Role roleUser = seedRole("USER"); // id: 1
        Role roleAdmin = seedRole("ADMIN"); // id: 2

        User adminUser = seedUser(
                "s.ishimwegabin@gmail.com",
                "#Password123",
                "+250787857036",
                List.of(roleUser, roleAdmin)
        );
    }

    public User seedUser(String email, String password, String contact, List<Role> roles) {
        return userRepository.save(
               User.builder()
                       .email(email)
                       .password(passwordEncoder.encode(password))
                       .contactNumber(contact)
                       .isEnabled(true)
                       .mfaEnabled(false)
                       .roles(roles)
                       .build()
        );
    }

    public Role seedRole(String name) {
        return roleRepository.save(
                Role.builder()
                        .name(name)
                        .build()
        );
    }
}

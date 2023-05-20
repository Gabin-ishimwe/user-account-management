package com.app.data;

import com.app.entities.Role;
import com.app.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SeedData implements CommandLineRunner {

    private final RoleRepository roleRepository;
    @Override
    public void run(String... args) throws Exception {
        seedRoles();
    }

    public void seedRoles() {
        Role roleUser = Role.builder() // id: 1
                .name("USER")
                .build();

        Role roleAdmin = Role.builder() // id: 2
                .name("ADMIN")
                .build();

        roleRepository.saveAll(List.of(roleUser, roleAdmin));
    }
}

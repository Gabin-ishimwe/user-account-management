package com.app.services;

import com.app.dto.ResponseData;
import com.app.entities.Role;
import com.app.entities.User;
import com.app.exceptions.NotFoundException;
import com.app.repositories.RoleRepository;
import com.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public ResponseData addRolePermission(UUID userId, Long roleId) throws NotFoundException {
        Role findRole = roleRepository.findById(roleId).orElseThrow(() -> new NotFoundException("Role Not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User Not found"));
        List<Role> roles = user.getRoles();
        if (roles == null) {
            user.setRoles(List.of(findRole));
            userRepository.save(user);
            return ResponseData.builder()
                    .message("User role added " + findRole.getName())
                    .data(user)
                    .build();
        }
        for (Role role : roles) {
            if (Objects.equals(role.getName(), findRole.getName())) {
                return ResponseData.builder()
                        .message("User already has role " + role.getName())
                        .build();
            }
        }
        List<Role> addingRole = new ArrayList<>(roles);
        addingRole.add(findRole);
        user.setRoles(addingRole);
        userRepository.save(user);
        System.out.println(user);
        return ResponseData.builder()
                .message("User role added " + findRole.getName())
                .data(user)
                .build();
    }

    public ResponseData removeRolePermission(UUID userId, Long roleId) throws NotFoundException {
        Role findRole = roleRepository.findById(roleId).orElseThrow(() -> new NotFoundException("Role Not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User Not found"));
        List<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (Objects.equals(role.getName(), findRole.getName())) {
                user.getRoles().remove(findRole);
                userRepository.save(user);
                return ResponseData.builder()
                        .message("User role removed " + findRole.getName())
                        .data(user)
                        .build();
            }
        }

        return ResponseData.builder()
                .message("User role doesn't exist " + findRole.getName())
                .data(user)
                .build();
    }

    public ResponseData getRoles() {
        return ResponseData.builder()
                .message("All roles")
                .data(roleRepository.findAll())
                .build();
    }
}

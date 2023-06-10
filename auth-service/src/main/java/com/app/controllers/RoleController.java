package com.app.controllers;

import com.app.dto.ResponseData;
import com.app.exceptions.NotFoundException;
import com.app.services.RoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "BearerAuth")
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseData assignRole(@RequestParam("userId") @Valid @org.hibernate.validator.constraints.UUID UUID userId, @RequestParam("roleId") Long roleId) throws NotFoundException {
        return roleService.addRolePermission(userId, roleId);
    }

    @PutMapping
    public ResponseData removeRole(@RequestParam("userId") @Valid @org.hibernate.validator.constraints.UUID UUID userId, @RequestParam("roleId") Long roleId) throws NotFoundException {
        return roleService.removeRolePermission(userId, roleId);
    }

    @GetMapping
    public ResponseData getRoles() {
        return roleService.getRoles();
    }
}

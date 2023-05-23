package com.app.controllers;

import com.app.dto.AccountDto;
import com.app.dto.ResponseData;
import com.app.entities.Account;
import com.app.exceptions.NotFoundException;
import com.app.services.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "/profile")
    public ResponseData updateUserAccount(@ModelAttribute @Valid AccountDto accountDto, @RequestHeader("userId") UUID userId) throws NotFoundException, IOException {
        return accountService.updateUserAccount(accountDto, userId);
    }

    @GetMapping("/profile")
    public Account getUserAccount(@RequestHeader("userId") UUID userId) throws NotFoundException {
        return accountService.getUserAccount(userId);
    }

    // TODO: authorize for specified user roles (ADMIN)
    @PutMapping(path = "/verification")
    public ResponseData changeAccountStatus(@RequestParam(value = "accountId") UUID accountId, @RequestParam("status") String status) throws NotFoundException {
        return accountService.updateAccountStatus(accountId, status);
    }

    // TODO: authorize for specified user roles (ADMIN)
    @GetMapping
    public ResponseData getAllAccounts() {
        return accountService.getAllAccounts();
    }
}

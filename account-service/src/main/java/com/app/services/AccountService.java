package com.app.services;

import com.app.dto.AccountDto;
import com.app.dto.AccountVerification;
import com.app.dto.ResponseData;
import com.app.entities.Account;
import com.app.entities.AccountStatus;
import com.app.exceptions.NotFoundException;
import com.app.repositories.AccountRepository;
import com.app.utils.CloudinaryUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CloudinaryUtil cloudinaryUtil;

    private final KafkaTemplate<String, AccountVerification> kafkaTemplate;


    public Account getUserAccount(UUID userId) throws NotFoundException {
        return accountRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("Account not found"));
    }

    public ResponseData updateUserAccount(AccountDto accountDto, UUID userId) throws NotFoundException, IOException {
        Account userAccount = accountRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("Account not found"));
        MultipartFile document = accountDto.getDocument();
        String documentUrl = null;
        if (document != null && !document.isEmpty()) {
            documentUrl = cloudinaryUtil.uploadImage(document);
        }
        userAccount.setDocument(documentUrl);
        if(accountDto.getPassportNumber() != null) {
            userAccount.setPassportNumber(accountDto.getPassportNumber());
        }
        if(accountDto.getNationalId() != null) {
            userAccount.setNationalId(accountDto.getNationalId());
        }
        Account savedAccount = accountRepository.save(userAccount);
        return ResponseData.builder()
                .message("Account updated")
                .data(savedAccount)
                .build();
    }

    public ResponseData updateAccountStatus(UUID accountId, String status) throws NotFoundException {
        Account findAccount = accountRepository.findById(accountId).orElseThrow(() -> new NotFoundException("User account not found"));
        findAccount.setStatus(AccountStatus.valueOf(status));
        if(Objects.equals(status, "VERIFIED")) findAccount.setVerified(true);
        Account updatedAccount = accountRepository.save(findAccount);

        // TODO: send kafka topic to the user-service to send email
        AccountVerification event = AccountVerification.builder()
                .userId(findAccount.getUserId())
                .status(String.valueOf(status))
                .build();
        kafkaTemplate.send("account-verified", event);

        return ResponseData.builder()
                .message("Account status updated")
                .data(updatedAccount)
                .build();
    }

    public ResponseData getAllAccounts() {
        List<Account> allAccounts = accountRepository.findAll();
        return ResponseData.builder()
                .message("All user accounts")
                .data(allAccounts)
                .build();
    }
}

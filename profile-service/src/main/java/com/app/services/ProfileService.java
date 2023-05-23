package com.app.services;

import com.app.dto.ProfileDto;
import com.app.dto.ResponseData;
import com.app.entities.Gender;
import com.app.entities.MaritalStatus;
import com.app.entities.Profile;
import com.app.exceptions.NotFoundException;
import com.app.repositories.ProfileRepository;
import com.app.utils.CloudinaryUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final CloudinaryUtil cloudinaryUtil;
    private final ProfileRepository profileRepository;

    public ResponseData updateProfile(ProfileDto profileDto, UUID profileId) throws NotFoundException, IOException {
        Profile findProfile = profileRepository.findByUserId(profileId)
                .orElseThrow(() -> new NotFoundException("Profile not found"));

        MultipartFile profilePhoto = profileDto.getProfilePhoto();
        String imageUrl = null;
        if (profilePhoto != null && !profilePhoto.isEmpty() && Objects.requireNonNull(profilePhoto.getContentType()).toLowerCase().startsWith("image")) {
            imageUrl = cloudinaryUtil.uploadImage(profilePhoto);
        }

        if(profileDto.getFirstName() != null) {
            findProfile.setFirstName(profileDto.getFirstName());
        }
        if(profileDto.getLastName() != null) {
            findProfile.setLastName(profileDto.getLastName());
        }
        if(profileDto.getGender() != null) {
            findProfile.setGender(Gender.valueOf(profileDto.getGender()));
        }
        if(profileDto.getMaritalStatus() != null) {
            findProfile.setMaritalStatus(MaritalStatus.valueOf(profileDto.getMaritalStatus()));
        }
        if(profileDto.getProfilePhoto() != null) {
            findProfile.setProfilePhoto(Objects.nonNull(imageUrl) ? imageUrl : findProfile.getProfilePhoto());
        }
        if(profileDto.getNationality() != null) {
            findProfile.setNationality(profileDto.getNationality());
        }
        if(profileDto.getDateOfBirth() != null) {
            // Define a formatter for the date format you want to use
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate change = LocalDate.parse(profileDto.getDateOfBirth(), formatter);
            LocalDateTime birthDate = change.atStartOfDay();

            // calculate age
            LocalDateTime now = LocalDateTime.now();
            Period period = Period.between(birthDate.toLocalDate(), now.toLocalDate());
            int age = period.getYears();
            findProfile.setAge(age);
            findProfile.setDateOfBirth(birthDate);
        }

        Profile updatedProfile = profileRepository.save(findProfile);
        return ResponseData.builder()
                .message("Profile updated")
                .data(updatedProfile)
                .build();
    }

    public ResponseData getUserProfile(UUID profileId) throws NotFoundException {
        Profile userProfile = profileRepository.findByUserId(profileId)
                .orElseThrow(() -> new NotFoundException("Profile not found"));
        return ResponseData.builder()
                .data(userProfile)
                .build();
    }

    public ResponseData getAllProfile() {
        List<Profile> allProfiles = profileRepository.findAll();
        return ResponseData.builder()
                .message("All profiles")
                .data(allProfiles)
                .build();
    }
}

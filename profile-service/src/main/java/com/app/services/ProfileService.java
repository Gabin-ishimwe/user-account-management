package com.app.services;

import com.app.config.CloudinaryConfig;
import com.app.dto.ProfileDto;
import com.app.entities.Gender;
import com.app.entities.MaritalStatus;
import com.app.entities.Profile;
import com.app.exceptions.NotFoundException;
import com.app.repositories.ProfileRepository;
import com.app.utils.CloudinaryUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final CloudinaryUtil cloudinaryUtil;
    private final ProfileRepository profileRepository;

    public Profile updateProfile(ProfileDto profileDto, UUID profileId) throws NotFoundException, IOException {
        Profile findProfile = profileRepository.findByUserId(profileId)
                .orElseThrow(() -> new NotFoundException("Profile not found"));

        MultipartFile profilePhoto = profileDto.getProfilePhoto();
        String imageUrl = null;
        if (!profilePhoto.isEmpty() && Objects.requireNonNull(profilePhoto.getContentType()).toLowerCase().startsWith("image")) {
            imageUrl = cloudinaryUtil.uploadImage(profilePhoto);
        }
        // Define a formatter for the date format you want to use
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println(formatter);
        LocalDateTime birthDate = LocalDateTime.parse(profileDto.getDateOfBirth(), formatter);
        System.out.println(birthDate);
        // calculate age
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        System.out.println(now.toLocalDate());
        System.out.println(birthDate.toLocalDate());
        Period period = Period.between(birthDate.toLocalDate(), now.toLocalDate());
        int age = period.getYears();

        findProfile.setFirstName(profileDto.getFirstName());
        findProfile.setLastName(profileDto.getLastName());
        findProfile.setAge(age);
        findProfile.setProfilePhoto(Objects.nonNull(imageUrl) ? imageUrl : findProfile.getProfilePhoto());
        findProfile.setGender(Gender.valueOf(profileDto.getGender()));
        findProfile.setMaritalStatus(MaritalStatus.valueOf(profileDto.getMaritalStatus()));
        findProfile.setDateOfBirth(birthDate);


        return profileRepository.save(findProfile);
    }

    public Profile getUserProfile(UUID profileId) throws NotFoundException {
        return profileRepository.findByUserId(profileId)
                .orElseThrow(() -> new NotFoundException("Profile not found"));
    }
}

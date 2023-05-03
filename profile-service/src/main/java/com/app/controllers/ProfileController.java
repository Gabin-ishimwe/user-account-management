package com.app.controllers;

import com.app.dto.ProfileDto;
import com.app.entities.Profile;
import com.app.exceptions.NotFoundException;
import com.app.services.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PutMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public Profile updateProfile(@ModelAttribute @Valid ProfileDto profileDto, @RequestHeader("userId") UUID profileId) throws NotFoundException, IOException {
        return profileService.updateProfile(profileDto, profileId);
    }

    @GetMapping()
    public Profile getUserProfile(@RequestHeader("userId") UUID profileId) throws NotFoundException {
        return profileService.getUserProfile(profileId);
    }
}

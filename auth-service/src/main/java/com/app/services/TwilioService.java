package com.app.services;

import com.app.config.TwilioConfig;
import com.app.dto.OtpRequestDto;
import com.app.entities.Otp;
import com.app.entities.User;
import com.app.exceptions.NotFoundException;
import com.app.exceptions.UserAuthException;
import com.app.repositories.OtpRepository;
import com.twilio.rest.api.v2010.account.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TwilioService {

    private final OtpRepository otpRepository;
    private final TwilioConfig twilioConfig;
    public String sendOtpPasssword(OtpRequestDto otpRequestDto, User user ){
        String otp = generateOtp();
        String msg = "Dear customer, Your OTP is ##" + otp + "##. use it to complete your sign in process";
        try {
            Message message = Message.creator(
                            new com.twilio.type.PhoneNumber(otpRequestDto.getPhoneNumber()),
                            new com.twilio.type.PhoneNumber(twilioConfig.getTrialNumber()),
                            msg)
                    .create();

            // save otp database

            Otp userOtp = new Otp(otp, user);
            otpRepository.save(userOtp);
            return "Otp sent to mobile device";
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public Otp validateOtp(String otp) throws NotFoundException, UserAuthException {
        Otp findOtp = otpRepository.findByToken(otp);
        if(findOtp == null) throw new NotFoundException("Otp token not found");
        if(findOtp.getExpirationTime().isBefore(LocalDateTime.now())) {
            otpRepository.delete(findOtp);
            throw new UserAuthException("Token already expired");
        }
        return findOtp;

    }

    private String generateOtp() {
        return new DecimalFormat("000000")
                .format(new Random().nextInt(999999));
    }
}

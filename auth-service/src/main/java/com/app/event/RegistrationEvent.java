package com.app.event;


import com.app.dto.AuthResponseDto;
import com.app.entities.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class RegistrationEvent extends ApplicationEvent {
    private final AuthResponseDto responseDto;
    private final String applicationUrl;
    public RegistrationEvent(AuthResponseDto responseDto, String applicationUrl) {
        super(responseDto);
        this.responseDto = responseDto;
        this.applicationUrl = applicationUrl;
    }
}

package com.app.event.resetPassword;

import com.app.entities.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ResetPasswordEvent extends ApplicationEvent {

    private final User user;
    private final String url;
    public ResetPasswordEvent(User user, String url) {
        super(user);
        this.user = user;
        this.url = url;
    }
}

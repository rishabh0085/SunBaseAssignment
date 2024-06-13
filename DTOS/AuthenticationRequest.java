package SunBaseDrive4.demo.DTOS;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequest {
    private String login_id;
    private String password;
}

package SunBaseDrive4.demo.DTOS;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoteCustomer {
    private String uuid;
    private String first_name;
    private String last_name;
    private String street;
    private String address;
    private String city;
    private String state;
    private String email;
    private String phone;
}

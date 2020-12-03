package com.userManagement.authservice.dto.request;

import com.userManagement.authservice.validation.ValidEmail;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRequest implements Serializable {

    static final long serialVersionUID = 1L;

    @ValidEmail
    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    private String date;

    private String phone;
}

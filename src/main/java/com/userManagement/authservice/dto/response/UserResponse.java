package com.userManagement.authservice.dto.response;

 import com.userManagement.authservice.validation.ValidEmail;
import lombok.*;

 import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponse implements Serializable {

    static final long serialVersionUID = 1L;

    @NotNull
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    @ValidEmail
    @NotNull
    private String email;

    @NotNull
    private String password;

    private String phone;

    private String date;




}

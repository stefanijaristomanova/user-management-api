package com.userManagement.authservice.dto.request;

 import lombok.*;

  import javax.validation.constraints.NotNull;
 import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    @NotNull
    private String email;

    @NotNull
    private String password;

}

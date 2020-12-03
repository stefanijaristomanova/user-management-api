package com.userManagement.authservice.dto.response;

import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;

    private String jwt;

}

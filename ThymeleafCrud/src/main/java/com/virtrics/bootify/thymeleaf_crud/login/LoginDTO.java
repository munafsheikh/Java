package com.virtrics.bootify.thymeleaf_crud.login;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LoginDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    @LoginUsernameUnique
    private String username;

    @Size(max = 255)
    private String email;

    @NotNull
    @Size(max = 255)
    private String passwordHash;

    @NotNull
    private Role role;

    private List<Long> person;

}

package com.virtrics.bootify.thymeleaf_crud.person;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PersonDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String firstname;

    @Size(max = 255)
    private String lastnames;

    @Size(max = 255)
    private String nickname;

    @Size(max = 255)
    private String gender;

    @NotNull
    private LocalDate dateOfBirth;

}

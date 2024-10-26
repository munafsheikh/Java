package com.virtrics.bootify.thymeleaf_crud.person;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/people", produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonResource {

    private final PersonService personService;

    public PersonResource(final PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<List<PersonDTO>> getAllPeople() {
        return ResponseEntity.ok(personService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> getPerson(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(personService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createPerson(@RequestBody @Valid final PersonDTO personDTO) {
        final Long createdId = personService.create(personDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePerson(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final PersonDTO personDTO) {
        personService.update(id, personDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePerson(@PathVariable(name = "id") final Long id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

package com.virtrics.bootify.thymeleaf_crud.login;

import com.virtrics.bootify.thymeleaf_crud.person.Person;
import com.virtrics.bootify.thymeleaf_crud.person.PersonRepository;
import com.virtrics.bootify.thymeleaf_crud.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class LoginService {

    private final LoginRepository loginRepository;
    private final PersonRepository personRepository;

    public LoginService(final LoginRepository loginRepository,
            final PersonRepository personRepository) {
        this.loginRepository = loginRepository;
        this.personRepository = personRepository;
    }

    public List<LoginDTO> findAll() {
        final List<Login> logins = loginRepository.findAll(Sort.by("id"));
        return logins.stream()
                .map(login -> mapToDTO(login, new LoginDTO()))
                .toList();
    }

    public LoginDTO get(final Long id) {
        return loginRepository.findById(id)
                .map(login -> mapToDTO(login, new LoginDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final LoginDTO loginDTO) {
        final Login login = new Login();
        mapToEntity(loginDTO, login);
        return loginRepository.save(login).getId();
    }

    public void update(final Long id, final LoginDTO loginDTO) {
        final Login login = loginRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(loginDTO, login);
        loginRepository.save(login);
    }

    public void delete(final Long id) {
        loginRepository.deleteById(id);
    }

    private LoginDTO mapToDTO(final Login login, final LoginDTO loginDTO) {
        loginDTO.setId(login.getId());
        loginDTO.setUsername(login.getUsername());
        loginDTO.setEmail(login.getEmail());
        loginDTO.setPasswordHash(login.getPasswordHash());
        loginDTO.setRole(login.getRole());
        loginDTO.setPerson(login.getPerson().stream()
                .map(person -> person.getId())
                .toList());
        return loginDTO;
    }

    private Login mapToEntity(final LoginDTO loginDTO, final Login login) {
        login.setUsername(loginDTO.getUsername());
        login.setEmail(loginDTO.getEmail());
        login.setPasswordHash(loginDTO.getPasswordHash());
        login.setRole(loginDTO.getRole());
        final List<Person> person = personRepository.findAllById(
                loginDTO.getPerson() == null ? Collections.emptyList() : loginDTO.getPerson());
        if (person.size() != (loginDTO.getPerson() == null ? 0 : loginDTO.getPerson().size())) {
            throw new NotFoundException("one of person not found");
        }
        login.setPerson(new HashSet<>(person));
        return login;
    }

    public boolean usernameExists(final String username) {
        return loginRepository.existsByUsernameIgnoreCase(username);
    }

}

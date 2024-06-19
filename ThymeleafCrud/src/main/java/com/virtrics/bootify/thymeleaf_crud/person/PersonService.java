package com.virtrics.bootify.thymeleaf_crud.person;

import com.virtrics.bootify.thymeleaf_crud.login.LoginRepository;
import com.virtrics.bootify.thymeleaf_crud.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class PersonService {

    private final PersonRepository personRepository;
    private final LoginRepository loginRepository;

    public PersonService(final PersonRepository personRepository,
            final LoginRepository loginRepository) {
        this.personRepository = personRepository;
        this.loginRepository = loginRepository;
    }

    public List<PersonDTO> findAll() {
        final List<Person> persons = personRepository.findAll(Sort.by("id"));
        return persons.stream()
                .map(person -> mapToDTO(person, new PersonDTO()))
                .toList();
    }

    public PersonDTO get(final Long id) {
        return personRepository.findById(id)
                .map(person -> mapToDTO(person, new PersonDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PersonDTO personDTO) {
        final Person person = new Person();
        mapToEntity(personDTO, person);
        return personRepository.save(person).getId();
    }

    public void update(final Long id, final PersonDTO personDTO) {
        final Person person = personRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(personDTO, person);
        personRepository.save(person);
    }

    public void delete(final Long id) {
        final Person person = personRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        loginRepository.findAllByPerson(person)
                .forEach(login -> login.getPerson().remove(person));
        personRepository.delete(person);
    }

    private PersonDTO mapToDTO(final Person person, final PersonDTO personDTO) {
        personDTO.setId(person.getId());
        personDTO.setFirstname(person.getFirstname());
        personDTO.setLastnames(person.getLastnames());
        personDTO.setNickname(person.getNickname());
        personDTO.setGender(person.getGender());
        personDTO.setDateOfBirth(person.getDateOfBirth());
        return personDTO;
    }

    private Person mapToEntity(final PersonDTO personDTO, final Person person) {
        person.setFirstname(personDTO.getFirstname());
        person.setLastnames(personDTO.getLastnames());
        person.setNickname(personDTO.getNickname());
        person.setGender(personDTO.getGender());
        person.setDateOfBirth(personDTO.getDateOfBirth());
        return person;
    }

}

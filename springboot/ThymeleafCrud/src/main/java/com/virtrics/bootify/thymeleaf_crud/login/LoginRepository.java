package com.virtrics.bootify.thymeleaf_crud.login;

import com.virtrics.bootify.thymeleaf_crud.person.Person;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LoginRepository extends JpaRepository<Login, Long> {

    Login findFirstByPerson(Person person);

    List<Login> findAllByPerson(Person person);

    boolean existsByUsernameIgnoreCase(String username);

}

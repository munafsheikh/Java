package com.virtrics.bootify.thymeleaf_crud.login;

import com.virtrics.bootify.thymeleaf_crud.person.Person;
import com.virtrics.bootify.thymeleaf_crud.person.PersonRepository;
import com.virtrics.bootify.thymeleaf_crud.util.CustomCollectors;
import com.virtrics.bootify.thymeleaf_crud.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/logins")
public class LoginController {

    private final LoginService loginService;
    private final PersonRepository personRepository;

    public LoginController(final LoginService loginService,
            final PersonRepository personRepository) {
        this.loginService = loginService;
        this.personRepository = personRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("roleValues", Role.values());
        model.addAttribute("personValues", personRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Person::getId, Person::getFirstname)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("logins", loginService.findAll());
        return "login/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("login") final LoginDTO loginDTO) {
        return "login/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("login") @Valid final LoginDTO loginDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "login/add";
        }
        loginService.create(loginDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("login.create.success"));
        return "redirect:/logins";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("login", loginService.get(id));
        return "login/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("login") @Valid final LoginDTO loginDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "login/edit";
        }
        loginService.update(id, loginDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("login.update.success"));
        return "redirect:/logins";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        loginService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("login.delete.success"));
        return "redirect:/logins";
    }

}

package com.virtrics.bootify.thymeleaf_crud.login;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Validate that the username value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = LoginUsernameUnique.LoginUsernameUniqueValidator.class
)
public @interface LoginUsernameUnique {

    String message() default "{Exists.login.username}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class LoginUsernameUniqueValidator implements ConstraintValidator<LoginUsernameUnique, String> {

        private final LoginService loginService;
        private final HttpServletRequest request;

        public LoginUsernameUniqueValidator(final LoginService loginService,
                final HttpServletRequest request) {
            this.loginService = loginService;
            this.request = request;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equalsIgnoreCase(loginService.get(Long.parseLong(currentId)).getUsername())) {
                // value hasn't changed
                return true;
            }
            return !loginService.usernameExists(value);
        }

    }

}

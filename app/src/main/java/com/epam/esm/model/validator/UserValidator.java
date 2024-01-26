package com.epam.esm.model.validator;

import com.epam.esm.entity.User;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UserValidator implements Validator {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        Long id = user.getId();
        if (id != null) {
            if (id < 1) {
                errors.rejectValue("id", "id.not_positive");
            }
        }
        String email = user.getEmail();
        if (email != null) {
            ValidationUtils.rejectIfEmpty(errors, "email", "user.email.empty");
            if (email.length() > 100) {
                errors.rejectValue("email", "user.email.too_long");
            }
            if (!email.matches(EMAIL_REGEX)) {
                errors.rejectValue("email", "user.email.invalid");
            }
        }
        String name = user.getName();
        if (name != null) {
            ValidationUtils.rejectIfEmpty(errors, "name", "user.name.empty");
            if (name.length() > 100) {
                errors.rejectValue("name", "user.name.too_long");
            }
        }
        String surname = user.getSurname();
        if (surname != null) {
            ValidationUtils.rejectIfEmpty(errors, "surname", "user.surname.empty");
            if (surname.length() > 100) {
                errors.rejectValue("surname", "user.surname.too_long");
            }
        }
        String password = user.getPassword();
        if (password != null) {
            ValidationUtils.rejectIfEmpty(errors, "password", "user.password.empty");
            if (password.length() > 100) {
                errors.rejectValue("password", "user.password.too_long");
            }
        }
    }
}

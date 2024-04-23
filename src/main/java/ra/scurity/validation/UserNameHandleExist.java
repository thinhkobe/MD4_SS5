package ra.scurity.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ra.scurity.repository.IUserRepository;

@Component
public class UserNameHandleExist implements ConstraintValidator<UserNameExist,String> {
    @Autowired
    private IUserRepository userRepository;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return !userRepository.existsByEmail(s);
    }

    @Override
    public void initialize(UserNameExist constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}


package com.hasim.healthboard.api.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

import com.hasim.healthboard.api.exception.MaxValueLengthException;
import com.hasim.healthboard.api.exception.MinValueLengthException;
import com.hasim.healthboard.api.exception.MissingValueException;



/**
 * Class to validate constraint violation on {@link Size} annotation
 * 
 * @author avisaraf
 *
 */
public class SizeValidator implements ConstraintValidator<Size, String> {

    private Size size;

    @Override
    public void initialize(Size constraintAnnotation) {
        this.size = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (size.min() > 0) {
            if (StringUtils.isEmpty(value)) {
                throw new MissingValueException(
                    size.name());
            }
            if (value.trim().length() < size.min()) {
                throw new MinValueLengthException(
                    size.min(),
                    size.name());
            }
        }
        if (size.max() > 0) {
            if (StringUtils.isEmpty(value) && (value.trim().length() > size.max())) {
                throw new MaxValueLengthException(
                    size.max(),
                    size.name());
            }
        }
        return true;
    }

}

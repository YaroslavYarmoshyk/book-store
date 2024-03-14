package com.online.bookstore.config.validator;

import com.online.bookstore.annotation.FieldMatch;
import com.online.bookstore.exception.SystemException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.RecordComponent;
import java.util.Objects;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.http.HttpStatus;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(final FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        try {
            if (value.getClass().isRecord()) {
                final Object firstObj = getRecordComponentValue(value, firstFieldName);
                final Object secondObj = getRecordComponentValue(value, secondFieldName);
                return Objects.equals(firstObj, secondObj);
            }
            final Object firstObj = BeanUtils.getProperty(value, firstFieldName);
            final Object secondObj = BeanUtils.getProperty(value, secondFieldName);
            return Objects.equals(firstObj, secondObj);
        } catch (final Exception exception) {
            throw new SystemException("An error occurred while validating the fields",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Object getRecordComponentValue(final Object record,
                                           final String fieldName) throws Exception {
        final RecordComponent[] components = record.getClass().getRecordComponents();
        for (final RecordComponent component : components) {
            if (component.getName().equals(fieldName)) {
                return component.getAccessor().invoke(record);
            }
        }
        throw new SystemException("No such field: " + fieldName, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

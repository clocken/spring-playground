/*
 * Copyright 2021 clocken
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.clocken.springjdbcandvalidation.domain.validation;

import com.github.clocken.springjdbcandvalidation.domain.ReferencedDomainObject;
import com.github.clocken.springjdbcandvalidation.repository.ReferencedDomainObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Custom validator for a foreign key constraint.
 * See: https://docs.jboss.org/hibernate/validator/6.1/reference/en-US/html_single/#validator-customconstraints-simple
 */
public class ValidForeignKeyValidator implements ConstraintValidator<ValidForeignKey, Long> {

    private final ReferencedDomainObjectRepository referencedDomainObjectRepository;
    private List<Long> referencedDomainObjectKeys;

    /**
     * Dependency injection constructor.
     *
     * @param referencedDomainObjectRepository The {@link ReferencedDomainObjectRepository} to be injected.
     */
    @Autowired
    public ValidForeignKeyValidator(ReferencedDomainObjectRepository referencedDomainObjectRepository) {
        this.referencedDomainObjectRepository = referencedDomainObjectRepository;
    }

    @Override
    public void initialize(ValidForeignKey constraintAnnotation) {
        List<ReferencedDomainObject> parentDomainObjects = new ArrayList<>();
        referencedDomainObjectRepository.findAll().forEach(parentDomainObjects::add);

        referencedDomainObjectKeys = parentDomainObjects.stream()
                .map(ReferencedDomainObject::getId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return referencedDomainObjectKeys.contains(value);
    }
}

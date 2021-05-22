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

package com.github.clocken.springjdbcandvalidation.web.rest;

import com.github.clocken.springjdbcandvalidation.domain.DomainObject;
import com.github.clocken.springjdbcandvalidation.domain.validation.ForeignKeyCheck;
import com.github.clocken.springjdbcandvalidation.repository.DomainObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.*;

/**
 * {@link RestController} for {@link DomainObject}'s.
 */
@RestController
@RequestMapping("/api/v0/domain-object")
public class DomainObjectResource {

    private final DomainObjectRepository domainObjectRepository;
    private final Validator validator;

    /**
     * Dependency injection constructor.
     *
     * @param domainObjectRepository The {@link DomainObjectRepository} to be injected.
     * @param validator              The {@link Validator} to be injected.
     */
    @Autowired
    public DomainObjectResource(DomainObjectRepository domainObjectRepository, Validator validator) {
        this.domainObjectRepository = domainObjectRepository;
        this.validator = validator;
    }

    /**
     * Handling method for PUT requests containing a JSON array of {@link DomainObject}'s.
     * Uses javax.validation.{@link Validator} to validate every element of the given array and only merges valid
     * ones into the DB.
     *
     * @param domainObjects The JSON array of {@link DomainObject}'s mapped as a {@link List}
     * @return A {@link ResponseEntity} to be returned to the client
     */
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createOrUpdateDomainObjects(@RequestBody List<DomainObject> domainObjects) {
        final Map<Integer, String> errors409 = new HashMap<>();
        final Map<Integer, String> errors500 = new HashMap<>();
        final List<DomainObject> validatedDomainObjects = new ArrayList<>();

        int index = 0;
        for (DomainObject domainObject : domainObjects) {
            Set<ConstraintViolation<DomainObject>> semanticConstraintViolations = validator.validate(domainObject);
            Set<ConstraintViolation<DomainObject>> foreignKeyConstraintViolations = validator.validate(domainObject, ForeignKeyCheck.class);

            if (!semanticConstraintViolations.isEmpty()) {
                errors409.put(index, new ConstraintViolationException(semanticConstraintViolations).getLocalizedMessage());
            }
            if (!foreignKeyConstraintViolations.isEmpty()) {
                errors500.put(index, new ConstraintViolationException(foreignKeyConstraintViolations).getLocalizedMessage());
            }
            if (semanticConstraintViolations.isEmpty() && foreignKeyConstraintViolations.isEmpty()) {
                validatedDomainObjects.add(domainObject);
            }

            index++;
        }

        domainObjectRepository.saveAll(validatedDomainObjects);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidForeignKeyValidatorTest {

    @Mock
    ReferencedDomainObjectRepository parentDomainObjectRepository;

    ValidForeignKeyValidator isValidForeignKeyValidator;

    @BeforeEach
    void setUpTest() {
        when(parentDomainObjectRepository.findAll())
                .thenReturn(Arrays.asList(
                        ReferencedDomainObject.of(1L),
                        ReferencedDomainObject.of(2L),
                        ReferencedDomainObject.of(3L)
                ));

        isValidForeignKeyValidator = new ValidForeignKeyValidator(parentDomainObjectRepository);
        isValidForeignKeyValidator.initialize(null);
    }

    @Test
    void should_validate() {
        assertThat(isValidForeignKeyValidator.isValid(1L, null))
                .isTrue();
    }
}

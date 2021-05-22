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

package com.github.clocken.springjdbcandvalidation.repository;

import com.github.clocken.springjdbcandvalidation.domain.DomainObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(DomainObjectRepositoryImpl.class)
class DomainObjectRepositoryIT {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DomainObjectRepository domainObjectRepository;

    @Test
    void should_save_new_entity() {
        Integer rowCount = jdbcTemplate.queryForObject("select count(*) from domain_object", Integer.class);

        domainObjectRepository.save(DomainObject.of(2L, 1L, "testi", LocalDate.now()));

        assertThat(jdbcTemplate.query("select id, data, date from domain_object",
                new BeanPropertyRowMapper<>(DomainObject.class)))
                .hasSize(rowCount + 1);
    }

    @Test
    void should_update_existing_entity() {
        Integer rowCount = jdbcTemplate.queryForObject("select count(*) from domain_object", Integer.class);

        domainObjectRepository.save(DomainObject.of(1L, 1L, "testi2", LocalDate.now()));

        assertThat(jdbcTemplate.queryForObject("select count(*) from domain_object", Integer.class))
                .isEqualTo(rowCount);

        assertThat(jdbcTemplate.queryForObject("select id, data, date from domain_object where id = 1",
                new BeanPropertyRowMapper<>(DomainObject.class)).getData())
                .isEqualTo("testi2");
    }

    @Test
    void should_read_entity() {
        assertThat(domainObjectRepository.findById(1L))
                .isEqualTo(DomainObject.of(1L, 1L, "testi", LocalDate.now()));
    }

    @Test
    void should_read_all_entities() {
        Integer rowCount = jdbcTemplate.queryForObject("select count(*) from domain_object", Integer.class);

        assertThat(domainObjectRepository.findAll())
                .hasSize(rowCount);
    }

    @Test
    void should_delete_entity() {
        Integer rowCount = jdbcTemplate.queryForObject("select count(*) from domain_object", Integer.class);

        domainObjectRepository.deleteById(1L);

        assertThat(jdbcTemplate.queryForObject("select count(*) from domain_object", Integer.class))
                .isEqualTo(rowCount - 1);
    }
}

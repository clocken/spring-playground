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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DomainObjectResourceIT {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    MockMvc mockMvc;

    @Transactional
    @Test
    void should_create_domain_object() throws Exception {
        Integer rowCount = jdbcTemplate.queryForObject("select count(*) from domain_object", Integer.class);

        mockMvc.perform(put("/api/v0/domain-object")
                .contentType(MediaType.APPLICATION_JSON)
                .content("" +
                        "[" +
                        "{" +
                        "\"id\":2" +
                        ",\"referencedId\":1" +
                        ",\"data\":\"testi2\"" +
                        ",\"date\":\"2021-05-21\"" +
                        "}" +
                        "]"
                ))
                .andExpect(status().isCreated());

        assertThat(jdbcTemplate.queryForObject("select count(*) from domain_object", Integer.class))
                .isEqualTo(rowCount + 1);
    }

    @Transactional
    @Test
    void should_create_valid_domain_objects_only() throws Exception {
        Integer rowCount = jdbcTemplate.queryForObject("select count(*) from domain_object", Integer.class);

        mockMvc.perform(put("/api/v0/domain-object")
                .contentType(MediaType.APPLICATION_JSON)
                // construct a JSON array of 40000 elements with the following conditions:
                // * every second element has invalid foreign key, meaning 20000 invalid elements
                // * every fifth element has missing "data" field, meaning 8000 invalid elements
                //   * this means every tenth element has both invalid foreign key and missing "data" field, meaning 4000 elements "overlap" on both conditions
                // * extra fields on an element should get ignored by the REST controller and the element be regarded as valid
                // So all in all there will be 24000 invalid and 16000 valid elements in the constructed JSON array
                .content("[" +
                        Stream.iterate(2, i -> ++i)
                                .map(i -> "{" +
                                        "\"id\":" + i +
                                        ((i % 2) == 0 ? ",\"referencedId\":1" : ",\"referencedId\":5") + // 5 is not a valid foreign key
                                        ((i % 5) != 0 ? ",\"data\":\"testi" + i + "\"" : "") + // empty/missing "data" field is not valid
                                        ((i % 3) == 0 ? ",\"someextravalue\":\"bogus" + i + "\"" : "") + // mix in some elements with extra fields
                                        ",\"date\":\"2021-05-21\"" +
                                        "}")
                                .limit(40000)
                                .collect(Collectors.joining(",")) +
                        "]"))
                .andExpect(status().isCreated());

        assertThat(jdbcTemplate.queryForObject("select count(*) from domain_object", Integer.class))
                .isEqualTo(rowCount + 16000);
    }
}

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

/**
 * Basic contract for a CRUD (Create, Read, Update, Delete) Repository / DAO.
 * May later be replaced by Spring Data's repository abstraction to auto-generate repository code.
 *
 * @param <T>  The entity type managed by this repository
 * @param <ID> The ID type of the entity
 */
public interface CrudRepository<T, ID> {

    T save(T entity);

    Iterable<T> saveAll(Iterable<T> entities);

    Iterable<T> findAll();

    T findById(ID id);

    void deleteById(ID id);
}

package com.verong.demo.database.custom.hibernate.persistence.orm;

import com.verong.demo.database.custom.hibernate.persistence.orm.annotation.MyEntity;

public interface MyEntityManager {

    /**
     * This method searches an entity by the passed id to return an instance of it (using type parameter) filled
     * with all data from a database.
     * <br>
     * This method can work only with those types of entity that are annotated with {@link MyEntity} annotation.
     * If the passed entity type is not annotated, the method throws an exception to notify about it.
     *
     * @param type class of the entity that has to be returned
     * @param id primary key to search by
     * @return an instance of {@code entity} that is filled by values from a database
     * @param <T> refers to {@code Entity} type
     */
    <T> T findById(Class<T> type, Long id);
}

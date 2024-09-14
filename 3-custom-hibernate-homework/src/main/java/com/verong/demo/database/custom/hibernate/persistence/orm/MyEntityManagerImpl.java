package com.verong.demo.database.custom.hibernate.persistence.orm;

import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;

@RequiredArgsConstructor
public class MyEntityManagerImpl implements MyEntityManager {

    private final DataSource dataSource;

    @Override
    public <T> T findById(Class<T> type, Long id) {

        /*
            TODO: implement this method to be able to send an SQL query to find entity in database by id and return an instance of entity back;

            The implementation of this method has to include the following steps:
            - create more annotation to be able to set table name, ID and column names (The annotation MyEntity has been already created);
            - use Reflection API to get necessary information of the passed entity type using annotation;
            - create a string with SQL query to find an entity by id:
                * set proper list of columns in "select" statement;
                * set proper table name;
            - execute your query using PreparedStatement of JDBC API;
            - create an instance of the passed entity type using Reflection API and no-args constructor;
            - set all fields using Reflection API;
        */

        return null;
    }
}

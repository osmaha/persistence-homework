package com.verong.demo.database.flyway;

public class Application {

    private static final String DATASOURCE_URL = "";
    private static final String DATASOURCE_USER = "";
    private static final String DATASOURCE_PASSWORD = "";

    public static void main(String[] args) {

        /*
            Your main task is to complete several points:
            - use the database from the previous exercise (you can clean your database or set another schema for flyway);
            - configure Flyway properly (do not forget about JDBC Driver and Flyway);
            - create a folder for migration scripts inside "resources";
            - add several SQL files with the following queries:
                * create table -> file V1__init_schema.sql;
                * insert rows into created table -> V2__insert_default_values.sql;
                * update statement -> V3__update_default_values.sql;
                * alter table to add additional column -> V4__add_new_column.sql;

            - check database that all SQL scripts have been executed correctly;
        */

        //TODO: create the Flyway instance and run migrations

    }
}

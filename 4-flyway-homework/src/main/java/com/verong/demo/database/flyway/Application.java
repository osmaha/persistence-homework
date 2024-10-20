package com.verong.demo.database.flyway;

import org.flywaydb.core.Flyway;

public class Application {

    private static final String DATASOURCE_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DATASOURCE_USER = "postgres";
    private static final String DATASOURCE_PASSWORD = "postgres";

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

            IMPORTANT:  You can add one or more files of migration and run a migration just to see how it works.
                        Flyway prints a list of logs you to be able to understand the current version of your database.
                        Try to add next migration file and run your program to see that previous migrations will be skipped
                        since they have been already run by Flyway.
        */

        Flyway flyway = Flyway.configure()
                .dataSource(DATASOURCE_URL, DATASOURCE_USER, DATASOURCE_PASSWORD)
                .schemas("test")
                .locations("db/migration")
                .load();

        flyway.migrate();
    }
}

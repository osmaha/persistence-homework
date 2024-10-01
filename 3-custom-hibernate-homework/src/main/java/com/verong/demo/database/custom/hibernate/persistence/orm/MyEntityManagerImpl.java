package com.verong.demo.database.custom.hibernate.persistence.orm;

import com.verong.demo.database.custom.hibernate.persistence.orm.annotation.MyColumn;
import com.verong.demo.database.custom.hibernate.persistence.orm.annotation.MyEntity;
import com.verong.demo.database.custom.hibernate.persistence.orm.annotation.MyId;
import com.verong.demo.database.custom.hibernate.persistence.orm.annotation.MyTable;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
public class MyEntityManagerImpl implements MyEntityManager {

    private static final Class<MyEntity> MY_ENTITY_ANNOTATION = MyEntity.class;
    private static final Class<MyTable> MY_TABLE_ANNOTATION = MyTable.class;
    private static final Class<MyId> MY_ID_ANNOTATION = MyId.class;
    private static final Class<MyColumn> MY_COLUMN_ANNOTATION = MyColumn.class;

    private static final String SELECT_BY_ID_TEMPLATE = "select %s from %s where %s = ?;";

    private final DataSource dataSource;

    @Override
    public <T> T findById(Class<T> type, Long id) {

        /*
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

        validateEntityType(type);
        var entityDefinition = parseEntity(type);

        try (var connection = dataSource.getConnection()) {
            return findByIdInternal(entityDefinition, id, connection);
        } catch (SQLException e) {
            throw new MyEntityManagerException("Error during selecting entity %s by id %d".formatted(type.getName(), id), e);
        }
    }

    private void validateEntityType(Class<?> type) {
        Objects.requireNonNull(type, "The parameter 'type' cannot be null");
        if (!type.isAnnotationPresent(MY_ENTITY_ANNOTATION)) {
            throw new EntityValidationException("Class %s is not an entity since the annotation [@%s] is not present".formatted(type.getName(), MY_ENTITY_ANNOTATION.getSimpleName()));
        }
        if (!type.isAnnotationPresent(MY_TABLE_ANNOTATION)) {
            throw new EntityValidationException("Entity %s does not have the annotation [@%s], so there is not way to get a table name".formatted(type.getName(), MY_TABLE_ANNOTATION.getSimpleName()));
        }
        var myIdAnnotatedFieldsCount = Arrays.stream(type.getDeclaredFields()).filter(field -> field.isAnnotationPresent(MyId.class)).count();
        if (myIdAnnotatedFieldsCount != 1) {
            throw new EntityValidationException("Entity %s does not have the field with the annotation [@%s], so there is not way to detect primary key".formatted(type.getName(), MY_ID_ANNOTATION.getSimpleName()));
        }
    }

    private <T> EntityDefinition<T> parseEntity(Class<T> type) {
        var tableName = getTableName(type);
        var idColumnName = getIdColumnName(type);
        var columnMap = getColumnsMap(type);

        return new EntityDefinition<>(type, tableName, idColumnName, columnMap);
    }

    private String getTableName(Class<?> type) {
        var tableName = type.getAnnotation(MY_TABLE_ANNOTATION).name();

        if (Objects.isNull(tableName) || tableName.isEmpty()) {
            throw new EntityValidationException("Table name of the entity %s has incorrect value".formatted(type.getName()));
        }

        return tableName;
    }

    private String getIdColumnName(Class<?> type) {
        var possibleIds = Arrays.stream(type.getDeclaredFields()).filter(field -> field.isAnnotationPresent(MyId.class)).toList();
        if (possibleIds.size() != 1) {
            throw new EntityValidationException("Cannot find Id for the entity %s".formatted(type.getName()));
        }

        var idField = possibleIds.getFirst();
        Class<?> idFieldType = idField.getType();
        if (!idFieldType.equals(Long.class)) {
            throw new EntityValidationException("Entity %s has unexpected type of Id: %s".formatted(type.getName(), idFieldType.getName()));
        }

        var idColumnName = Optional.ofNullable(idField.getAnnotation(MY_COLUMN_ANNOTATION))
                .map(MyColumn::name)
                .orElse(idField.getName());

        if (idColumnName.isEmpty()) {
            throw new EntityValidationException("Table name of the entity %s has incorrect value".formatted(type.getName()));
        }

        return idColumnName;
    }

    private Map<String, Field> getColumnsMap(Class<?> type) {
        var columnMap = new LinkedHashMap<String, Field>();

        for (var field : type.getDeclaredFields()) {
            var columnName = Optional.ofNullable(field.getAnnotation(MY_COLUMN_ANNOTATION))
                    .map(MyColumn::name)
                    .orElse(field.getName());

            columnMap.put(columnName, field);
        }

        return columnMap;
    }

    private record EntityDefinition<T>(
            Class<T> type,
            String tableName,
            String idColumnName,
            Map<String, Field> columnMap
    ) {

    }

    private <T> String buildSqlString(EntityDefinition<T> entityDefinition) {
        var columnMap = entityDefinition.columnMap;


        var columnListBuilder = new StringBuilder();
        var entries = columnMap.entrySet();
        var entriesSize = entries.size();

        int index = 0;
        for (var entry : entries) {
            columnListBuilder.append(entry.getKey());

            if (index + 1 != entriesSize) {
                columnListBuilder.append(", ");
            }

            index++;
        }

        var columnList = columnListBuilder.toString();
        var tableName = entityDefinition.tableName;
        var idColumnName = entityDefinition.idColumnName;

        return SELECT_BY_ID_TEMPLATE.formatted(columnList, tableName, idColumnName);
    }

    private <T> T findByIdInternal(EntityDefinition<T> entityDefinition, Long id, Connection connection) throws SQLException {
        var selectByIdStatement = prepareSelectStudentByIdStatement(entityDefinition, id, connection);
        var resultSet = selectByIdStatement.executeQuery();
        return resultSet.next() ? parseRow(entityDefinition, resultSet) : null;
    }

    private PreparedStatement prepareSelectStudentByIdStatement(EntityDefinition<?> entityDefinition, Long id, Connection connection) {
        try {
            var sqlString = buildSqlString(entityDefinition);
            var selectByIdStatement = connection.prepareStatement(sqlString);
            selectByIdStatement.setLong(1, id);
            return selectByIdStatement;
        } catch (SQLException e) {
            throw new MyEntityManagerException("Statement for selecting entity %s by id %d cannot be prepared".formatted(entityDefinition.type.getName(), id), e);
        }
    }

    private <T> T parseRow(EntityDefinition<T> entityDefinition, ResultSet resultSet) throws SQLException {
        var name = entityDefinition.type.getName();
        try {
            var nonArgsConstructor = entityDefinition.type.getConstructor();
            var instance = nonArgsConstructor.newInstance();

            entityDefinition.columnMap.forEach((columnName, field) -> {
                applyFieldMapper(instance, field, columnName, resultSet);
            });

            return instance;
        } catch (NoSuchMethodException e) {
            throw new EntityValidationException("Entity %s does not have default non-args constructor which is required".formatted(name), e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new EntityValidationException("Cannot instantiate an entity %s".formatted(name), e);
        } catch (EntityValidationException e) {
            throw new EntityValidationException("Cannot fill the entity %s with data from ResultSet".formatted(name), e);
        }
    }

    private <T> void applyFieldMapper(T instance, Field field, String columnName, ResultSet rs) {

        try {
            field.setAccessible(true);
            Class<?> type = field.getType();
            Object value;
            if (type == String.class) {
                value = rs.getString(columnName);
            } else if (type == Integer.class || type == int.class) {
                value = rs.getInt(columnName);

            } else if (type == Long.class || type == long.class) {
                value = rs.getLong(columnName);

            } else if (type == Boolean.class || type == boolean.class) {
                value = rs.getBoolean(columnName);

            } else if (type == LocalDate.class) {
                value = rs.getDate(columnName).toLocalDate();

            } else if (type.isEnum()) {
                String enumValue = rs.getString(columnName);
                value = Enum.valueOf((Class<Enum>) type, enumValue);
            } else {
                throw new IllegalArgumentException("Unsupported field type: " + type.getName());
            }

            field.set(instance, value);
        } catch (SQLException e) {
            throw new EntityValidationException("Cannot extract value for the column %s from ResultSet".formatted(columnName), e);
        } catch (IllegalAccessException e) {
            throw new EntityValidationException("Cannot inject value for the field %s".formatted(field.getName()), e);
        }
    }

}

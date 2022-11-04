package com.Utilities;

import com.google.cloud.secretmanager.v1.AccessSecretVersionResponse;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretVersionName;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.restassured.internal.path.json.JSONAssertion;

import javax.management.ObjectName;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostgresDBHelper {

    private String CLOUD_SQL_CONNECTION_NAME;
    private String DB_USER;
    private String DB_PASS;
    private String DB_NAME;

    private static DataSource pool;

    public PostgresDBHelper(String projectId) {

        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            SecretVersionName secretVersionName = SecretVersionName.of("repd-e-eng-adm-01", "sensitive_keys", "latest");
            AccessSecretVersionResponse response = client.accessSecretVersion(secretVersionName);
            response.getPayload().getData().toStringUtf8();
            HashMap<Object, Object> data = new Gson().fromJson(response.getPayload().getData().toStringUtf8(), HashMap.class);
            DB_NAME = (String) ((LinkedTreeMap) ((LinkedTreeMap) data.get(projectId)).get("POSTGRES")).get("ENV_DATABASE_NAME");
            DB_USER = (String) ((LinkedTreeMap) ((LinkedTreeMap) data.get(projectId)).get("POSTGRES")).get("ENV_DATABASE_USER");
            DB_PASS = (String) ((LinkedTreeMap) ((LinkedTreeMap) data.get(projectId)).get("POSTGRES")).get("ENV_DATABASE_PASSWORD");
            CLOUD_SQL_CONNECTION_NAME = (String) ((LinkedTreeMap) ((LinkedTreeMap) data.get(projectId)).get("POSTGRES")).get("ENV_DATABASE_CONN");

        } catch (Exception e) {
            Log.error(e.toString());
        }

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(String.format("jdbc:postgresql:///%s", DB_NAME));
        config.setUsername(DB_USER); // e.g. "root", "postgres"
        config.setPassword(DB_PASS); // e.g. "my-password"

        config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.postgres.SocketFactory");
        config.addDataSourceProperty("cloudSqlInstance", CLOUD_SQL_CONNECTION_NAME);
        config.addDataSourceProperty("ipTypes", "PUBLIC,PRIVATE");

        config.setMaximumPoolSize(2);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(10000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        pool = new HikariDataSource(config);
    }


    /**
     * This method will query the postgres db
     *
     * @param query to be executed
     * @return Resultset
     */
    public ResultSet getPostgresTableResultSet(String query) {
        ResultSet results = null;
        try (Connection conn = pool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Execute the statement
            results = pstmt.executeQuery();
            return results;
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to execute the query :" + query, exception);
        }
    }


    public List<HashMap<String, Object>> queryPostgresTable(String sql) {

        List<HashMap<String, Object>> data = new ArrayList<>();
        HashMap<String, Object> rowData;
        try (Connection conn = pool.getConnection()) {
            // PreparedStatements are compiled by the database immediately and executed at a later date.
            // Most databases cache previously compiled queries, which improves efficiency.
            String stmt1 = sql;
            try (PreparedStatement voteStmt = conn.prepareStatement(stmt1);) {
                // Execute the statement
                ResultSet results = voteStmt.executeQuery();

                while (results.next()) {
                    rowData = new HashMap<>();
                    ResultSetMetaData resultSetMetaData = results.getMetaData();
                    for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                        String key = results.getMetaData().getColumnName(i);
                        if (results.getArray(key) != null) {
                            rowData.put(key, results.getArray(key).toString());
                        } else {
                            rowData.put(key, "");
                        }
                    }

                    data.add(rowData);
                }

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return data;
    }

    public int updateQueryPostgresTable(String query) {

        int affectedrows = 0;
        try (Connection conn = pool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Execute the statement
            affectedrows = pstmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to execute the query :" + query, exception);
        }
        return affectedrows;
    }

    /*** * This method will wait till the value of the column changes to expected
     *
     * @param table
     * @param column
     * @param conditions
     * @param in_expectedValueOfTheAttribute
     * @param timeout
     * @param sleepTime
     * @return
     * @throws Exception
     */
    public String waitForAColumnValueToChange(String table, String column, String conditions, String in_expectedValueOfTheAttribute, double timeout, long sleepTime , List<String> pipelineFailureStates) throws Exception {
        timeout = timeout <= 0L ? 360L : timeout;
        sleepTime = sleepTime <= 0L ? 1000L : sleepTime;
        int timer = 0;

        String attributeValue;
        for (attributeValue = queryOnCondition(table, column, conditions); (attributeValue == null || !attributeValue.equals(in_expectedValueOfTheAttribute)) && (long) timer < timeout; ++timer) {
            CommonUtils.sleep(sleepTime);
            attributeValue = queryOnCondition(table, column, conditions);
            Log.info("Attibute value " + attributeValue);
            if (pipelineFailureStates.contains(attributeValue)) {
                return attributeValue;
            }
        }

        if (!in_expectedValueOfTheAttribute.equals(attributeValue)) {
            throw new RuntimeException("The expected state is " + in_expectedValueOfTheAttribute + " but the current state after " + timeout + " seconds is: " + attributeValue);
        } else {
            Log.debug("The expected value for " + column + " is " + in_expectedValueOfTheAttribute + " has been reached in" + timer + "s");
            return attributeValue;
        }
    }

    /**
     * This method will query the database on the basis of the given condition
     *
     * @param table
     * @param column     column to be selected
     * @param conditions
     * @return value of the column
     * @throws Exception
     */
    public String queryOnCondition(String table, String column, String conditions) throws Exception {
        String query = String.format("Select %s from %s where %s", column, table, conditions);

        String columnValue = null;
        ResultSet resultSet = getPostgresTableResultSet(query);
        while (resultSet.next()) {
            columnValue = resultSet.getString(column);
            Log.info(column + " value is " + columnValue);
        }
        resultSet.close();
        return columnValue;
    }
}

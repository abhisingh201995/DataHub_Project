package com.Utilities;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostgresDBHelper {

    private static final String CLOUD_SQL_CONNECTION_NAME =
            System.getenv("CLOUD_SQL_CONNECTION_NAME");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASS = System.getenv("DB_PASS");
    private static final String DB_NAME = System.getenv("DB_NAME");

    private static DataSource pool;

    public PostgresDBHelper() {

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


    public List<HashMap<String, Object>> queryPostgresTable(String sql){

        List<HashMap<String,Object>> data = new ArrayList<>();
        HashMap<String,Object> rowData;
        try (Connection conn = pool.getConnection()) {
            // PreparedStatements are compiled by the database immediately and executed at a later date.
            // Most databases cache previously compiled queries, which improves efficiency.
            String stmt1 = sql;
            try (PreparedStatement voteStmt = conn.prepareStatement(stmt1);) {
                // Execute the statement
                ResultSet results = voteStmt.executeQuery();

                while (results.next()) {
                   rowData = new HashMap<>();
                   ResultSetMetaData resultSetMetaData=results.getMetaData();
                    for(int i=1;i<=resultSetMetaData.getColumnCount();i++){
                        String key = results.getMetaData().getColumnName(i);
                        if(results.getArray(key)!=null){
                            rowData.put(key, results.getArray(key).toString());
                        }else{
                            rowData.put(key,"");
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








}

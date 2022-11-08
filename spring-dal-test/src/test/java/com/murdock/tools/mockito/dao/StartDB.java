package com.murdock.tools.mockito.dao;

import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

/**
 * @author weipeng2k 2022-11-08 20:12:33
 */
public class StartDB {
    public static void main(String[] args) throws Exception {
        Server dbServer = new Server();
        Properties props = new Properties();
        //HSQL配置文件，将易变部分写入到配置文件中，避免硬编码

        props.setProperty("server.port", "9001");
        props.setProperty("server.remote_open", "true");
        props.setProperty("server.database.0", "file:hsqldb/demodb");
        props.setProperty("server.dbname.0", "testdb");

        dbServer.setProperties(new HsqlProperties(props));
        dbServer.start();

        Connection con = null;
        Statement stmt = null;
        int result = 0;

        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost:9001/testdb");
            stmt = con.createStatement();

            String sql = "CREATE TABLE member (\n" +
                    "   id INT NOT NULL,\n" +
                    "   name VARCHAR(32) NOT NULL,\n" +
                    "   password VARCHAR(32) NOT NULL,\n" +
                    "   gmt_create DATE NOT NULL,\n" +
                    "   gmt_modified DATE NOT NULL,\n" +
                    "   PRIMARY KEY (id) \n" +
                    ");";
            result = stmt.executeUpdate(sql);
            con.commit();

        }  catch (Exception e) {
            e.printStackTrace(System.out);
        }
        System.out.println("Table created successfully");
        System.out.println("在Console中敲入回车,以停止Hsql DB服务.");
        if (System.in.read() != 0) {
            dbServer.stop();
            System.out.println("Hsql DB stopped");
        }
    }
}

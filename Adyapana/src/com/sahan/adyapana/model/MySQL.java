/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sahan.adyapana.model;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;

public class MySQL implements Serializable {

    private static Connection connection;

    public String ip;
    public String port;
    public String dbname;
    public String username;
    public String password;

    public String dump;

    public static void createConnection() throws Exception {

        if (connection == null) {

            FileInputStream inputStream = new FileInputStream("dbinfo.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            MySQL db = (MySQL) objectInputStream.readObject();
            objectInputStream.close();

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + db.ip + ":" + db.port + "/" + db.dbname, db.username, db.password);
        }

    }

    public static ResultSet executeSearch(String query) throws Exception {
        createConnection();
        return connection.createStatement().executeQuery(query);
    }

    public static Integer executeIUD(String query) throws Exception {
        createConnection();
        return connection.createStatement().executeUpdate(query);
    }
}

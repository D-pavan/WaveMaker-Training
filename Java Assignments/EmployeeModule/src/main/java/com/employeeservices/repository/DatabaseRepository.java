package com.employeeservices.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseRepository {
    private static DatabaseRepository databaseRepository;
    private Logger logger=LoggerFactory.getLogger(DatabaseRepository.class);
    private DatabaseRepository() {
        databaseRepository = null;
    }

    public static synchronized DatabaseRepository getInstance() {
        if (databaseRepository == null) {
            databaseRepository = new DatabaseRepository();
        }
        return databaseRepository;
    }

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/EMPLOYEES_DB", "root", "Pavan@970");
        } catch (Exception e) {
            System.out.println(e);
            logger.error("Exception while getting connection",e);
        }

        return null;
    }

}

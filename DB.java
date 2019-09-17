package com.mbsl.velocity.risk.auth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*
author- Amila Jayarathna
 */
public class DB {

    private static Connection c;

    private static void createNewConnection() throws Exception {

       // Class.forName("com.ibm.as400.access.AS400JDBCDriver");
        //c = DriverManager.getConnection("jdbc:as400://10.1.1.111/MBCLDTA", "MBSL2108", "MBSL2108");
    }

    public static int iud(String sql) throws Exception {

        int i = 0;
        try {
            if (c == null) {
                createNewConnection();
            }
            c.createStatement().executeUpdate(sql);
            i = 1;
        } catch (Exception e) {
        }
        return i;
    }

    public static void executeStmt(PreparedStatement stmt) throws Exception {

        if (c == null) {
            createNewConnection();
        }
        stmt.executeUpdate();
    }

    public static PreparedStatement getStmt(String sql) throws Exception {
        PreparedStatement stmt = null;
        if (c == null) {
            createNewConnection();
        }
        stmt = c.prepareStatement(sql);
        return stmt;
    }

    public static ResultSet PsSearch(String sql) throws Exception {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            if (c == null) {
                createNewConnection();
            }
            stmt = c.prepareStatement(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
        }

        return rs;
    }

    public static ResultSet search(String sql) throws Exception {
        if (c == null) {
            createNewConnection();
        }
        return c.createStatement().executeQuery(sql);
    }

    public static Connection getConnection() throws Exception {

        createNewConnection();
        return c;
    }

    public static void closeConnection() throws Exception {
        if (c != null) {
            c.close();
        }
    }

}

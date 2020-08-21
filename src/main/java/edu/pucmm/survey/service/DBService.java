package edu.pucmm.survey.service;

import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBService {

    private static Server tcpServer;
    private static Server webServer;
    private static final String URL = "jdbc:h2:tcp://localhost/~/Survey";

    public DBService() throws SQLException {
        tcpServer = Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers", "-tcpDaemon", "-ifNotExists");
        webServer = Server.createWebServer("-webPort", "9090", "-webAllowOthers", "-webDaemon");
    }

    public void startDB() throws SQLException {
        tcpServer.start();
        webServer.start();
    }

    public void stopDB() throws SQLException {
        webServer.shutdown();
        tcpServer.shutdown();
    }

    private Connection getConnection() {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(URL, "sa", "");
        } catch (SQLException e) {
            Logger.getLogger(DBService.class.getName()).log(Level.SEVERE, null, e);
        }

        return connection;
    }

    public void testConnection() {
        try {
            getConnection().close();
        } catch (SQLException e) {
            Logger.getLogger(DBService.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}

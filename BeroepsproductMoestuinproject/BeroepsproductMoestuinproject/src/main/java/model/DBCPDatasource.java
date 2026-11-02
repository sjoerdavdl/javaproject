package model;

import java.sql.SQLException;
import java.sql.Connection;
import org.apache.commons.dbcp2.BasicDataSource;

public class DBCPDatasource {

    private static BasicDataSource ds = new BasicDataSource();

    static {
        ds.setUrl("jdbc:mysql://localhost:3306/moestuinbp1her?useSSL=false"); //Vul hier de naam van de database in op de plek van 'blok1'
        ds.setUsername("root"); // vul hier de gebruikersnaam in
        ds.setPassword("c_4pX7#9qm0e@1uW7"); // vul hier het wachtwoord in
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    private DBCPDatasource() {

    }

}

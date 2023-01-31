package dbWork;

import serverSettings.ConfigRead;

import java.sql.*;
import java.util.ArrayList;

public class DbConnection {
    private static final String DBHOST = ConfigRead.DBHOST;
    private static final String DBPORT = ConfigRead.DBPORT;
    private static final String DBUSER = ConfigRead.DBUSER;
    private static final String DBPASS = ConfigRead.DBPASS;
    private static final String DBNAME = ConfigRead.DBNAME;
//    private static final String DBHOST = "localhost";
//    private static final String DBPORT = "3306";
//    private static final String DBUSER = "root";
//    private static final String DBPASS = "tu051220.";
//    private static final String DBNAME = "insurance_service";
    private static final String URL = "jdbc:mysql://" + DBHOST + ":" + DBPORT + "/" + DBNAME;
    private static DbConnection instance;
    protected Connection connection;
    protected Statement statement;

    private DbConnection() {
        try {
            connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
            statement = connection.createStatement();
            System.out.println("Connected to DB");
        } catch (SQLException e) {
            System.out.println("DB connection error");
            e.printStackTrace();
        }
    }

    public static synchronized DbConnection getInstance() {
        if (instance == null)
            instance = new DbConnection();
        return instance;
    }

    public ArrayList<String[]> getArrayResult(String query) {
        ArrayList<String[]> res = new ArrayList<>();
        try {
            ResultSet resultSet = statement.executeQuery(query);
            int count = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
                String[] arrayString = new String[count];
                for (int i = 1;  i <= count; i++)
                    arrayString[i - 1] = resultSet.getString(i);
                res.add(arrayString);
            }
        } catch (NullPointerException e) {
            return res;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }
}
package dbWork;

import basicClasses.Insurance;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class DbInsurance {
    private static DbInsurance instance;
    private final DbConnection dbConnection;

    public void delete(int idService) {
        String query = "DELETE FROM service WHERE idservice = " + idService;
        try {
            dbConnection.statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Insurance insurance) {
        String proc = "{CALL update_service(?,?,?,?)}";
        try {
            CallableStatement callableStatement = dbConnection.connection.prepareCall(proc);
            callableStatement.setInt(1, insurance.getIdInsurance());
            callableStatement.setString(2, insurance.getName());
            callableStatement.setFloat(3, insurance.getPricePerMonth());
            callableStatement.setFloat(4, insurance.getPayout());
            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(Insurance insurance) {
        String proc = "{CALL insert_service(?,?,?)}";
        try {
            CallableStatement callableStatement = dbConnection.connection.prepareCall(proc);
            callableStatement.setString(1, insurance.getName());
            callableStatement.setFloat(2, insurance.getPricePerMonth());
            callableStatement.setFloat(3, insurance.getPayout());
            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String[]> select(int idInsurance) {
        String query = "SELECT idservice, name, price_per_month, payout FROM service";
        if (idInsurance != 0)
            query += " WHERE idservice = " + idInsurance;
        return DbConnection.getInstance().getArrayResult(query);
    }

    private DbInsurance() {
        dbConnection = DbConnection.getInstance();
    }

    public static synchronized DbInsurance getInstance() {
        if (instance == null)
            instance = new DbInsurance();
        return instance;
    }
}

package dbWork;

import basicClasses.Agent;
import basicClasses.Authorization;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

public class DbAgent {
    private static DbAgent instance;
    private final DbConnection dbConnection;

    public boolean delete(int idUser) {
        String query = "DELETE FROM user WHERE iduser = " + idUser;
        try {
            dbConnection.statement.executeUpdate(query);
        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void update(Agent agent) {
        String proc = "{CALL update_agent(?,?,?,?,?,?,?)}";
        try {
            CallableStatement callableStatement = dbConnection.connection.prepareCall(proc);
            callableStatement.setInt(1, agent.getIdUser());
            callableStatement.setDate(2, Date.valueOf(agent.getEmploymentDate()));
            callableStatement.setString(3, agent.getSurname());
            callableStatement.setString(4, agent.getName());
            callableStatement.setDate(5, Date.valueOf(agent.getBirthday()));
            callableStatement.setString(6, agent.getPhone());
            callableStatement.setString(7, agent.getAddress());
            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String[]> select(int idUser) {
        String query = """
                SELECT user.iduser, surname, name, birthday, phone, address, login, password, employment_date
                FROM user, agent, authorization
                WHERE user.iduser = agent.iduser AND user.iduser = authorization.iduser
                """;
        if (idUser != 0)
            query += " AND user.iduser = " + idUser;
        return DbConnection.getInstance().getArrayResult(query);
    }

    private DbAgent() {
        dbConnection = DbConnection.getInstance();
    }

    public static synchronized DbAgent getInstance() {
        if (instance == null)
            instance = new DbAgent();
        return instance;
    }

    public void insert(Agent agent, Authorization authorization) {
        String proc = "{CALL insert_agent(?,?,?,?,?,?,?,?)}";
        try {
            CallableStatement callableStatement = dbConnection.connection.prepareCall(proc);
            callableStatement.setDate(1, Date.valueOf(agent.getEmploymentDate()));
            callableStatement.setString(2, agent.getSurname());
            callableStatement.setString(3, agent.getName());
            callableStatement.setDate(4, Date.valueOf(agent.getBirthday()));
            callableStatement.setString(5, agent.getPhone());
            callableStatement.setString(6, agent.getAddress());
            callableStatement.setString(7, authorization.getLogin());
            callableStatement.setString(8, authorization.getPassword());
            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

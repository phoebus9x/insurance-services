package dbWork;

import basicClasses.Authorization;
import basicClasses.User;

import java.io.ObjectOutputStream;
import java.sql.*;
import java.util.ArrayList;

public class DbUser {
    private static DbUser instance;
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

    public ArrayList<String[]> select(int idUser) {
        String query = """
                SELECT user.iduser, surname, name, birthday, phone, address, login, password, passport
                FROM user, client, authorization
                WHERE user.iduser = client.iduser AND user.iduser = authorization.iduser
                """;
        if (idUser != 0)
            query += " AND user.iduser = " + idUser;
        return DbConnection.getInstance().getArrayResult(query);
    }

    public void update(User user) {
        String proc = "{CALL update_client(?,?,?,?,?,?,?)}";
        try {
            CallableStatement callableStatement = dbConnection.connection.prepareCall(proc);
            callableStatement.setInt(1, user.getIdUser());
            callableStatement.setString(2, user.getPassport());
            callableStatement.setString(3, user.getSurname());
            callableStatement.setString(4, user.getName());
            callableStatement.setDate(5, Date.valueOf(user.getBirthday()));
            callableStatement.setString(6, user.getPhone());
            callableStatement.setString(7, user.getAddress());
            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(User user, Authorization authorization) {
        String proc = "{call insert_client(?,?,?,?,?,?,?,?)}";
        try {
            CallableStatement callableStatement = dbConnection.connection.prepareCall(proc);
            callableStatement.setString(1, user.getPassport());
            callableStatement.setString(2, user.getSurname());
            callableStatement.setString(3, user.getName());
            callableStatement.setDate(4, Date.valueOf(user.getBirthday()));
            callableStatement.setString(5, user.getPhone());
            callableStatement.setString(6, user.getAddress());
            callableStatement.setString(7, authorization.getLogin());
            callableStatement.setString(8, authorization.getPassword());
            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DbUser() {
        dbConnection = DbConnection.getInstance();
    }

    public static synchronized DbUser getInstance() {
        if (instance == null)
            instance = new DbUser();
        return instance;
    }
}

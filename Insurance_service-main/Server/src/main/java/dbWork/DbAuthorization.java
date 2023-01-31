package dbWork;

import basicClasses.Authorization;
import global.Global;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

public class DbAuthorization {
    private static DbAuthorization instance;
    private final DbConnection dbConnection;

    public String getRole(Authorization authorization) {
        String proc = "{CALL get_role(?,?,?,?)}";
        int id_user = 0;
        String role = "";
        try {
            CallableStatement callableStatement = dbConnection.connection.prepareCall(proc);
            callableStatement.setString(1, authorization.getLogin());
            callableStatement.setString(2, authorization.getPassword());
            callableStatement.registerOutParameter(3, Types.INTEGER);
            callableStatement.registerOutParameter(4, Types.VARCHAR);
            callableStatement.execute();
            id_user = callableStatement.getInt(3);
            role = callableStatement.getString(4);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (id_user != 0 && !Objects.equals(role, "")) {
            Global.saveObject(id_user, role);
            return role;
        }
        else
            return "";
    }

    private DbAuthorization() {
        dbConnection = DbConnection.getInstance();
    }

    public static synchronized DbAuthorization getInstance() {
        if (instance == null)
            instance = new DbAuthorization();
        return instance;
    }
}

package dbWork;

import java.util.ArrayList;

public class DbAdmin {
    private static DbAdmin instance;
    private final DbConnection dbConnection;

    public ArrayList<String[]> select(int id_user) {
        String query = """
                SELECT user.iduser, surname, name, birthday, phone, address, login, password
                FROM user, admin, authorization
                WHERE user.iduser = admin.iduser AND user.iduser = authorization.iduser
                """;
        if (id_user != 0)
            query += " AND user.iduser = " + id_user;
        return DbConnection.getInstance().getArrayResult(query);
    }

    private DbAdmin() {
        dbConnection = DbConnection.getInstance();
    }

    public static synchronized DbAdmin getInstance() {
        if (instance == null)
            instance = new DbAdmin();
        return instance;
    }
}

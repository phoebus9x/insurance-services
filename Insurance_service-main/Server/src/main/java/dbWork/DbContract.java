package dbWork;

import basicClasses.Contract;
import global.Global;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class DbContract {
    private static DbContract instance;
    private final DbConnection dbConnection;

    public ArrayList<String[]> getAgentsReport() {
        String query = """
                SELECT us.surname, COUNT(*)
                FROM reviewed_contracts rc, agent a, user us
                WHERE a.iduser = us.iduser AND rc.idagent = a.idagent
                GROUP BY us.surname;
                """;
        return DbConnection.getInstance().getArrayResult(query);
    }

    public ArrayList<String[]> getFinancialReport() {
        String query = """
                SELECT s.name, ifnull(t.r_cn, 0), ifnull(ROUND(t.r_pr, 1), 0), ifnull(od.od_cn, 0),
                ifnull(ROUND(od.od_pr, 1), 0), ifnull(ot.ot_cn, 0), ifnull(ROUND(ot.ot_pr, 1), 0)
                  FROM service s
                       left join (
                SELECT c.idservice, COUNT(*) r_cn, SUM(c.price) r_pr
                FROM contract c, service sv
                WHERE c.idstatus = 1 AND c.idservice = sv.idservice
                GROUP BY idservice) t on s.idservice = t.idservice
                       left join (
                SELECT c.idservice, COUNT(*) od_cn, SUM(c.price) od_pr
                FROM contract c, service sv
                WHERE c.idstatus = 2 AND c.idservice = sv.idservice
                GROUP BY idservice) od on s.idservice = od.idservice
                       left join (
                SELECT c.idservice, COUNT(*) ot_cn, SUM(c.price) ot_pr
                FROM contract c, service sv
                WHERE c.idstatus = 3 AND c.idservice = sv.idservice
                GROUP BY idservice) ot on s.idservice = ot.idservice;
                """;
        return DbConnection.getInstance().getArrayResult(query);
    }

    public ArrayList<String[]> getReport() {
        String query = """
                SELECT s.status_name, COUNT(*)
                FROM contract c, status s
                WHERE c.idstatus = s.idstatus
                GROUP BY s.status_name;
                """;
        return DbConnection.getInstance().getArrayResult(query);
    }

    public void changeStatus(int idContract, int idStatus) {
        String proc = "{call set_status(?,?,?)}";
        try {
            CallableStatement callableStatement = dbConnection.connection.prepareCall(proc);
            callableStatement.setInt(1, idContract);
            callableStatement.setInt(2, Global.agent.getIdUser());
            callableStatement.setInt(3, idStatus);
            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(Contract contract) {
        String proc = "{call insert_contract(?,?,?,?)}";
        try {
            CallableStatement callableStatement = dbConnection.connection.prepareCall(proc);
            callableStatement.setDate(1, Date.valueOf(contract.getStartDate()));
            callableStatement.setDate(2, Date.valueOf(contract.getEndDate()));
            callableStatement.setInt(3, Global.user.getIdUser());
            callableStatement.setInt(4, contract.getIdService());
            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int idContract) {
        String query = "DELETE FROM contract WHERE idContract = " + idContract;
        try {
            dbConnection.statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Contract contract) {
        String proc = "{CALL update_contract(?,?,?,?)}";
        try {
            CallableStatement callableStatement = dbConnection.connection.prepareCall(proc);
            callableStatement.setInt(1, contract.getIdContract());
            callableStatement.setDate(2, Date.valueOf(contract.getStartDate()));
            callableStatement.setDate(3, Date.valueOf(contract.getEndDate()));
            callableStatement.setInt(4, contract.getIdService());
            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String[]> select(int idUser) {
        String query = String.format("""
         SELECT c.idcontract, c.idservice, c.idstatus, c.start_date, c.end_date, c.price, s.name, s.payout, st.status_name, u.surname, us.surname
         FROM  service s, status st, client cl, user us,
         contract c left join reviewed_contracts r on r.idcontract = c.idcontract
         		   left join agent ag on r.idagent = ag.idagent
         		   left join user u on ag.iduser = u.iduser
         WHERE c.idservice = s.idservice AND c.idstatus = st.idstatus AND c.idclient = cl.idclient AND cl.iduser = us.iduser
           and (exists( select null from client cl where cl.idclient = c.idclient and cl.iduser = %d)
            or (exists (select null from agent a where a.iduser = %d) and c.idstatus = 1)
            or (exists (select null from agent a, reviewed_contracts rc where a.idagent = rc.idagent and a.iduser = %d)));""",
                idUser, idUser, idUser);
        return DbConnection.getInstance().getArrayResult(query);
    }

    private DbContract() {
        dbConnection = DbConnection.getInstance();
    }

    public static synchronized DbContract getInstance() {
        if (instance == null)
            instance = new DbContract();
        return instance;
    }
}

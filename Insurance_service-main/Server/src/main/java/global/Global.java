package global;

import basicClasses.*;
import dbWork.DbFactory;

import java.time.LocalDate;
import java.util.ArrayList;

public class Global {
    public static User user;
    public static Agent agent;
    public static Admin admin;

    public static void saveObject(int idUser, String role) {
        DbFactory dbFactory = new DbFactory();
        ArrayList<String[]> arrayList;
        switch (role) {
            case "user" -> {
                arrayList = dbFactory.getUser().select(idUser);
                for (String[] row : arrayList) {
                    Global.user = new User(Integer.parseInt(row[0]), row[1], row[2], LocalDate.parse(row[3]),
                            row[4], row[5], row[8]);
                }
            }
            case "admin" -> {
                arrayList = dbFactory.getAdmin().select(idUser);
                for (String[] row : arrayList) {
                    Global.admin = new Admin(Integer.parseInt(row[0]), row[1], row[2], LocalDate.parse(row[3]),
                            row[4], row[5]);
                }
            }
            case "agent" -> {
                arrayList = dbFactory.getAgent().select(idUser);
                for (String[] row : arrayList) {
                    Global.agent = new Agent(Integer.parseInt(row[0]), row[1], row[2], LocalDate.parse(row[3]),
                            row[4], row[5], LocalDate.parse(row[8]));
                }
            }
        }
    }
}

package basicClasses;

import java.io.Serializable;
import java.time.LocalDate;

public class User extends UserBase implements Serializable {
    private String passport;

    public User() {}

    public User(String surname, String name, LocalDate birthday, String phone, String address, String passport) {
        super(surname, name, birthday, phone, address);
        this.passport = passport;
    }

    public User(int idUser, String surname, String name, LocalDate birthday, String phone, String address, String passport) {
        super(idUser, surname, name, birthday, phone, address);
        this.passport = passport;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }
}

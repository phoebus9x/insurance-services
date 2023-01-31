package basicClasses;

import java.io.Serializable;
import java.time.LocalDate;

public class UserBase implements Serializable {
    private int idUser;
    private String surname;
    private String name;
    private LocalDate birthday;
    private String phone;
    private String address;

    public UserBase() {}

    public UserBase(String surname, String name, LocalDate birthday, String phone, String address) {
        this.surname = surname;
        this.name = name;
        this.birthday = birthday;
        this.phone = phone;
        this.address = address;
    }

    public UserBase(int idUser, String surname, String name, LocalDate birthday, String phone, String address) {
        this.idUser = idUser;
        this.surname = surname;
        this.name = name;
        this.birthday = birthday;
        this.phone = phone;
        this.address = address;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

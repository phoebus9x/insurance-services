package basicClasses;

import java.io.Serializable;

public class Status implements Serializable {
    private int idStatus;
    private String statusName;

    public Status() {}

    public Status(String statusName) {
        this.statusName = statusName;
    }

    public Status(int idStatus, String statusName) {
        this.idStatus = idStatus;
        this.statusName = statusName;
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}

package serverSettings;

import basicClasses.*;
import dbWork.DbFactory;
import global.Global;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ServerWork implements Runnable {
    protected Socket clientSocket;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    public ServerWork(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ois = new ObjectInputStream(clientSocket.getInputStream());
            DbFactory dbFactory = new DbFactory();
            while (true) {
                String clientChoice = ois.readObject().toString();
                switch (clientChoice) {
                    case "login" -> {
                        String role = dbFactory.getAuthorization().getRole((Authorization) ois.readObject());
                        System.out.println("Authorization | Client " + clientSocket.getPort());
                        oos.writeObject(role);
                    }
                    // User
                    case "registration" -> {
                        User user = (User) ois.readObject();
                        Authorization authorization = (Authorization) ois.readObject();
                        dbFactory.getUser().insert(user, authorization);
                        System.out.println("Registration | Client " + clientSocket.getPort());
                    }
                    case "showUsersTable" -> {
                        oos.writeObject(dbFactory.getUser().select(0));
                        System.out.println("Show users table | Client " + clientSocket.getPort());
                    }
                    case "deleteUser" -> {
                        int idUser = Integer.parseInt(ois.readObject().toString());
                        if (!dbFactory.getUser().delete(idUser)) {
                            oos.writeObject("error");
                        }
                        else {
                            oos.writeObject("ok");
                        }
                        System.out.println("Delete user | Client " + clientSocket.getPort());
                    }
                    case "editUser" -> {
                        User user = (User) ois.readObject();
                        dbFactory.getUser().update(user);
                        System.out.println("Update user's info | Client " + clientSocket.getPort());
                    }
                    // Agent
                    case "showAgentsTable" -> {
                        oos.writeObject(dbFactory.getAgent().select(0));
                        System.out.println("Show agents table | Client " + clientSocket.getPort());
                    }
                    case "deleteAgent" -> {
                        int idUser = Integer.parseInt(ois.readObject().toString());
                        if (!dbFactory.getAgent().delete(idUser)) {
                            oos.writeObject("error");
                        }
                        else {
                            oos.writeObject("ok");
                        }
                        System.out.println("Delete agent | Client " + clientSocket.getPort());
                    }
                    case "editAgent" -> {
                        Agent agent = (Agent) ois.readObject();
                        dbFactory.getAgent().update(agent);
                        System.out.println("Update agent's info | Client " + clientSocket.getPort());
                    }
                    case "addAgent" -> {
                        Authorization authorization = (Authorization) ois.readObject();
                        Agent agent = (Agent) ois.readObject();
                        dbFactory.getAgent().insert(agent, authorization);
                        System.out.println("Add agent | Client " + clientSocket.getPort());
                    }
                    // Insurance
                    case "showInsurancesTable" -> {
                        oos.writeObject(dbFactory.getInsurance().select(0));
                        System.out.println("Show insurances table | Client " + clientSocket.getPort());
                    }
                    case "deleteInsurance" -> {
                        int idService = Integer.parseInt(ois.readObject().toString());
                        dbFactory.getInsurance().delete(idService);
                        System.out.println("Delete insurance | Client " + clientSocket.getPort());
                    }
                    case "editInsurance" -> {
                        Insurance insurance = (Insurance) ois.readObject();
                        dbFactory.getInsurance().update(insurance);
                        System.out.println("Update insurance | Client " + clientSocket.getPort());
                    }
                    case "addInsurance" -> {
                        Insurance insurance = (Insurance) ois.readObject();
                        dbFactory.getInsurance().insert(insurance);
                        System.out.println("Add insurance | Client " + clientSocket.getPort());
                    }
                    // Contract
                    case "showUserContracts" -> {
                        oos.writeObject(dbFactory.getContract().select(Global.user.getIdUser()));
                        System.out.println("Show users contracts | Client " + clientSocket.getPort());
                    }
                    case "showAgentContracts" -> {
                        oos.writeObject(dbFactory.getContract().select(Global.agent.getIdUser()));
                        System.out.println("Show agents contracts | Client " + clientSocket.getPort());
                    }
                    case "addContract" -> {
                        Contract contract = (Contract) ois.readObject();
                        dbFactory.getContract().insert(contract);
                        System.out.println("Add contract | Client " + clientSocket.getPort());
                    }
                    case "deleteContract" -> {
                        int idContract = Integer.parseInt(ois.readObject().toString());
                        dbFactory.getContract().delete(idContract);
                        System.out.println("Delete contract | Client " + clientSocket.getPort());
                    }
                    case "editContract" -> {
                        Contract contract = (Contract) ois.readObject();
                        dbFactory.getContract().update(contract);
                        System.out.println("Update contract | Client " + clientSocket.getPort());
                    }
                    case "changeStatus" -> {
                        int idContract = Integer.parseInt(ois.readObject().toString());
                        int idStatus = Integer.parseInt(ois.readObject().toString());
                        dbFactory.getContract().changeStatus(idContract, idStatus);
                        oos.writeObject(Global.agent.getSurname());
                        System.out.println("Change status | Client " + clientSocket.getPort());
                    }
                    // Diagrams
                    case "showStatusReport" -> {
                        oos.writeObject(dbFactory.getContract().getReport());
                        System.out.println("Show status diagram | Client " + clientSocket.getPort());
                    }
                    case "showTxtReport" -> {
                        oos.writeObject(dbFactory.getContract().getFinancialReport());
                        System.out.println("Show txt report | Client " + clientSocket.getPort());
                    }
                    case "showAgentsReport" -> {
                        oos.writeObject(dbFactory.getContract().getAgentsReport());
                        System.out.println("Show agents report | Client " + clientSocket.getPort());
                    }
                }
            }
        } catch (SocketException e) {
            System.out.println("Client " + clientSocket.getPort() + " disconnected");
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

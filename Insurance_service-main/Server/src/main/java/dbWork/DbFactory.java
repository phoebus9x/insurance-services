package dbWork;

public class DbFactory extends AbstractFactory {
    @Override
    public DbUser getUser() {
        return DbUser.getInstance();
    }

    @Override
    public DbAuthorization getAuthorization() {
        return DbAuthorization.getInstance();
    }

    @Override
    public DbAdmin getAdmin() {
        return DbAdmin.getInstance();
    }

    @Override
    public DbAgent getAgent() {
        return DbAgent.getInstance();
    }

    @Override
    public DbInsurance getInsurance() {
        return DbInsurance.getInstance();
    }

    @Override
    public DbContract getContract() {
        return DbContract.getInstance();
    }
}

import Gestion_admin.Database_Connection;
import Gestion_admin.Gestion_Personne;

/**
 * Class main du projet
 *
 * @author Célia
 */
public class Main {

    public static void main(String[] argv) {
        Database_Connection database = new Database_Connection();
        Login log_etud = new Login(20160024, "bla");
        log_etud.LogIn();
        Login log_admin = new Login(00000000, "admin");
        log_admin.LogIn();
    }
}
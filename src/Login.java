import Gestion_admin.Database_Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Login
{
    private final Integer matricule;
    private final String password;

    public Login(Integer matricule, String password)
    {
        this.matricule = matricule;
        this.password = password;
    }


    public void LogIn()
    {
        String sql = "SELECT Matricule, Password FROM ";
        if (Find_LogIn(sql, "etudiant"))
        {
            /* LANCER INTERFACE ETUDIANT*/
        }
        else if (Find_LogIn(sql, "professeur"))
        {
            /*LANCER INTERFACE PROF*/
        }
        else if (Find_LogIn(sql, "administration"))
        {
            /*LANCER INTERFACE ADMIN*/
        }

        System.out.println("PERSON NOT FOUND");
    }

    private boolean Find_LogIn(String sql, String table)
    {
        Database_Connection database = new Database_Connection();
        ResultSet data = database.run_Statement_READ(sql + table);
        int res = check_Validity(data);

        if (res == 1)
        {
            System.out.println("PERSON FOUND : IT IS A " + table);
            database.Database_Deconnection();
            return true;
        }
        else if (res == 0)
        {
            System.out.println("PERSON FOUND: PASSWORD INCORRECT");
        }
        database.Database_Deconnection();
        return false;
    }

    private int check_Validity(ResultSet data)
    {
        try
        {
            while (data.next())
            {
                Integer matriculeData = data.getInt("Matricule");
                String passwordData = data.getString("Password");
                if (matriculeData.equals(matricule))
                {
                    if (passwordData.equals(password))
                    {
                        //CONNEXION VALIDATED
                        return 1;
                    }
                    //THE PASSWORD IS INCORRECT
                    return 0;
                }
            }
            //MATRICULE NOT FOUND IN THIS TABLE
            return -1;
        }
        catch (SQLException ignore)
        {
            return -1;
        }
    }
}

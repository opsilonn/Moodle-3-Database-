package GUI;


import GUI_Components.*;
import javax.swing.*;
import java.awt.*;


/**
 * Fenêtre permettant de se connecter au logiciel (version 4).
 *
 * Cette classe hérite de {@link CustomJFrame}
 *
 * @author Hugues
 */
public class GUI_Login extends CustomJFrame
{
    private static final int DIM_X = 500;
    private static final int DIM_Y = 500;

    private JPanel panel;
    private JPanel panelLogo;
    private JPanel panelInputs;
    private JPanel panelForm;

    private JLabel labelLogo;

    public JTextField fieldID;
    public JPasswordField fieldPassword;

    public JButton buttonLogin;
    public JLabel labelIncorrect;

    /**
     * Création de l'interface de login
     */
    public GUI_Login()
    {
        super("Login", true, DIM_X, DIM_Y);

        // Adds the logo image
        ImageIcon imageIcon = new ImageIcon(PATH_LOGO_FULL); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance((int) (DIM_X * 0.6), DIM_Y /3,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);  // transform it back
        labelLogo.setIcon(imageIcon);


        labelIncorrect.setVisible(false);
        buttonLogin.addActionListener( e -> loginVerifier() );


        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }


    private void createUIComponents()
    {
        fieldID = new CustomJTextField("NUMERIC",  false, 8);
        fieldPassword = new CustomJTextField("ALL",  true, 20);
    }


    private void loginVerifier()
    {
        /*
        int inputID = 0;
        String inputPassword = "";

        Professeur userProf = null;
        Eleve userEleve = null;


        // On vérifie la taille de l'input ID : si nulle, on évite toute vérification
        if(fieldID.getText().length() != 0)
        {
            inputID = Integer.parseInt(fieldID.getText());
            inputPassword = new String(fieldPassword.getPassword());


            // On vérifie s'il y a correspondance avec un PROFESSEUR
            for (Professeur prof : ecole.getProfesseur())
            {
                if( prof.getID() == inputID &&  Objects.equals(prof.getPassword(), inputPassword) )
                    userProf = prof;
            }


            // On vérifie s'il y a correspondance avec un ELEVE

            // On charge toutes les promotions
            for ( Map.Entry<String, Promotion> promo : ecole.getPromo().entrySet() )
            {
                // Dans chaque Promotion, on vérifie chaque élève
                for (Eleve eleve : promo.getValue().getEleves())
                {
                    if( eleve.getID() == inputID && Objects.equals(eleve.getPassword(), inputPassword) )
                        userEleve = eleve;
                }
            }
        }


        // On agit en conséquence : ouverture de fenêtre appropriée si Login réussi, sinon message d'erreur
        if(userProf != null || userEleve != null)
        {
            // Ouverture de la page appropriée
            CustomJFrame mainWindow;
            if( userProf != null )  mainWindow = new GUI_Professeur(userProf, ecole);
            if( userEleve != null ) mainWindow = new GUI_Eleve(userEleve, ecole);


            // Fermeture du login
            this.dispose();
        }
        else
            labelIncorrect.setVisible(true);
            */
    }
}
package modelLivre;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DaoLivre implements ILivre {
    private static Connection conn = null;
    private static DaoLivre instanceDao = null;

    // MySQL
    //private static final String PILOTE = "com.mysql.jdbc.Driver";
    private static final String FICHIER_TXT = "src/livres.txt";
    private static final String URL_BD = "jdbc:mysql://localhost/bd_Biblio";
    private static final String USAGER = "root";
    private static final String PASS = "";

    private static final String SUPPRIMER = "DELETE FROM livre WHERE numLivre=?";
    private static final String GET_ALL = "SELECT * FROM livre ORDER BY numLivre";
    private static final String GET_BY_ID = "SELECT * FROM livre WHERE numLivre=?";
    private static final String GET_BY_TITRE = "SELECT * FROM livre WHERE titreLivre=?";
    private static final String ENREGISTRER = "INSERT INTO livre VALUES(?,?, ?,?,?, ?)";
    private static final String MODIFIER = "UPDATE livre SET numLivre=?,titreLivre=?,Auteur=?,Annee=?,Pages=?,Cathegorie=? WHERE numLivre=?";

    // Singleton de connexion à la BD
    // getConnexion() est devenu une zonne critique. 
    // Pour ne pas avoir deux processus légers (threads) qui
    // appellent au même temps getConnexion

    private DaoLivre(){};

    public void remplirBD() {
        String ligne;
        String []elems;
    try {
        BufferedReader  data = new BufferedReader(new InputStreamReader(new FileInputStream(FICHIER_TXT),StandardCharsets.ISO_8859_1));
        
        ligne = data.readLine().trim();//Lire la premi re ligne du FICHIER
        int c=0;
        while (ligne != null) {//Si ligne == null alors ont a atteint la fin du FICHIER
           elems=ligne.split(";");
           System.out.println(ligne);
           Livre l=new Livre(Integer.parseInt(elems[0]),elems[1],Integer.parseInt(elems[2]),Integer.parseInt(elems[3]),Integer.parseInt(elems[4]),elems[5]);
           System.out.println(MdlLivre_Enregistrer(l)+c);
           ligne = data.readLine();
           c++;
        }
       data.close();
        
    } catch (Exception e) {
        // TODO: handle exception
    } 
  
    }
    public void createDB() throws IOException {
		//Getting the connection
    if(conn == null){    
	 
            try {
                //conn = DriverManager.getConnection("jdbc:mysql://localhost/", USAGER,PASS);
                conn = DriverManager.getConnection(URL_BD, USAGER, PASS);
                    //Creating the Statement
                
                Statement stmt1 = conn.createStatement();
                // Statement stmt2 = conn.createStatement();
                //Query to create a database
                String query1 = "CREATE database bd_Biblio";
                String query2="alter database bd_Biblio charset=utf8";
                String query3 = "USE bd_Biblio";
                String query4 = "CREATE TABLE livre (numLivre int ,titreLivre varchar(100),Auteur int,Annee int,Pages int,Cathegorie varchar(30))";
                //Executing the query
                stmt1.execute(query1);
                stmt1.execute(query2);
                
                stmt1.execute(query3);
                stmt1.execute(query4);
                System.out.println("Database created");
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("Connection established......");
        }

  }    
    public static synchronized DaoLivre getLivreDao () {
        try {
            // Class.forName(PILOTE);
            if (instanceDao == null) {
                instanceDao = new DaoLivre();
                conn = DriverManager.getConnection(URL_BD, USAGER, PASS);
            }
            return instanceDao;
        } catch (Exception e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public String MdlLivre_Enregistrer(Livre livre) {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(ENREGISTRER, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, livre.getNum());
            stmt.setString(2, livre.getTitre());
            stmt.setInt(3, livre.getAuteur());
            stmt.setInt(4, livre.getAnnee());
            stmt.setInt(5, livre.getPages());
            stmt.setString(6, livre.getCathegorie());
           
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                livre.setNum(rs.getInt(1));
            }
            return "Livre est bien enregistré";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            MdlLivre_Fermer(stmt);
            MdlLivre_Fermer(conn);
        }
    }

    // Read
    public List<Livre> MdlLivre_GetAll() {
        PreparedStatement stmt = null;
        List<Livre> listeLivres = new ArrayList<Livre>();

        try {
            stmt = conn.prepareStatement(GET_ALL);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Livre livre = new Livre();
                livre.setNum(rs.getInt("numLivre"));
                livre.setTitre(rs.getString("titreLivre"));
                livre.setNum(rs.getInt("numLivre"));
                livre.setNum(rs.getInt("numLivre"));
                livre.setNum(rs.getInt("numLivre"));
                livre.setTitre(rs.getString("titreLivre"));

                listeLivres.add(livre);
            }
        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            MdlLivre_Fermer(stmt);
            MdlLivre_Fermer(conn);
        }

        return listeLivres;
    }

    public Livre MdlLivre_GetByID(int numLivre) {
        PreparedStatement stmt = null;

        try {

            stmt = conn.prepareStatement(GET_BY_ID);
            stmt.setInt(1, numLivre);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Livre livre = new Livre();
                livre.setNum(rs.getInt("numLivre"));
                livre.setTitre(rs.getString("titreLivre"));
                livre.setNum(rs.getInt("numLivre"));
                livre.setNum(rs.getInt("numLivre"));
                livre.setNum(rs.getInt("numLivre"));
                livre.setTitre(rs.getString("titreLivre"));

                return livre;
            } else {
                return null;
            }
        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            MdlLivre_Fermer(stmt);
            MdlLivre_Fermer(conn);
        }
    }

    public Livre MdlLivre_GetByLoc(String loc) {
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(GET_BY_TITRE);
            stmt.setString(1, loc);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Livre livre = new Livre();
                livre.setNum(rs.getInt("numLivre"));
                livre.setTitre(rs.getString("titreLivre"));
                livre.setNum(rs.getInt("numLivre"));
                livre.setNum(rs.getInt("numLivre"));
                livre.setNum(rs.getInt("numLivre"));
                livre.setTitre(rs.getString("titreLivre"));

                return livre;
            } else {
                return null;
            }
        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            MdlLivre_Fermer(stmt);
            MdlLivre_Fermer(conn);
        }
    }

    // Update, faudrat avant appeler MdlF_GetById(idf) pour obtenir
    // les données du film à modifier via une interface et après envoyer 
    // ce film à MdlF_Modifier(film) pour faire la mise à  jour.
    public int MdlLivre_Modifier(Livre livre) {
        PreparedStatement stmt = null;
       ;
        try {
            stmt = conn.prepareStatement(MODIFIER);
            stmt.setInt(1, livre.getNum());
            stmt.setString(2, livre.getTitre());
            stmt.setInt(3, livre.getAuteur());
            stmt.setInt(4, livre.getAnnee());
            stmt.setInt(5, livre.getPages());
            stmt.setString(6, livre.getCathegorie());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            MdlLivre_Fermer(stmt);
            MdlLivre_Fermer(conn);
        }
    }

    // Delete
    public int MdlLivre_Supprimer(int livreno) {
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(SUPPRIMER);
            stmt.setInt(1, livreno);

            return stmt.executeUpdate();
        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            MdlLivre_Fermer(stmt);
            MdlLivre_Fermer(conn);
        }
    }
   
    private static void MdlLivre_Fermer(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    private static void MdlLivre_Fermer(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                // e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
    public Connection getConn() {
        return conn;
    }

}

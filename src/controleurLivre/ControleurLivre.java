package controleurLivre;

import java.io.IOException;

import modelLivre.DaoLivre;
import modelLivre.Livre;

public class ControleurLivre implements IActionsLivre {

    private static ControleurLivre CtrLivre_Instance = null;
    private static DaoLivre Dao_Instance = null;
    // Singleton du contrôleur
    // getControleurFilm() est devenu une zonne critique.
    // Pour ne pas avoir deux processus légers (threads) qui
    // appellent au même temps getConnexion
    private ControleurLivre(){}

    public static synchronized ControleurLivre getControleurLivre() {
        try {
            if (CtrLivre_Instance == null) {
                CtrLivre_Instance = new ControleurLivre();
                Dao_Instance = DaoLivre.getLivreDao();
            }
            return CtrLivre_Instance;
        } catch (Exception e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public String CtrLivre_Enregistrer(Livre livre) {
        String message = null;
        message = Dao_Instance.MdlLivre_Enregistrer(livre);
        return message;
    }

    @Override
    public void CtrLivre_ChargerDB() {
        try {
                if(Dao_Instance.getConn()==null){

                    Dao_Instance.createDB();
                }
                Dao_Instance.remplirBD();
            } catch (IOException e) {
                e.printStackTrace();
            }
        
    }


}

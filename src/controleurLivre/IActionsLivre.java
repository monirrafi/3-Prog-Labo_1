package controleurLivre;

import modelLivre.Livre;

public interface IActionsLivre {
    // Pour le CRUD - Create Read Update Delete
    public void CtrLivre_ChargerDB();
    // Create
    public String CtrLivre_Enregistrer(Livre livre);
    
    // // Read
    // public List<Film> CtrF_GetAllFilms();

    // public Film CtrF_GetFilmById(int idf);

    // public Film CtrF_GetFilmByTitre(String titre);

    // // Update
    // public int CtrF_Modifier(Film user);

    // // Delete
     //public int CtrD_Enlever(int deptno); 
}

import controleurLivre.*;


public final class Application {
    private Application() {
    }

    public static void main(String[] args) {
        
        ControleurLivre ctrLivre = ControleurLivre.getControleurLivre();
		ctrLivre.CtrLivre_ChargerDB();
	}
}
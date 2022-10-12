public class tableIndex {
    private long adr;
    private long taille;
    private int status;
    
    public tableIndex(long adr, long taille, int status) {
        this.adr = adr;
        this.taille = taille;
        this.status = status;
    }
    public long getAdr() {
        return adr;
    }
    public void setAdr(long adr) {
        this.adr = adr;
    }
    public long getTaille() {
        return taille;
    }
    public void setTaille(long taille) {
        this.taille = taille;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    
    
}

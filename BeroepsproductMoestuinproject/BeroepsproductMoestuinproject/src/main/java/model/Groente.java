package model;

public class Groente {
    
    private String groente;
    
    public Groente(String groente){
        this.groente = groente;
    }

    public String getGroente() {
        return groente;
    }

    public void setGroente(String groente) {
        this.groente = groente;
    }
    
    @Override
    public String toString() {
        return groente;
    }
    
}

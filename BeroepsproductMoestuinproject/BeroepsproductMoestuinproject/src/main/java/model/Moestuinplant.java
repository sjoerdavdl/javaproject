package model;

import java.time.LocalDate;

public class Moestuinplant {
    
    private Groente groente;
    private Moestuin moestuinnaam;
    private LocalDate datum;

    public Moestuinplant(Groente groente, Moestuin moestuinnaam, 
            LocalDate datum) {
        this.groente = groente;
        this.moestuinnaam = moestuinnaam;
        this.datum = datum;
    }

    public Groente getGroente() {
        return groente;
    }

    public void setGroente(Groente groente) {
        this.groente = groente;
    }

    public Moestuin getMoestuinnaam() {
        return moestuinnaam;
    }

    public void setMoestuinnaam(Moestuin moestuinnaam) {
        this.moestuinnaam = moestuinnaam;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }
    
    @Override
    public String toString() {
        // controleer eerst of de objecten niet null zijn
        String gGroente = (groente != null) ? groente.getGroente() : "Onbekend";
        String mMoes = (moestuinnaam != null) ? moestuinnaam.getMoestuinnaam() : "Onbekend";
        String d = (datum != null) ? datum.toString() : "Onbekend";

    return gGroente + " - " + mMoes + " (" + d + ")";
    }
    
}

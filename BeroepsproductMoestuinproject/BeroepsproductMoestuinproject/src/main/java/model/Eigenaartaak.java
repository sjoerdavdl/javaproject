package model;

import java.time.LocalDate;

public class Eigenaartaak {
    
    private Bewoner bewoner;
    private Taak taakomschrijving;
    private LocalDate datum;
    
    public Eigenaartaak(Bewoner bewoner, Taak taakomschrijving, LocalDate datum){
        this.bewoner = bewoner;
        this.taakomschrijving = taakomschrijving;
        this.datum = datum;
    }

    public Bewoner getBewoner() {
        return bewoner;
    }

    public void setBewoner(Bewoner bewoner) {
        this.bewoner = bewoner;
    }

    public Taak getTaakomschrijving() {
        return taakomschrijving;
    }

    public void setTaakomschrijving(Taak taakomschrijving) {
        this.taakomschrijving = taakomschrijving;
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
        String bNaam = (bewoner != null) ? bewoner.getBewonernaam() : "Onbekend";
        String tOmsch = (taakomschrijving != null) ? taakomschrijving.getTaakomschrijving() : "Onbekend";
        String d = (datum != null) ? datum.toString() : "Onbekend";

    return bNaam + " - " + tOmsch + " (" + d + ")";
    }

}

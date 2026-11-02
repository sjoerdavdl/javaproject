package model;

public class Moestuineigenaar {
    
    private Moestuin moestuinnaam;
    private Bewoner bewoner;
    private Adres adres;

    public Moestuineigenaar(Moestuin moestuinnaam, Bewoner bewoner, Adres adres) {
        this.moestuinnaam = moestuinnaam;
        this.bewoner = bewoner;
        this.adres = adres;
    }
    
    public Moestuin getMoestuinnaam() {
        return moestuinnaam;
    }

    public void setMoestuinnaam(Moestuin moestuinnaam) {
        this.moestuinnaam = moestuinnaam;
    }

    public Bewoner getBewoner() {
        return bewoner;
    }

    public void setBewoner(Bewoner bewoner) {
        this.bewoner = bewoner;
    }

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }
    
    @Override
    public String toString() {
        // controleer eerst of de objecten niet null zijn
        String mNaam = (moestuinnaam != null) ? moestuinnaam.getMoestuinnaam() : "Onbekend";
        String bNaam= (bewoner != null) ? bewoner.getBewonernaam() : "Onbekend";
        String aAdr = (adres != null) ? adres.getAdres() : "Onbekend";

    return mNaam + " - " + bNaam + " - " + aAdr;
    }
    
}

package model;

public class Adres {
    
    private String adres;
    
    public Adres(String adres){
        
        this.adres = adres;

    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }
    
    @Override
    public String toString() {
        return adres;
    }
    
}

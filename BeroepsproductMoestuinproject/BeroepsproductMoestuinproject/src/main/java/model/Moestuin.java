package model;

public class Moestuin {
    
    private String moestuinnaam;
    
    public Moestuin(String moestuinnaam){
        this.moestuinnaam = moestuinnaam;
    }

    public String getMoestuinnaam() {
        return moestuinnaam;
    }

    public void setMoestuinnaam(String moestuinnaam) {
        this.moestuinnaam = moestuinnaam;
    }
    
    @Override
    public String toString() {
        return moestuinnaam;
    }
    
}

package model;

public class Bewoner {
    
    private String bewonernaam;
    
    public Bewoner(String bewonernaam){
        this.bewonernaam = bewonernaam;
    }

    public String getBewonernaam() {
        return bewonernaam;
    }

    public void setBewonernaam(String bewonernaam) {
        this.bewonernaam = bewonernaam;
    }
    
    @Override
    public String toString() {
        return bewonernaam;
    }
    
}

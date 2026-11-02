package model;

public class Taak {
    
    private String taakomschrijving;
    
    public Taak(String taakomschrijving){
        this.taakomschrijving = taakomschrijving;
    }

    public String getTaakomschrijving() {
        return taakomschrijving;
    }

    public void setTaakomschrijving(String taakomschrijving) {
        this.taakomschrijving = taakomschrijving;
    }
    
    @Override
    public String toString() {
        return taakomschrijving;
    }
    
}

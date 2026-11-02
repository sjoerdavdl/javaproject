package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import model.Bewoner;
import model.DBCPDatasource;
import model.Eigenaartaak;
import model.Taak;

public class EigenaartaakScherm {
    
    private Label eigtaakLabel = new Label("Eigenaartaken");
    private Label bewonerLabel = new Label("Bewoner");
    private Label taakLabel = new Label("Taak");
    private Label datumLabel = new Label("Datum");
    private Button createButton = new Button("Create");
    private Button readButton = new Button("Read");
    private Button deleteButton = new Button("Delete");
    private ComboBox<String> taakBox;
    private ComboBox<String> bewonerBox;
    private DatePicker datumprikker = new DatePicker();
    private ListView<Eigenaartaak> eigenaartaakList = new ListView();
    private ArrayList<Eigenaartaak> allEigtaken = new ArrayList();
    
    public EigenaartaakScherm(Pane root){
        
        taakBox = new ComboBox<>();
        bewonerBox = new ComboBox<>();
        datumprikker = new DatePicker();
        
        taakBox.setPrefWidth(100);
        bewonerBox.setPrefWidth(100);
        datumprikker.setPrefWidth(130);
        
        //elementen op de juiste plek zetten
        eigtaakLabel.relocate(10, 10);
        bewonerLabel.relocate(10, 30);
        bewonerBox.relocate(10, 50);
        taakLabel.relocate(120, 30);
        taakBox.relocate(120, 50);
        datumLabel.relocate(230, 30);
        datumprikker.relocate(230, 50);
        createButton.relocate(10, 80);   
        readButton.relocate(70, 80);
        deleteButton.relocate(130, 80);
        eigenaartaakList.relocate(10, 110);
        
        laadBewoners();
        laadTaken();
        
        createButton.setOnAction(e -> {
                        
            String bewonernaam = bewonerBox.getValue();
            String taakomschrijving = taakBox.getValue();
            LocalDate datum = datumprikker.getValue();
            
            if (bewonernaam == null || taakomschrijving == null || datum == null) {
                System.out.println("Selecteer alle velden eerst!");
            return;
            }
            
            int iResult = createEigenaartaak(bewonernaam, 
                    taakomschrijving, datum);
            System.out.println("Aantal rijen toegevoegd: " + iResult);
            
        });
        
        readButton.setOnAction((ActionEvent e) -> {
            allEigtaken.clear();
            eigenaartaakList.getItems().clear();
            
            ResultSet r = null;
            try {
                r = readTaak("SELECT * from eigenaartaken");
                
                while (r.next()) {
                    
                    String bewonerNaam = r.getString("bewoners_bewonernaam");
                    String taakOmsch = r.getString("taken_taakomschrijving");
                    LocalDate datum = r.getDate("datum").toLocalDate();

                    // Maak objecten
                    Bewoner bewoner = new Bewoner(bewonerNaam);
                    Taak taak = new Taak(taakOmsch);

                    Eigenaartaak et = new Eigenaartaak(bewoner, taak, datum);
                    et.setBewoner(bewoner);
                    et.setTaakomschrijving(taak);
                    et.setDatum(datum);

                    allEigtaken.add(et);
                    System.out.println("Bewoner: " + bewonerNaam + ", Taak: " + taakOmsch + ", Datum: " + datum);
                }
            }   catch (Exception se) {
                se.printStackTrace();
            }
            
            for (Eigenaartaak et : allEigtaken) { // for - each
                eigenaartaakList.getItems().add(et);
            }
        });
        
        deleteButton.setOnAction(e -> {
            
            Eigenaartaak geselecteerdeEigenaartaak =
                eigenaartaakList.getSelectionModel().getSelectedItem();

            if (geselecteerdeEigenaartaak == null) {
                System.out.println("Geen eigenaartaak geselecteerd");
                return;
        }

        // verwijderen uit database
        deleteGroente(geselecteerdeEigenaartaak);

        // verwijderen uit ListView
        eigenaartaakList.getItems().remove(geselecteerdeEigenaartaak);
            
        });
        
        root.getChildren().addAll(eigtaakLabel, bewonerBox, taakBox, datumprikker, 
                bewonerLabel, taakLabel, datumLabel, createButton, readButton, 
                deleteButton, eigenaartaakList);
                
    }
    
    private void laadBewoners() {
        try (Connection conn = DBCPDatasource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT bewonernaam FROM bewoners")) {

            while (rs.next()) {
                bewonerBox.getItems().add(rs.getString("bewonernaam"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void laadTaken() {
        try (Connection conn = DBCPDatasource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT taakomschrijving "
                     + "FROM taken")) {

            while (rs.next()) {
                taakBox.getItems().add(rs.getString("taakomschrijving"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int createEigenaartaak(String bewonernaam, String 
               taakomschrijving, LocalDate datum) {
        String sql = "INSERT INTO eigenaartaken (bewoners_bewonernaam,"
                + " taken_taakomschrijving, datum) VALUES (?, ?, ?)";
        int result = 0;
       
        try (Connection conn = DBCPDatasource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bewonernaam);
            pstmt.setString(2, taakomschrijving);
            pstmt.setDate(3, java.sql.Date.valueOf(datum));
            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return result;
        
    }
    
    public ResultSet readTaak(String sql){
        
        ResultSet result = null;
        try {
            Connection con = DBCPDatasource.getConnection();
            Statement stat = con.createStatement();
            result = stat.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Niet mijn fout !!!");
        }
        return result;
    }
    
    public void deleteGroente(Eigenaartaak eigenaartaak) {
        String sql = "DELETE FROM eigenaartaken WHERE bewoners_bewonernaam = ? "
                + "AND taken_taakomschrijving = ? AND datum = ?";
        
        try (Connection con = DBCPDatasource.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, eigenaartaak.getBewoner().getBewonernaam());
            ps.setString(2, eigenaartaak.getTaakomschrijving().getTaakomschrijving());
            ps.setDate(3, java.sql.Date.valueOf(eigenaartaak.getDatum()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
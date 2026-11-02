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
import model.Groente;
import model.DBCPDatasource;
import model.Moestuinplant;
import model.Moestuin;

public class MoestuinplantScherm {
    
    private Label moestuinplantLabel = new Label("Moestuinplanten");
    private Label groenteLabel = new Label("Groente");
    private Label moestuinLabel = new Label("Moestuin");
    private Label datumLabel = new Label("Datum");
    private Button createButton = new Button("Create");
    private Button readButton = new Button("Read");
    private Button deleteButton = new Button("Delete");
    private ComboBox<String> groenteBox;
    private ComboBox<String> moestuinBox;
    private DatePicker datumprikker = new DatePicker();
    private ListView<Moestuinplant> moestuinplantList = new ListView();
    private ArrayList<Moestuinplant> allMoestuinplanten = new ArrayList();
    
    public MoestuinplantScherm(Pane root){
        
        groenteBox = new ComboBox<>();
        moestuinBox = new ComboBox<>();
        datumprikker = new DatePicker();
        
        groenteBox.setPrefWidth(100);
        moestuinBox.setPrefWidth(100);
        datumprikker.setPrefWidth(130);
        
        //elementen op de juiste plek zetten
        moestuinplantLabel.relocate(10, 10);
        groenteLabel.relocate(10, 30);
        groenteBox.relocate(10, 50);
        moestuinLabel.relocate(120, 30);
        moestuinBox.relocate(120, 50);
        datumLabel.relocate(230, 30);
        datumprikker.relocate(230, 50);
        createButton.relocate(10, 80);   
        readButton.relocate(70, 80);
        deleteButton.relocate(130, 80);
        moestuinplantList.relocate(10, 110);
        
        laadGroenten();
        laadMoestuinen();
        
        createButton.setOnAction(e -> {
                        
            String groente = groenteBox.getValue();
            String moestuinnaam = moestuinBox.getValue();
            LocalDate datum = datumprikker.getValue();
            
            if (groente == null || moestuinnaam == null || datum == null) {
                System.out.println("Selecteer alle velden eerst!");
            return;
            }
            
            int iResult = createMoestuinplant(groente, 
                    moestuinnaam, datum);
            System.out.println("Aantal rijen toegevoegd: " + iResult);
            
        });
        
        readButton.setOnAction((ActionEvent e) -> {
            allMoestuinplanten.clear();
            moestuinplantList.getItems().clear();
            
            ResultSet r = null;
            try {
                r = readMoestuinplant("SELECT * from moestuinplanten");
                
                while (r.next()) {
                    
                    String groentenaam = r.getString("groenten_groente");
                    String moestuinnaam = r.getString("moestuinen_moestuinnaam");
                    LocalDate datum = r.getDate("datum").toLocalDate();

                    // Maak objecten
                    Groente groente = new Groente(groentenaam);
                    Moestuin moestuin = new Moestuin(moestuinnaam);

                    Moestuinplant mp = new Moestuinplant(groente, moestuin, datum);
                    mp.setGroente(groente);
                    mp.setMoestuinnaam(moestuin);
                    mp.setDatum(datum);

                    allMoestuinplanten.add(mp);
                    System.out.println("Groente: " + groentenaam + 
                            ", Moestuin: " + moestuinnaam + ", Datum: " + datum);
                }
            }   catch (Exception se) {
                se.printStackTrace();
            }
            
            for (Moestuinplant mp : allMoestuinplanten) { // for - each
                moestuinplantList.getItems().add(mp);
            }
        });
        
        deleteButton.setOnAction(e -> {
            
            Moestuinplant geselecteerdeMoestuinplant =
                moestuinplantList.getSelectionModel().getSelectedItem();

            if (geselecteerdeMoestuinplant == null) {
                System.out.println("Geen moestuinplant geselecteerd");
                return;
        }

        // verwijderen uit database
        deleteMoestuinplant(geselecteerdeMoestuinplant);

        // verwijderen uit ListView
        moestuinplantList.getItems().remove(geselecteerdeMoestuinplant);
            
        });
        
        root.getChildren().addAll(moestuinplantLabel, groenteBox, moestuinBox, 
                datumprikker, groenteLabel, moestuinLabel, datumLabel, createButton, readButton, 
                deleteButton, moestuinplantList);
               
    }
    
    private void laadGroenten() {
        try (Connection conn = DBCPDatasource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT groente FROM groenten")) {

            while (rs.next()) {
                groenteBox.getItems().add(rs.getString("groente"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void laadMoestuinen() {
        try (Connection conn = DBCPDatasource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT moestuinnaam "
                     + "FROM moestuinen")) {

            while (rs.next()) {
                moestuinBox.getItems().add(rs.getString("moestuinnaam"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int createMoestuinplant(String groente, String 
               moestuin, LocalDate datum) {
        String sql = "INSERT INTO moestuinplanten (groenten_groente,"
                + " moestuinen_moestuinnaam, datum) VALUES (?, ?, ?)";
        int result = 0;
       
        try (Connection conn = DBCPDatasource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, groente);
            pstmt.setString(2, moestuin);
            pstmt.setDate(3, java.sql.Date.valueOf(datum));
            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return result;
        
    }
    
    public ResultSet readMoestuinplant(String sql){
        
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
    
    public void deleteMoestuinplant(Moestuinplant moestuinplant) {
        String sql = "DELETE FROM moestuinplanten WHERE groenten_groente = ? "
                + "AND moestuinen_moestuinnaam = ? AND datum = ?";
        
        try (Connection con = DBCPDatasource.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, moestuinplant.getGroente().getGroente());
            ps.setString(2, moestuinplant.getMoestuinnaam().getMoestuinnaam());
            ps.setDate(3, java.sql.Date.valueOf(moestuinplant.getDatum()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}

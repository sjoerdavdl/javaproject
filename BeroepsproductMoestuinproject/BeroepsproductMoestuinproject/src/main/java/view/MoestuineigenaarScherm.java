package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import model.Moestuin;
import model.Bewoner;
import model.DBCPDatasource;
import model.Moestuineigenaar;
import model.Adres;

public class MoestuineigenaarScherm {
    
    private Label moestuineigLabel = new Label("Moestuineigenaar");
    private Label moestuinLabel = new Label("Moestuin");
    private Label bewonerLabel = new Label("Bewoner");
    private Label adresLabel = new Label("Adres");
    private Button createButton = new Button("Create");
    private Button readButton = new Button("Read");
    private Button deleteButton = new Button("Delete");
    private ComboBox<String> moestuinBox;
    private ComboBox<String> bewonerBox;
    private ComboBox<String> adresBox;
    private ListView<Moestuineigenaar> moestuinenList = new ListView();
    private ArrayList<Moestuineigenaar> allMoestuinen = new ArrayList();
    
    public MoestuineigenaarScherm(Pane root){
        
        moestuinBox = new ComboBox<>();
        bewonerBox = new ComboBox<>();
        adresBox = new ComboBox<>();
        
        moestuinBox.setPrefWidth(100);
        bewonerBox.setPrefWidth(100);
        adresBox.setPrefWidth(100);
        
        //elementen op de juiste plek zetten
        moestuineigLabel.relocate(10, 10);
        moestuinLabel.relocate(10, 30);
        moestuinBox.relocate(10, 50);
        bewonerLabel.relocate(120, 30);
        bewonerBox.relocate(120, 50);
        adresLabel.relocate(230, 30);
        adresBox.relocate(230, 50);
        createButton.relocate(10, 80);   
        readButton.relocate(70, 80);
        deleteButton.relocate(130, 80);
        moestuinenList.relocate(10, 110);
    
        laadMoestuinen();
        laadBewoners();
        laadAdressen();
        
        createButton.setOnAction(e -> {
                        
            String moestuinnaam = moestuinBox.getValue();
            String bewonernaam = bewonerBox.getValue();
            String adres = adresBox.getValue();
            
            if (moestuinnaam == null || bewonernaam == null || adres == null) {
                System.out.println("Selecteer alle velden eerst!");
            return;
            }
            
            int iResult = createMoestuineigenaar(moestuinnaam, 
                    bewonernaam, adres);
            System.out.println("Aantal rijen toegevoegd: " + iResult);
            
        });
        
        readButton.setOnAction((ActionEvent e) -> {
            allMoestuinen.clear();
            moestuinenList.getItems().clear();
            
            ResultSet r = null;
            try {
                r = readMoestuineigenaar("SELECT * from moestuineigenaren");
                
                while (r.next()) {
                    
                    String moestuinnaam = r.getString("moestuinen_moestuinnaam");
                    String bewonernaam = r.getString("bewoners_bewonernaam");
                    String adres = r.getString("adressen_adres");
                    // Maak objecten
                    Moestuin moestuin = new Moestuin(moestuinnaam);
                    Bewoner bewoner = new Bewoner(bewonernaam);
                    Adres adres2 = new Adres(adres);

                    Moestuineigenaar me = new Moestuineigenaar(moestuin, bewoner, adres2);
                    me.setMoestuinnaam(moestuin);
                    me.setBewoner(bewoner);
                    me.setAdres(adres2);

                    allMoestuinen.add(me);
                    System.out.println("Moestuin: " + moestuinnaam + 
                            ", Bewoner: " + bewonernaam + ", Adres: " + adres);
                }
            }   catch (Exception se) {
                se.printStackTrace();
            }
            
            for (Moestuineigenaar me : allMoestuinen) { // for - each
                moestuinenList.getItems().add(me);
            }
        });
        
        deleteButton.setOnAction(e -> {
            
            Moestuineigenaar geselecteerdeMoestuineigenaar =
                moestuinenList.getSelectionModel().getSelectedItem();

            if (geselecteerdeMoestuineigenaar == null) {
                System.out.println("Geen moestuineigenaar geselecteerd");
                return;
        }

        // verwijderen uit database
        deleteMoestuineigenaar(geselecteerdeMoestuineigenaar);

        // verwijderen uit ListView
        moestuinenList.getItems().remove(geselecteerdeMoestuineigenaar);
            
        });
        
        root.getChildren().addAll(moestuineigLabel, moestuinBox, bewonerBox, 
                adresBox, moestuinLabel, bewonerLabel, adresLabel, createButton, 
                readButton, deleteButton, moestuinenList);
                
    }
    
    private void laadMoestuinen() {
        try (Connection conn = DBCPDatasource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT moestuinnaam FROM moestuinen")) {

            while (rs.next()) {
                moestuinBox.getItems().add(rs.getString("moestuinnaam"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void laadBewoners() {
        try (Connection conn = DBCPDatasource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT bewonernaam "
                     + "FROM bewoners")) {

            while (rs.next()) {
                bewonerBox.getItems().add(rs.getString("bewonernaam"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void laadAdressen() {
        try (Connection conn = DBCPDatasource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT adres "
                     + "FROM adressen")) {

            while (rs.next()) {
                adresBox.getItems().add(rs.getString("adres"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
     public int createMoestuineigenaar(String moestuinnaam, String 
               bewonernaam, String adres) {
        String sql = "INSERT INTO moestuineigenaren (moestuinen_moestuinnaam,"
                + " bewoners_bewonernaam, adressen_adres) VALUES (?, ?, ?)";
        int result = 0;
       
        try (Connection conn = DBCPDatasource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, moestuinnaam);
            pstmt.setString(2, bewonernaam);
            pstmt.setString(3, adres);
            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return result;
        
    }
    
    public ResultSet readMoestuineigenaar(String sql){
        
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
    
    public void deleteMoestuineigenaar(Moestuineigenaar moestuineigenaar) {
        String sql = "DELETE FROM moestuineigenaren WHERE moestuinen_moestuinnaam = ? "
                + "AND bewoners_bewonernaam = ? AND adressen_adres = ?";
        
        try (Connection con = DBCPDatasource.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, moestuineigenaar.getMoestuinnaam().getMoestuinnaam());
            ps.setString(2, moestuineigenaar.getBewoner().getBewonernaam());
            ps.setString(3, moestuineigenaar.getAdres().getAdres());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}

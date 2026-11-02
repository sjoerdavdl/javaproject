package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import model.Bewoner;
import model.DBCPDatasource;

public class BewonerScherm {
    
    private Label bewonerLabel = new Label("Bewoner");
    private TextField bewonerVeld = new TextField();
    private Button createButton = new Button("Create");
    private Button readButton = new Button("Read");
    private Button deleteButton = new Button("Delete");
    private ListView<Bewoner> bewonerList;
    private ArrayList<Bewoner> bewoners = new ArrayList();
    
    public BewonerScherm(Pane root){
        
        bewonerVeld.setPromptText("...");
        bewonerList = new ListView<>();
        
        //elementen op de juiste plek zetten
        bewonerLabel.relocate(10, 10);
        bewonerVeld.relocate(10, 30);
        createButton.relocate(10, 60);
        readButton.relocate(70, 60);
        deleteButton.relocate(130,60);
        bewonerList.relocate(10, 90);
        
        createButton.setOnAction(e -> {
            
            String bewoner = bewonerVeld.getText().trim();
            
            if (bewoner.isEmpty()) {
                System.out.println("Bewoner mag niet leeg zijn!");
                return;
                
            }

            int iResult = createBewoner(bewoner);
            System.out.println("Aantal rijen toegevoegd: " + iResult);
            
        });
        
        readButton.setOnAction(e -> {
            
            bewoners.clear();
            bewonerList.getItems().clear();
            
            ResultSet r = null;
            try {
                r = readBewoner("select bewonernaam from bewoners");
                while (r.next()) {
                    String bewonerNaam = null;
                    Bewoner b = new Bewoner(bewonerNaam);
                    b.setBewonernaam(r.getString("bewonernaam"));
                    bewoners.add(b);
                }
            }   catch (Exception se) {
                    se.printStackTrace();
            }
            
                for (Bewoner b : bewoners) { // for - each
                bewonerList.getItems().add(b);
            }
            
        });
        
        deleteButton.setOnAction(e -> {
            
            Bewoner geselecteerdBewoner =
                bewonerList.getSelectionModel().getSelectedItem();

            if (geselecteerdBewoner == null) {
                System.out.println("Geen bewoner geselecteerd");
                return;
        }

        // verwijderen uit database
        deleteBewoner(geselecteerdBewoner);

        // verwijderen uit ListView
        bewonerList.getItems().remove(geselecteerdBewoner);
            
        });
        
        root.getChildren().addAll(bewonerLabel, bewonerVeld, createButton, 
                readButton, deleteButton, bewonerList);
        
    }
    
    public int createBewoner(String bewonernaam) {
        
        String sql = "INSERT INTO bewoners (bewonernaam) VALUES (?)";
        int result = 0;

        try (Connection conn = DBCPDatasource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bewonernaam);
            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return result;
        
    }
    
    public ResultSet readBewoner(String sql){
        
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
    
    public void deleteBewoner(Bewoner bewonernaam) {
        String sql = "DELETE FROM bewoners WHERE bewonernaam = ?";
        
        try (Connection con = DBCPDatasource.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, bewonernaam.getBewonernaam());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

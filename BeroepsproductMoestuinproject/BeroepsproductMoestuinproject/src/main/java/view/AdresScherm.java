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
import model.Adres;
import model.DBCPDatasource;


public class AdresScherm {
    
    private Label adresLabel = new Label("Adres");
    private TextField adresVeld = new TextField();
    private Button createButton = new Button("Create");
    private Button readButton = new Button("Read");
    private Button deleteButton = new Button("Delete");
    private ListView<Adres> adresList;
    private ArrayList<Adres> adressen = new ArrayList();

    
    public AdresScherm(Pane root){
        
        adresVeld.setPromptText("...");
        adresList = new ListView<>();
        
        //elementen op de juiste plek zetten
        adresLabel.relocate(10, 10);
        adresVeld.relocate(10, 30);
        createButton.relocate(10, 60);
        readButton.relocate(70, 60);
        deleteButton.relocate(130,60);
        adresList.relocate(10, 90);
        
        createButton.setOnAction(e -> {
            
            String adres = adresVeld.getText().trim();
            
            if (adres.isEmpty()) {
                System.out.println("Adres mag niet leeg zijn!");
                return;
                
            }

            int iResult = createAdres(adres);
            System.out.println("Aantal rijen toegevoegd: " + iResult);
            
        });
        
        readButton.setOnAction(e -> {
            
            adressen.clear();
            adresList.getItems().clear();
            
            ResultSet r = null;
            try {
                r = readAdres("select adres from adressen");
                while (r.next()) {
                    String adres = null;
                    Adres a = new Adres(adres);
                    a.setAdres(r.getString("adres"));
                    adressen.add(a);
                }
            }   catch (Exception se) {
                    se.printStackTrace();
            }
            
            for (Adres a : adressen) { // for - each
            adresList.getItems().add(a);
        }
            
        });
        
        deleteButton.setOnAction(e -> {
            
            Adres geselecteerdAdres =
                adresList.getSelectionModel().getSelectedItem();

            if (geselecteerdAdres == null) {
                System.out.println("Geen adres geselecteerd");
                return;
        }

        // verwijderen uit database
        deleteAdres(geselecteerdAdres);

        // verwijderen uit ListView
        adresList.getItems().remove(geselecteerdAdres);
            
        });
        
        root.getChildren().addAll(adresLabel, adresVeld, createButton, 
                readButton, deleteButton, adresList);
                
    }
    
    public int createAdres(String adres) {
        
        String sql = "INSERT INTO adressen (adres) VALUES (?)";
        int result = 0;

        try (Connection conn = DBCPDatasource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, adres);
            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return result;
        
    }
    
    public ResultSet readAdres(String sql){
        
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
    
    public void deleteAdres(Adres adres) {
        String sql = "DELETE FROM adressen WHERE adres = ?";
        
        try (Connection con = DBCPDatasource.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, adres.getAdres());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
        

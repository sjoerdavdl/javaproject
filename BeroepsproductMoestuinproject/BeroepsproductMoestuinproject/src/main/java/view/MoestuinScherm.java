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
import model.Moestuin;
import model.DBCPDatasource;

public class MoestuinScherm {
    
    private Label moestuinLabel = new Label("Moestuin");
    private TextField moestuinVeld = new TextField();
    private Button createButton = new Button("Create");
    private Button readButton = new Button("Read");
    private Button deleteButton = new Button("Delete");
    private ListView<Moestuin> moestuinList;
    private ArrayList<Moestuin> moestuinen = new ArrayList();
    
    public MoestuinScherm(Pane root){
        
        moestuinVeld.setPromptText("...");
        moestuinList = new ListView<>();
        
        //elementen op de juiste plek zetten
        moestuinLabel.relocate(10, 10);
        moestuinVeld.relocate(10, 30);
        createButton.relocate(10, 60);
        readButton.relocate(70, 60);
        deleteButton.relocate(130,60);
        moestuinList.relocate(10, 90);
        
        createButton.setOnAction(e -> {
            
            String moestuin = moestuinVeld.getText().trim();
            
            if (moestuin.isEmpty()) {
                System.out.println("Moestuin mag niet leeg zijn!");
                return;
                
            }

            int iResult = createMoestuin(moestuin);
            System.out.println("Aantal rijen toegevoegd: " + iResult);
            
        });
        
        readButton.setOnAction(e -> {
            
            moestuinen.clear();
            moestuinList.getItems().clear();
            
            ResultSet r = null;
            try {
                r = readMoestuin("select moestuinnaam from moestuinen");
                while (r.next()) {
                    String moestuin = null;
                    Moestuin m = new Moestuin(moestuin);
                    m.setMoestuinnaam(r.getString("moestuinnaam"));
                    moestuinen.add(m);
                }
            }   catch (Exception se) {
                    se.printStackTrace();
            }
            
                for (Moestuin m : moestuinen) { // for - each
                moestuinList.getItems().add(m);
            }
            
        });
        
        deleteButton.setOnAction(e -> {
            
            Moestuin geselecteerdeMoestuin =
                moestuinList.getSelectionModel().getSelectedItem();

            if (geselecteerdeMoestuin == null) {
                System.out.println("Geen moestuin geselecteerd");
                return;
        }

        // verwijderen uit database
        deleteMoestuin(geselecteerdeMoestuin);

        // verwijderen uit ListView
        moestuinList.getItems().remove(geselecteerdeMoestuin);
            
        });
        
        root.getChildren().addAll(moestuinLabel, moestuinVeld, createButton, 
                readButton, deleteButton, moestuinList);
        
    }
    
    public int createMoestuin(String moestuinnaam) {
        
        String sql = "INSERT INTO taken (taakomschrijving) VALUES (?)";
        int result = 0;

        try (Connection conn = DBCPDatasource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, moestuinnaam);
            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return result;
        
    }
    
    public ResultSet readMoestuin(String sql){
        
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
    
    public void deleteMoestuin(Moestuin moestuin) {
        String sql = "DELETE FROM moestuinen WHERE moestuinnaam = ?";
        
        try (Connection con = DBCPDatasource.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, moestuin.getMoestuinnaam());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}

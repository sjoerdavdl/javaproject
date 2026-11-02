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
import model.Taak;
import model.DBCPDatasource;

public class TaakScherm {
    
    private Label taakLabel = new Label("Taak");
    private TextField taakVeld = new TextField();
    private Button createButton = new Button("Create");
    private Button readButton = new Button("Read");
    private Button deleteButton = new Button("Delete");
    private ListView<Taak> taakList;
    private ArrayList<Taak> taken = new ArrayList();
    
    public TaakScherm(Pane root){
        
        taakVeld.setPromptText("...");
        taakList = new ListView<>();
        
        //elementen op de juiste plek zetten
        taakLabel.relocate(10, 10);
        taakVeld.relocate(10, 30);
        createButton.relocate(10, 60);
        readButton.relocate(70, 60);
        deleteButton.relocate(130,60);
        taakList.relocate(10, 90);
        
        createButton.setOnAction(e -> {
            
            String taak = taakVeld.getText().trim();
            
            if (taak.isEmpty()) {
                System.out.println("Adres mag niet leeg zijn!");
                return;
                
            }

            int iResult = createTaak(taak);
            System.out.println("Aantal rijen toegevoegd: " + iResult);
            
        });
        
        readButton.setOnAction(e -> {
            
            taken.clear();
            taakList.getItems().clear();
            
            ResultSet r = null;
            try {
                r = readTaak("select taakomschrijving from taken");
                while (r.next()) {
                    String taakomschrijving = null;
                    Taak t = new Taak(taakomschrijving);
                    t.setTaakomschrijving(r.getString("taakomschrijving"));
                    taken.add(t);
                }
            }   catch (Exception se) {
                    se.printStackTrace();
            }
            
                for (Taak t : taken) { // for - each
                taakList.getItems().add(t);
            }
            
        });
        
        deleteButton.setOnAction(e -> {
            
            Taak geselecteerdeTaak =
                taakList.getSelectionModel().getSelectedItem();

            if (geselecteerdeTaak == null) {
                System.out.println("Geen taak geselecteerd");
                return;
        }

        // verwijderen uit database
        deleteTaak(geselecteerdeTaak);

        // verwijderen uit ListView
        taakList.getItems().remove(geselecteerdeTaak);
            
        });
        
        root.getChildren().addAll(taakLabel, taakVeld, createButton, 
                readButton, deleteButton, taakList);
        
    }
    
    public int createTaak(String taakomschrijving) {
        
        String sql = "INSERT INTO taken (taakomschrijving) VALUES (?)";
        int result = 0;

        try (Connection conn = DBCPDatasource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, taakomschrijving);
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
    
    public void deleteTaak(Taak taak) {
        String sql = "DELETE FROM taken WHERE taakomschrijving = ?";
        
        try (Connection con = DBCPDatasource.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, taak.getTaakomschrijving());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}

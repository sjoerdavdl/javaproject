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
import model.Groente;
import model.DBCPDatasource;

public class GroenteScherm {
    
    private Label groenteLabel = new Label("Groente");
    private TextField groenteVeld = new TextField();
    private Button createButton = new Button("Create");
    private Button readButton = new Button("Read");
    private Button deleteButton = new Button("Delete");
    private ListView<Groente> groenteList;
    private ArrayList<Groente> groenten = new ArrayList();
    
    public GroenteScherm(Pane root){
        
        groenteVeld.setPromptText("...");
        groenteList = new ListView<>();
        
        //elementen op de juiste plek zetten
        groenteLabel.relocate(10, 10);
        groenteVeld.relocate(10, 30);
        createButton.relocate(10, 60);
        readButton.relocate(70, 60);
        deleteButton.relocate(130,60);
        groenteList.relocate(10, 90);
        
        createButton.setOnAction(e -> {
            
            String groente = groenteVeld.getText().trim();
            
            if (groente.isEmpty()) {
                System.out.println("Groente mag niet leeg zijn!");
                return;
                
            }

            int iResult = createGroente(groente);
            System.out.println("Aantal rijen toegevoegd: " + iResult);
            
        });
        
        readButton.setOnAction(e -> {
            
            groenten.clear();
            groenteList.getItems().clear();
            
            ResultSet r = null;
            try {
                r = readGroente("select groente from groenten");
                while (r.next()) {
                    String groentenaam = null;
                    Groente g = new Groente(groentenaam);
                    g.setGroente(r.getString("groente"));
                    groenten.add(g);
                }
            }   catch (Exception se) {
                    se.printStackTrace();
            }
            
                for (Groente g : groenten) { // for - each
                groenteList.getItems().add(g);
            }
            
        });
        
        deleteButton.setOnAction(e -> {
            
            Groente geselecteerdeGroente =
                groenteList.getSelectionModel().getSelectedItem();

            if (geselecteerdeGroente == null) {
                System.out.println("Geen groente geselecteerd");
                return;
        }

        // verwijderen uit database
        deleteGroente(geselecteerdeGroente);

        // verwijderen uit ListView
        groenteList.getItems().remove(geselecteerdeGroente);
            
        });
        
        root.getChildren().addAll(groenteLabel, groenteVeld, createButton, 
                readButton, deleteButton, groenteList);
        
    }
    
    public int createGroente(String groente) {
        
        String sql = "INSERT INTO groenten (groente) VALUES (?)";
        int result = 0;

        try (Connection conn = DBCPDatasource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, groente);
            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return result;
        
    }
    
    public ResultSet readGroente(String sql){
        
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
    
    public void deleteGroente(Groente groente) {
        String sql = "DELETE FROM groenten WHERE groente = ?";
        
        try (Connection con = DBCPDatasource.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, groente.getGroente());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}



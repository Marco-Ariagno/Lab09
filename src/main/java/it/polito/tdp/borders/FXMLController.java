package it.polito.tdp.borders;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtAnno;

    @FXML
    private ComboBox<Country> cmbBoxStato;

    @FXML
    private Button btnTrovaVicini;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	model=new Model();
    	txtResult.clear();
    	int anno=0;
    	try {
    		anno=Integer.parseInt(txtAnno.getText());
    	}
    	catch(NumberFormatException nfe) {
    		nfe.printStackTrace();
    		return;
    	}
    	model.creaGrafo(anno);
    	txtResult.appendText("Grafo con "+model.nVertici()+" vertici e "+model.nArchi()+" archi\n");
    	for(Country c:model.getGrafo().vertexSet()) {
    		txtResult.appendText("Stato: "+c+", paesi confinanti: "+model.getGrafo().degreeOf(c)+"\n");
    	}
    	txtResult.appendText("Numero componenti connesse: "+model.nComponentiConnesse()+"\n");
    	
    	List<Country> vertici=new ArrayList<>(model.getGrafo().vertexSet());
    	Collections.sort(vertici);
    	
    	cmbBoxStato.getItems().addAll(vertici);
    }

    @FXML
    void doTrovaVicini(ActionEvent event) {
    	Country c=cmbBoxStato.getValue();
    	/*Set<Country> connessi=model.trovaPercorsoConConnInsp(c);
    	connessi.remove(c);
    	for(Country co:connessi) {
    		txtResult.appendText(co+"\n");
    	}*/
    	/*Set<Country> connessi=new HashSet<>(model.trovaPercorsoConIterator(c));
    	connessi.remove(c);
    	for(Country co:connessi) {
    		txtResult.appendText(co+"\n");
    	}*/
    	List<Country> connessi=new ArrayList<>(model.visitaAmpiezza(c));
    	connessi.remove(c);
    	for(Country co:connessi) {
    		txtResult.appendText(co+"\n");
    	}
    }

    @FXML
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbBoxStato != null : "fx:id=\"cmbBoxStato\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnTrovaVicini != null : "fx:id=\"btnTrovaVicini\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model=model;
    }
}

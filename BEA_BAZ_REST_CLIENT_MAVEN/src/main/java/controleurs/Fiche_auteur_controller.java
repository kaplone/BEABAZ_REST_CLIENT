package controleurs;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.type.TypeReference;

import models.Auteur;
import utils.JsonUtils;
import utils.RestAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Fiche_auteur_controller extends Fiche_controller implements Initializable{
	
	@FXML
	private ObservableList<Auteur> liste_auteurs;
	@FXML
	private ListView<Auteur> listView_auteur;

	@FXML
	private TextField nom_auteur_textField;
	@FXML
	private TextArea remarques_auteur_textArea;
	@FXML
	private Button nouveau;
	@FXML
	private TextField nom_complet_auteur_textField;

	private Auteur auteurSelectionne;
	
	@FXML
	public void onAuteurSelect(){
        super.onAnnulerEditButton();
        	
		if (listView_auteur.getSelectionModel().getSelectedItem() == null){
			auteurSelectionne = null;
			editer.setVisible(false);	
		}
		else {
			if (listView_auteur.getSelectionModel().getSelectedItem().getNom() != null){
				auteurSelectionne = listView_auteur.getSelectionModel().getSelectedItem();	
				afficherAuteur();
				editer.setVisible(true);
    		}
    		else {
    			editer.setVisible(false);
    		}	
		}	
	}

    public void rafraichirAffichage(){
    	
    	liste_auteurs = FXCollections.observableArrayList();
    	JsonUtils.JsonToListObj(RestAccess.requestAll("auteur"), liste_auteurs, new TypeReference<List<Auteur>>() {});

		listView_auteur.setItems(liste_auteurs);  
		afficherAuteur();
    }
    
    @FXML
    public void onNouveauAuteurButton() {  	
    	super.onNouveauButton();

    	auteurSelectionne = new Auteur();
    }
    
    @FXML
    public void onEditerAuteurButton(){
    	super.onEditerButton();
    }
    
    @FXML
    public void onAnnulerEditButton(){
    	super.onAnnulerEditButton();
    	onAuteurSelect();
    }
    
    @FXML
    public void onMiseAJourAuteurButton(){
    	
    	auteurSelectionne.setNom(nom_auteur_textField.getText());
    	auteurSelectionne.setNom_complet(nom_complet_auteur_textField.getText());
    	auteurSelectionne.setRemarques(remarques_auteur_textArea.getText());
		
		if (edit) {
			auteurSelectionne.update();
		}
		else {
		   Auteur temp_Auteur = auteurSelectionne.save();
		   
		   if (temp_Auteur != null){
			   auteurSelectionne = temp_Auteur;
			   listView_auteur.getSelectionModel().select(auteurSelectionne);
			   rafraichirAffichage();
		   }
		}
		super.onMiseAJourButton();
    	
    }
    
    public void afficherAuteur(){
    	editability(false);
		editer.setVisible(true);
		prompt(false);
    	
    	if (auteurSelectionne != null){
    		nom_label.setText(auteurSelectionne.getNom());
    		nom_auteur_textField.setText(auteurSelectionne.getNom());
    		nom_complet_auteur_textField.setText(auteurSelectionne.getNom_complet());
        	remarques_auteur_textArea.setText(auteurSelectionne.getRemarques());
    	}	
    	else {
    		raz();
    	}	
    }
    
    @Override
    public void editability(boolean bool){
    	nom_auteur_textField.setEditable(bool);
    	nom_complet_auteur_textField.setEditable(bool);
		remarques_auteur_textArea.setEditable(bool);
		prompt(bool);
    }
    
    @Override
    public void raz(){
    	nom_auteur_textField.setText("");
    	nom_complet_auteur_textField.setText("");
    	remarques_auteur_textArea.setText("");
    }
    
    @Override
    public void prompt(boolean bool){
    	
    	if (bool){
    		nom_auteur_textField.setPromptText("saisir le nom affiché du nouvel auteur");
        	nom_complet_auteur_textField.setPromptText("saisir le nom complet du nouvel auteur");
        	remarques_auteur_textArea.setPromptText("éventuelles remarques");
    	}
    	else {
    		nom_auteur_textField.setPromptText(null);
        	nom_complet_auteur_textField.setPromptText(null);
        	remarques_auteur_textArea.setPromptText(null);
    	}	
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {       
		super.init();

		versAuteursButton.setVisible(false);
		
		liste_auteurs = FXCollections.observableArrayList();
		JsonUtils.JsonToListObj(RestAccess.requestAll("auteur"), liste_auteurs, new TypeReference<List<Auteur>>() {});

		listView_auteur.setItems(liste_auteurs);
		
		listView_auteur.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			onAuteurSelect();
		});
		
		if (! liste_auteurs.isEmpty()){
			listView_auteur.getSelectionModel().select(0);
		}
	}
}

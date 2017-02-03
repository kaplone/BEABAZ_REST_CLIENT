package application;

import java.net.URL;
import java.util.ResourceBundle;

import models.Auteur;
import models.Contexte;
import models.Messages;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
	
	Auteur[] auteurCursor;
	Auteur auteurSelectionne;

	Auteur auteur;
	
	@FXML
	public void onAuteurSelect(){

		
		if (listView_auteur.getSelectionModel().getSelectedItem() == null){

			editer.setVisible(false);	
		}
		else {
			
			if (listView_auteur.getSelectionModel().getSelectedItem().getNom() != null){

				auteurSelectionne = listView_auteur.getSelectionModel().getSelectedItem();	
				affichageInfos(auteurSelectionne);
				editer.setVisible(true);
    		}
    		else {
    			editer.setVisible(false);
    		}	
		}	
	}
	
    private void affichageInfos(Auteur auteurSelectionne){
        
    	if (listView_auteur.getSelectionModel().getSelectedItem() != null){
    		nom_auteur_textField.setText(auteurSelectionne.getNom());
    		nom_complet_auteur_textField.setText(auteurSelectionne.getNom_complet());
        	remarques_auteur_textArea.setText(auteurSelectionne.getRemarques());
    	}	
    	else {
    		raz();
    	}
    }
    
    public void onNouveauAuteurButton() {  	
    	super.onNouveauButton();
    	editability(true);
    	raz();
    	
    	auteurSelectionne = new Auteur();
    }
    
    public void onAnnulerButton() {	
    	editability(false);
    	raz();
    	
    	nouveau.setText("Nouvel auteur");

    	if (listView_auteur.getSelectionModel().getSelectedItem() == null){
    		editer.setVisible(false);
    	}
    	else {
    		if (listView_auteur.getSelectionModel().getSelectedItem().getNom() == null){
    			editer.setVisible(true);
    		}
    		else {
    			editer.setVisible(false);
    		}
    		
    	}
    	rafraichirAffichage();
    	listView_auteur.getSelectionModel().select(auteurSelectionne);
    	affichageInfos(auteurSelectionne);
    	
    }
    
    public void rafraichirAffichage(){
    	
    	liste_auteurs = FXCollections.observableArrayList();

		auteurCursor = Auteur.retrouveAuteurs();
		
		for (Auteur auteur : auteurCursor) {
			liste_auteurs.add(auteur);
		}
		
		listView_auteur.setItems(liste_auteurs);
    	
    }
    
    @FXML
    public void onEditerAuteurButton(){
    	super.onEditerButton();
        editability(true);
    }
    
    @FXML
    public void onAnnulerEditButton(){
    	super.onAnnulerEditButton();
    	editability(false);
    	
    	annuler.setVisible(false);
    	if (listView_auteur.getSelectionModel().getSelectedItem() == null){
    		editer.setVisible(false);
    	}
    	else {
    		if (listView_auteur.getSelectionModel().getSelectedItem().getNom() == null){
    			editer.setVisible(true);
    		}
    		else {
    			editer.setVisible(false);
    		}   		
    	} 	
    }
    
    @FXML
    public void onMiseAJourAuteurButton(){
    	super.onMiseAJourButton();
    	editability(false);
    	
    	
    	if (auteurSelectionne == null){
    		auteurSelectionne = new Auteur();
    	}
    	
    	auteurSelectionne.setNom(nom_auteur_textField.getText());
    	auteurSelectionne.setNom_complet(nom_complet_auteur_textField.getText());
    	auteurSelectionne.setRemarques(remarques_auteur_textArea.getText());
    	
    	annuler.setVisible(false);
    	if (listView_auteur.getSelectionModel().getSelectedItem() == null){
    		editer.setVisible(false);
    	}
    	else {
    		if (listView_auteur.getSelectionModel().getSelectedItem().getNom() == null){
    			editer.setVisible(true);
    		}
    		else {
    			editer.setVisible(false);
    		}
    		
    	}
		
		if (edit) {
			Auteur.update(auteurSelectionne);
			afficherAuteur();
			rafraichirAffichage();
			onAnnulerEditButton();
		}
		else {
			
		   Auteur.save(auteurSelectionne);
		   afficherAuteur();
		   rafraichirAffichage();
		   onAnnulerEditButton();
		}
    	
    }
    
    public void afficherAuteur(){
    	nom_auteur_textField.setText(auteurSelectionne.getNom());
    	nom_complet_auteur_textField.setText(auteurSelectionne.getNom_complet());
    	remarques_auteur_textArea.setText(auteurSelectionne.getRemarques());

    	
    }
    
    public void editability(boolean bool){
    	nom_auteur_textField.setEditable(bool);
    	nom_complet_auteur_textField.setEditable(bool);
		remarques_auteur_textArea.setEditable(bool);
		prompt(bool);
    }
    
    public void raz(){
    	nom_auteur_textField.setText("");
    	nom_complet_auteur_textField.setText("");
    	remarques_auteur_textArea.setText("");
    }
    
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
		
		nom_auteur_textField.setEditable(false);
		nom_complet_auteur_textField.setEditable(false);
		remarques_auteur_textArea.setEditable(false);
		
		liste_auteurs = FXCollections.observableArrayList();
		
		auteurCursor = Auteur.retrouveAuteurs();
		
		for (Auteur auteur : auteurCursor) {
			liste_auteurs.add(auteur);
		}
		
		listView_auteur.setItems(liste_auteurs);
		if (! liste_auteurs.isEmpty()){
			listView_auteur.getSelectionModel().select(0);
			auteurSelectionne = listView_auteur.getSelectionModel().getSelectedItem();
			afficherAuteur();
		}
		

	}

}

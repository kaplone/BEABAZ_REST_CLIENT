package controleurs;

import java.util.Map;

import application.JfxUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.Contexte;
import models.Messages;

public abstract class Fiche_controller{
	
	@FXML
	protected Button versClientButton;
	@FXML
	protected Button versCommandeButton;
	@FXML
	protected Button versOeuvreButton;
	@FXML
	protected Button versRapportButton;
	@FXML
	protected Button versTraitementsButton;
	@FXML
	protected Button versProduitsButton;
	@FXML
	protected Button versModelesButton;
	@FXML
	protected Button versFichiersButton;
	@FXML
	protected Button versMatieresButton;
	@FXML
	protected Button versTechniquesButton;
	@FXML
	protected Button versModelsButton;
	@FXML
	protected Button versAuteursButton;
	
	@FXML
	protected Button nouveau;
	@FXML
	protected Button annuler;
	@FXML
	protected Button editer;
	@FXML
	protected Button mise_a_jour;
	
	@FXML
	protected Label nom_label;
	
	@FXML
	protected ChoiceBox<String> auteursChoiceBox;
	@FXML
	protected TableView<Map<String, String>> tableOeuvre;
	@FXML
	protected TableColumn<Map<String, String>, String> oeuvres_nom_colonne;
	@FXML
	protected TableColumn<Map<String, String>, ImageView> oeuvres_fait_colonne;
	
	protected Stage currentStage;
	
	protected boolean edit = false;
	
	protected static ObservableList<String> observableAuteurs;
	protected static ObservableList<Map<String, String>> obs_oeuvres;
	
	@FXML
	public void onVersProduitsButton(){
		
		Scene fiche_produit_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_produit.fxml"), Contexte.largeurFenetre, Contexte.hauteurFenetre);
		fiche_produit_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_produit_scene);	
	}
	@FXML
    public void onVersFichiersButton(){}
    @FXML
    public void onVersTraitementsButton(){
    	Scene fiche_traitement_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_traitement.fxml"), Contexte.largeurFenetre, Contexte.hauteurFenetre);
		fiche_traitement_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_traitement_scene);
    }
    @FXML
    public void onVersModelesButton(){
    	Scene fiche_modele_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_model.fxml"), Contexte.largeurFenetre, Contexte.hauteurFenetre);
		fiche_modele_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_modele_scene);
    }
        

    @FXML
    public void onVersClientButton(){
    	Scene fiche_client_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_client.fxml"), Contexte.largeurFenetre, Contexte.hauteurFenetre);
		fiche_client_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_client_scene);
    }
	@FXML
	public void onVersCommandeButton(){
		
		Scene fiche_commande_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_commande.fxml"), Contexte.largeurFenetre, Contexte.hauteurFenetre);
		fiche_commande_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_commande_scene);	
	}
	@FXML
    public void onVersOeuvreButton(){
    	Scene fiche_oeuvre_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_oeuvre_v2.fxml"), Contexte.largeurFenetre, Contexte.hauteurFenetre);
		fiche_oeuvre_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_oeuvre_scene);
    }
	
	@FXML
    public void onVersAuteursButton(){
		Scene fiche_auteur_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_auteur.fxml"), Contexte.largeurFenetre, Contexte.hauteurFenetre);
		fiche_auteur_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_auteur_scene);
	}
	
	@FXML
    public void onMatieres_button(){
    	Scene fiche_matiere_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_matiere.fxml"), Contexte.largeurFenetre, Contexte.hauteurFenetre);
		fiche_matiere_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_matiere_scene);
    }
    @FXML
    public void onTechniques_button(){
    	Scene fiche_technique_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_technique.fxml"), Contexte.largeurFenetre, Contexte.hauteurFenetre);
		fiche_technique_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_technique_scene);
    }
    
    public void onNouveauButton() {
    	
    	edit = false;
    	
    	nouveau.setVisible(false);
    	annuler.setVisible(true);
    	editer.setVisible(false);
    	mise_a_jour.setVisible(true);
    	
    	mise_a_jour.setText("Enregistrer");
    	nom_label.setText("");
    	
    	raz();
    	prompt(true);
    	editability(true);
    	
    }
    
    public void onCreerButton() {
    	
    	edit = false;
    	
    	nouveau.setVisible(false);
    	annuler.setVisible(true);
    	editer.setVisible(false);
    	mise_a_jour.setVisible(true);
    	
    	mise_a_jour.setText("Créer");
    	
    	prompt(false);
    	editability(false);
    }
    
    public void onEditerButton(){
    	
    	edit = true;
    	
    	nouveau.setVisible(false);
    	annuler.setVisible(true);
    	editer.setVisible(false);
    	mise_a_jour.setVisible(true);
    	
    	mise_a_jour.setText("Mise à jour");
    	
    	editability(true);
        prompt(true);
    }
    
    public void onAnnulerEditButton(){
    	
    	edit = false;
    	
    	nouveau.setVisible(true);
    	annuler.setVisible(false);
    	editer.setVisible(true);
    	mise_a_jour.setVisible(false);
    	
        editability(false);
        prompt(false);
    }
    
    public void onMiseAJourButton(){
    	
    	edit = false;
    	    	
    	annuler.setVisible(false);
    	nouveau.setVisible(true);
    	editer.setVisible(true);
    	mise_a_jour.setVisible(false);
    	
    	editability(false);
    	prompt(false);
    }
    
    public void etatInitial(){
    	nouveau.setVisible(true);
    	annuler.setVisible(false);
    	editer.setVisible(false);
    	mise_a_jour.setVisible(false);
    }
    
    public abstract void editability(boolean b);
    public abstract void prompt(boolean b);
    public abstract void raz();
    
    
    
	public void init() {
		etatInitial();
    	
		currentStage = Messages.getStage();
		
		versClientButton.setVisible(true);
		versRapportButton.setVisible(true);
		versModelesButton.setVisible(true);
		versTraitementsButton.setVisible(true);
		versFichiersButton.setVisible(true);
		versProduitsButton.setVisible(true);
		versAuteursButton.setVisible(true);
    	
		if(Messages.getCommande() != null){
		    versCommandeButton.setVisible(true);
		}
		else {
			versCommandeButton.setVisible(false);
		}
		
		if(Messages.getOeuvreTraitee() != null){
			versOeuvreButton.setVisible(true);
		}
		else {
			versOeuvreButton.setVisible(false);
		}
		
		versRapportButton.setVisible(false);
		
		editability(false);
    	prompt(false);
    }

}

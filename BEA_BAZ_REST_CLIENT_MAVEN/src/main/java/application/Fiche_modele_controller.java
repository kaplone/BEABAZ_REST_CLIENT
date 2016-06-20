package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import org.jongo.MongoCursor;

import utils.MongoAccess;
import models.Model;
import models.Client;
import models.Commande;
import models.Messages;
import models.Traitement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Fiche_modele_controller  implements Initializable{
	
	@FXML
	private ObservableList<Model> liste_models;
	@FXML
	private ListView<Model> listView_model;

	@FXML
	private TextField nom_model_textField;
	@FXML
	private TextField file_path_textField;
	@FXML
	private TextArea remarques_model_textArea;
	@FXML
	private Button nouveau_model;
	@FXML
	private Button mise_a_jour_model;
	@FXML
	private Button annuler;
	@FXML
	private Button editer;
	@FXML
	private Button versClientButton;
	@FXML
	private Button versCommandeButton;
	@FXML
	private Button versOeuvreButton;
	@FXML
	private Button versRapportButton;
	@FXML
	private Button versTraitementsButton;
	@FXML
	private Button versProduitsButton;
	@FXML
	private Button versModelesButton;
	@FXML
	private Button versFichiersButton;
	@FXML
	private Button versModelsButton;
	@FXML
	private Button versAuteursButton;
	
	
	@FXML
	private Button cheminVersModelButton;
	
	MongoCursor<Model> modelCursor;
	Model modelSelectionne;
	
	Stage currentStage;

	Model model;
	
	private File file;
	
	private boolean edit = false;

	@FXML
	public void onVersProduitsButton(){
		
		Scene fiche_produit_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_produit.fxml"), 1275, 722);
		fiche_produit_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_produit_scene);	
	}
	@FXML
    public void onVersFichiersButton(){}
    @FXML
    public void onVersTraitementsButton(){
    	Scene fiche_traitement_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_traitement.fxml"), 1275, 722);
		fiche_traitement_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_traitement_scene);
    }

    @FXML
    public void onVersClientButton(){
    	Scene fiche_client_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_client.fxml"), 1275, 722);
		fiche_client_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_client_scene);
    }
	@FXML
	public void onVersCommandeButton(){
		
		Scene fiche_commande_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_commande.fxml"), 1275, 722);
		fiche_commande_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_commande_scene);	
	}
	@FXML
    public void onVersOeuvreButton(){
    	Scene fiche_oeuvre_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_oeuvre.fxml"), 1275, 722);
		fiche_oeuvre_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_oeuvre_scene);
    }
	
	@FXML
	public void onVersAuteursButton(){
		
		Scene fiche_auteur_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_auteur.fxml"), 1275, 722);
		fiche_auteur_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_auteur_scene);
		
	}
	@FXML
    public void onMatieres_button(){
    	Scene fiche_matiere_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_matiere.fxml"), 1275, 722);
		fiche_matiere_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_matiere_scene);
    }
    @FXML
    public void onTechniques_button(){
    	Scene fiche_technique_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_technique.fxml"), 1275, 722);
		fiche_technique_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_technique_scene);
    }
	
	@FXML
	public void onModelSelect(){
		
		modelSelectionne = listView_model.getSelectionModel().getSelectedItem();
		Messages.setModel(modelSelectionne);		
		affichageInfos(modelSelectionne);
		
	}
	
    private void affichageInfos(Model modelSelectionne){

    	
    	nom_model_textField.setText(modelSelectionne.getNom());
    	remarques_model_textArea.setText(modelSelectionne.getRemarques());
    	file_path_textField.setText(modelSelectionne.getCheminVersModelSTR());
    	
    	model = Messages.getModel();    	
    	
    }
    
    public void onNouveauModelButton() {
    	
    	cheminVersModelButton.setVisible(true);
    	
    	mise_a_jour_model.setText("Enregistrer");
    	nom_model_textField.setText("");
    	remarques_model_textArea.setText("");
    	nom_model_textField.setPromptText("saisir le nom du nouvel model");
    	remarques_model_textArea.setPromptText("éventuelles remarques");
    	nouveau_model.setVisible(false);
    	
    	modelSelectionne = new Model();
    	
    	edit = false;
    	annuler.setVisible(true);
    	editer.setVisible(false);
    	mise_a_jour_model.setVisible(true);
    	nom_model_textField.setEditable(true);
		remarques_model_textArea.setEditable(true);
    }
    
    public void onAnnulerButton() {
    	
    	cheminVersModelButton.setVisible(false);
    	
    	mise_a_jour_model.setText("Mise à jour");
    	nom_model_textField.setText("");
    	remarques_model_textArea.setText("");
    	nom_model_textField.setPromptText("");
    	remarques_model_textArea.setPromptText("");
    	nouveau_model.setText("Nouvel model");
    	rafraichirAffichage();
    	listView_model.getSelectionModel().select(modelSelectionne);
    	affichageInfos(modelSelectionne);
    }
    
    public void rafraichirAffichage(){
    	
    	liste_models = FXCollections.observableArrayList();
		
		
		
		modelCursor = MongoAccess.request("model").as(Model.class);
		
		while (modelCursor.hasNext()){
			liste_models.add(modelCursor.next());
		}
		
		listView_model.setItems(liste_models);
    	
    }
    
    @FXML
    public void onEditerModelButton(){
    	
        
    	
    	
    	annuler.setVisible(true);
    	editer.setVisible(false);
    	mise_a_jour_model.setVisible(true);
    	nom_model_textField.setEditable(true);
		remarques_model_textArea.setEditable(true);
		
		edit = true;

	
    }
    
    @FXML
    public void onAnnulerEditButton(){
    	
    	annuler.setVisible(false);
    	editer.setVisible(true);
    	mise_a_jour_model.setVisible(false);
    	nom_model_textField.setEditable(false);
		remarques_model_textArea.setEditable(false);
		nouveau_model.setVisible(true);
		rafraichirAffichage();
		listView_model.getSelectionModel().select(modelSelectionne);
    	affichageInfos(modelSelectionne);
    	
    	edit = false;
    	
    }
    
    @FXML
    public void onMiseAJourModelButton(){
    	
    	if (modelSelectionne == null){
    		modelSelectionne = new Model();
    	}
    	
    	modelSelectionne.setNom(nom_model_textField.getText());
    	modelSelectionne.setRemarques(remarques_model_textArea.getText());
    	modelSelectionne.setCheminVersModelSTR(file_path_textField.getText());
    	
    	annuler.setVisible(false);
    	editer.setVisible(true);
    	mise_a_jour_model.setVisible(false);
    	nom_model_textField.setEditable(false);
		remarques_model_textArea.setEditable(false);
		
		if (edit) {
			Model.update(modelSelectionne);
			//afficherClient();
			rafraichirAffichage();
			onAnnulerEditButton();
		}
		else {
			
		   Model.save(modelSelectionne);
		   //afficherClient();
		   onAnnulerEditButton();
		}
    	
    }
    
protected File chooseExport(){
		
		Stage newStage = new Stage();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Fichier à importer");
		fileChooser.getExtensionFilters().addAll(
		         new FileChooser.ExtensionFilter("feuille Open Office", "*.odt"));
		File selectedFile = fileChooser.showOpenDialog(newStage);
		if (selectedFile != null) {
			 return selectedFile;
		}
		else {
			 return (File) null;
		}
		
	}
	
	@FXML
	public void onCheminVersModelButton(){
		
		file = chooseExport();
		file_path_textField.setText(file.toString());
		nom_model_textField.setText(file.getName().split("\\.")[0]);
	}
	@FXML
	public void on_import_file_button(){
		try {
			Documents.read(file, "produit");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		file_path_textField.setEditable(false);
		nom_model_textField.setEditable(false);
		remarques_model_textArea.setEditable(false);
        editer.setVisible(true);
        mise_a_jour_model.setVisible(false);
		annuler.setVisible(false);
		
		versAuteursButton.setVisible(true);
		versClientButton.setVisible(true);
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
		versModelesButton.setVisible(false);
		
		if (Messages.getCommande() != null){
			versCommandeButton.setVisible(true);
		}
		else {
			versCommandeButton.setVisible(false);
		}
		
		cheminVersModelButton.setVisible(false);
		
		liste_models = FXCollections.observableArrayList();
		
		currentStage = Messages.getStage();
		
		modelCursor = MongoAccess.request("model").as(Model.class);
		
		while (modelCursor.hasNext()){
			liste_models.add(modelCursor.next());
		}
		
		listView_model.setItems(liste_models);

	}

}

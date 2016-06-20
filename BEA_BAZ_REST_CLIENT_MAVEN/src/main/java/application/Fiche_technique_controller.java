package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.jongo.MongoCursor;

import utils.MongoAccess;
import models.Client;
import models.Commande;
import models.Messages;
import models.Produit;
import models.TacheTraitement;
import models.Technique;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Fiche_technique_controller  implements Initializable{

	@FXML
	private ObservableList<Technique> liste_techniques;
	@FXML
	private ListView<Technique> listView_techniques;
	@FXML
	private Button nouvelle_technique;
	@FXML
	private Button mise_a_jour_technique;
	@FXML
	private Button annuler;
	@FXML
	private Button editer;
	@FXML
	private Button versCommandeButton;
	@FXML
	private Button versTraitementButton;
	@FXML
	private Button versModeleButton;
	@FXML
	private Button versOeuvreButton;
	@FXML
	private Button versRapportButton;
	@FXML
	private Button versFichierButton;
	@FXML
	private Button versProduitsButton;
	@FXML
	private Button matieres_button;
	@FXML
	private Button techniques_button;
	@FXML
	private Button lier_button;
	@FXML
	private Button importerButton;
	@FXML
	private TextField file_path_textField;
	
	@FXML
	private Label fiche_technique_label;
	@FXML
	private Label nom_technique_label;
	@FXML
	private TextField nom_technique_textField;
	@FXML
	private TextField nom_complet_technique_textField;
	@FXML
	private TextArea remarques_technique_textArea;
	
	private boolean edit = false;
	
	private File file;
	
	private Map<String, ObjectId> techniques_id;

	MongoCursor<Technique> techniqueCursor ;
	Technique techniqueSelectionne;
	
	Stage currentStage;

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
	public void onVersProduitsButton(){
		
		Scene fiche_produit_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_produit.fxml"), 1275, 722);
		fiche_produit_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_produit_scene);	
	}
	@FXML
    public void onVersFichiersButton(){}

    @FXML
    public void onVersModelesButton(){
    	Scene fiche_model_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_model.fxml"), 1275, 722);
		fiche_model_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_model_scene);
    }
	@FXML
	public void onVersTraitementsButton(){
		
		Scene fiche_traitement_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_traitement.fxml"), 1275, 722);
		fiche_traitement_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_traitement_scene);	
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
	
    protected File chooseExport(){
		
		Stage newStage = new Stage();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Fichier à importer");
		fileChooser.getExtensionFilters().addAll(
		         new FileChooser.ExtensionFilter("feuille de calcul", "*.xlsx"));
		File selectedFile = fileChooser.showOpenDialog(newStage);
		if (selectedFile != null) {
			 return selectedFile;
		}
		else {
			 return (File) null;
		}
		
	}
	
	@FXML
	public void on_select_file_button(){

		file = chooseExport();
		
		if (file != null){
			file_path_textField.setVisible(true);
			file_path_textField.setText(file.toString());
			importerButton.setVisible(true);
		}
		
	}
	@FXML
	public void on_import_file_button(){
		try {
			Documents.read(file, "technique");
			file_path_textField.setVisible(false);
			importerButton.setVisible(false);
			rafraichirAffichage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    private void affichageInfos(){

    	
    	nom_technique_textField.setText(techniqueSelectionne.getNom());
    	remarques_technique_textArea.setText(techniqueSelectionne.getRemarques());
    	
    	liste_techniques.clear();
    	
    	if (techniqueSelectionne != null){
    		
    		techniqueCursor = MongoAccess.request("technique").as(Technique.class);
    		
    		while (techniqueCursor.hasNext()){
    			Technique enplus = techniqueCursor.next();
    			liste_techniques.add(enplus);
    		}	
    		listView_techniques.setItems(liste_techniques);		
    	}	
    }
    
    public void onNouvelle_techniqueButton() {
    	
    	mise_a_jour_technique.setText("Enregistrer");
    	nom_technique_textField.setText("");
    	nom_complet_technique_textField.setText("");
    	remarques_technique_textArea.setText("");
    	nom_technique_textField.setPromptText("saisir le nom affiché du nouveau traitement");
    	nom_complet_technique_textField.setPromptText("saisir le nom complet du nouveau traitement");
    	remarques_technique_textArea.setPromptText("éventuelles remarques");
    	nouvelle_technique.setVisible(false);
    	
    	techniqueSelectionne = new Technique();
    	
    	edit = false;
    	annuler.setVisible(true);
    	editer.setVisible(false);
    	mise_a_jour_technique.setVisible(true);
    	nom_technique_textField.setEditable(true);
    	nom_complet_technique_textField.setEditable(true);
		remarques_technique_textArea.setEditable(true);
    	
    	
    }
    
    public void onAnnulerButton() {
    	
    	nom_technique_textField.setEditable(false);
    	nom_complet_technique_textField.setEditable(false);
		remarques_technique_textArea.setEditable(false);
    	
    	mise_a_jour_technique.setText("Mise à jour");
    	nom_technique_textField.setText("");
    	remarques_technique_textArea.setText("");
    	nom_technique_textField.setPromptText("");
    	nom_complet_technique_textField.setPromptText("");
    	remarques_technique_textArea.setPromptText("");
    	nouvelle_technique.setText("Nouveau traitement");
    	rafraichirAffichage();
    	listView_techniques.getSelectionModel().select(techniqueSelectionne);
    	affichageInfos();
    	
    }
    
    public void rafraichirAffichage(){

		liste_techniques  = FXCollections.observableArrayList();
		techniques_id = new TreeMap<>();

		techniqueCursor = MongoAccess.request("technique").as(Technique.class);
		
		while (techniqueCursor.hasNext()){
			Technique t = techniqueCursor.next();
			liste_techniques.add(t);
			techniques_id.put(t.getNom(), t.get_id());
		}
		Messages.setTechniques_id(techniques_id);
		
		listView_techniques.setItems(liste_techniques);
		
		
    	
    }
    
    @FXML
    public void onEditerTraitementButton(){
    	

    	annuler.setVisible(true);
    	editer.setVisible(false);
    	mise_a_jour_technique.setVisible(true);
    	nom_technique_textField.setEditable(true);
    	nom_complet_technique_textField.setEditable(true);
    	nom_complet_technique_textField.setEditable(true);
		remarques_technique_textArea.setEditable(true);
		
		edit = true;

	
    }
    
    @FXML
    public void onAnnulerEditButton(){
    	
    	annuler.setVisible(false);
    	editer.setVisible(true);
    	mise_a_jour_technique.setVisible(false);
    	nom_technique_textField.setEditable(false);
    	nom_complet_technique_textField.setEditable(false);
		remarques_technique_textArea.setEditable(false);
		nouvelle_technique.setVisible(true);
		rafraichirAffichage();
		listView_techniques.getSelectionModel().select(techniqueSelectionne);
    	
    	edit = false;
    	
    }
    
    @FXML
    public void onMiseAJourTechniqueButton(){

    	if (techniqueSelectionne == null) {
    		techniqueSelectionne = new Technique();
    	}
    	
    	techniqueSelectionne.setNom(nom_technique_textField.getText());
    	techniqueSelectionne.setRemarques(remarques_technique_textArea.getText());
    	techniqueSelectionne.setNom_complet(nom_complet_technique_textField.getText());
    	
    	listView_techniques.getSelectionModel().select(techniqueSelectionne);
		
		if (edit) {
			Technique.update(techniqueSelectionne);
			afficherTechnique();
			rafraichirAffichage();
			onAnnulerEditButton();
		}
		else {
			
		   Technique.save(techniqueSelectionne);
		   onAnnulerEditButton();
		}
    	
    }
    
    public void afficherTechnique(){

		remarques_technique_textArea.setEditable(false);
		nom_complet_technique_textField.setEditable(false);
		nom_technique_textField.setEditable(false);
        editer.setVisible(true);
        mise_a_jour_technique.setVisible(false);
		annuler.setVisible(false);
		
		fiche_technique_label.setText("FICHE PRODUIT :");
		
		nom_technique_label.setText(techniqueSelectionne.getNom());
		nom_technique_textField.setText(techniqueSelectionne.getNom());
		nom_complet_technique_textField.setText(techniqueSelectionne.getNom_complet());
		remarques_technique_textArea.setText(techniqueSelectionne.getRemarques());
    }
    
    public void onTechniqueSelect(){
    	
    	techniqueSelectionne = listView_techniques.getSelectionModel().getSelectedItem();
    	
    	if(techniqueSelectionne != null){
    		afficherTechnique();
    	}
    		
    }   
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		techniques_id = new TreeMap<>();

		nom_technique_textField.setEditable(false);
		nom_complet_technique_textField.setEditable(false);
		remarques_technique_textArea.setEditable(false);
        editer.setVisible(true);
        mise_a_jour_technique.setVisible(false);
		annuler.setVisible(false);
		
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
		techniques_button.setVisible(false);
		
		file_path_textField.setVisible(false);
		importerButton.setVisible(false);

		liste_techniques  = FXCollections.observableArrayList();
		
		currentStage = Messages.getStage();
		
        techniqueCursor = MongoAccess.request("technique").as(Technique.class);
		
        while (techniqueCursor.hasNext()){
			Technique t = techniqueCursor.next();
			liste_techniques.add(t);
			techniques_id.put(t.getNom(), t.get_id());
		}
		Messages.setTechniques_id(techniques_id);
		
		listView_techniques.setItems(liste_techniques);
		
		listView_techniques.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			onTechniqueSelect();
		});
		
		listView_techniques.getSelectionModel().select(0);
		techniqueSelectionne = listView_techniques.getSelectionModel().getSelectedItem();

	}

}

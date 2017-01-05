package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.jongo.MongoCursor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import utils.RestAccess;
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

public class Fiche_produit_controller  implements Initializable{

	@FXML
	private ObservableList<Produit> liste_produits;
	
	@FXML
	private ListView<Traitement> listView_traitements;
	@FXML
	private ListView<Produit> listView_produits;
	@FXML
	private Button nouveau_produit;
	@FXML
	private Button mise_a_jour_traitement;
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
	private Button importerButton;
	@FXML
	private TextField file_path_textField;
	
	@FXML
	private Label fiche_traitement_label;
	@FXML
	private Label nom_traitement_label;
	@FXML
	private TextField nom_produit_textField;
	@FXML
	private TextField nom_complet_produit_textField;
	@FXML
	private TextArea remarques_produit_textArea;
	
	private boolean edit = false;
	
	private File file;

	MongoCursor<Produit> produitCursor ;
	Produit produitSelectionne;
	TacheTraitement traitementSelectionne;
	
	Stage currentStage;

	Produit detail;
	
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
    @FXML
    public void onTechniques_button(){
    	Scene fiche_technique_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_technique.fxml"), 1275, 722);
		fiche_technique_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_technique_scene);
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
			Documents.read(file, "produit");
			file_path_textField.setVisible(false);
			importerButton.setVisible(false);
			rafraichirAffichage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    private void affichageInfos(Produit produitSelectionne){

    	
    	nom_produit_textField.setText(produitSelectionne.getNom());
    	remarques_produit_textArea.setText(produitSelectionne.getRemarques());
    	
    	liste_produits.clear();
    	
    	if (produitSelectionne != null){
    		
    		String jsonString = RestAccess.requestAll("produit");
    		ObjectMapper om = new ObjectMapper();

    		try {
    			List<Produit> produits = om.readValue(jsonString, new TypeReference<List<Produit>>() {});
    			liste_produits.addAll(produits);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}	
    		listView_produits.setItems(liste_produits);		
    	}	
    }
    
    public void onNouveauProduitButton() {
    	
    	mise_a_jour_traitement.setText("Enregistrer");
    	nom_produit_textField.setText("");
    	remarques_produit_textArea.setText("");
    	nom_produit_textField.setPromptText("saisir le nom affiché du nouveau traitement");
    	nom_produit_textField.setPromptText("saisir le nom complet du nouveau traitement");
    	remarques_produit_textArea.setPromptText("éventuelles remarques");
    	nouveau_produit.setVisible(false);
    	
    	produitSelectionne = new Produit();
    	
    	edit = false;
    	annuler.setVisible(true);
    	editer.setVisible(false);
    	mise_a_jour_traitement.setVisible(true);
    	nom_produit_textField.setEditable(true);
    	nom_complet_produit_textField.setEditable(true);
		remarques_produit_textArea.setEditable(true);
    	
    	
    }
    
    public void onAnnulerButton() {
    	
    	nom_produit_textField.setEditable(false);
    	nom_complet_produit_textField.setEditable(false);
		remarques_produit_textArea.setEditable(false);
    	
    	mise_a_jour_traitement.setText("Mise à jour");
    	nom_produit_textField.setText("");
    	remarques_produit_textArea.setText("");
    	nom_produit_textField.setPromptText("");
    	nom_complet_produit_textField.setPromptText("");
    	remarques_produit_textArea.setPromptText("");
    	nouveau_produit.setText("Nouveau traitement");
    	rafraichirAffichage();
    	listView_produits.getSelectionModel().select(produitSelectionne);
    	affichageInfos(produitSelectionne);
    	
    }
    
    public void rafraichirAffichage(){

		liste_produits  = FXCollections.observableArrayList();

		String jsonString = RestAccess.requestAll("produit");
		ObjectMapper om = new ObjectMapper();

		try {
			List<Produit> produits = om.readValue(jsonString, new TypeReference<List<Produit>>() {});
			liste_produits.addAll(produits);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		listView_produits.setItems(liste_produits);
    	
    }
    
    @FXML
    public void onEditerTraitementButton(){
    	

    	annuler.setVisible(true);
    	editer.setVisible(false);
    	mise_a_jour_traitement.setVisible(true);
    	nom_produit_textField.setEditable(true);
    	nom_complet_produit_textField.setEditable(true);
		remarques_produit_textArea.setEditable(true);
		
		edit = true;

	
    }
    
    @FXML
    public void onAnnulerEditButton(){
    	
    	annuler.setVisible(false);
    	editer.setVisible(true);
    	mise_a_jour_traitement.setVisible(false);
    	nom_produit_textField.setEditable(false);
    	nom_complet_produit_textField.setEditable(false);
		remarques_produit_textArea.setEditable(false);
		nouveau_produit.setVisible(true);
		rafraichirAffichage();
		listView_produits.getSelectionModel().select(produitSelectionne);
    	affichageInfos(produitSelectionne);
    	
    	edit = false;
    	
    }
    
    @FXML
    public void onMiseAJourTraitementButton(){

    	if (produitSelectionne == null) {
    		produitSelectionne = new Produit();
    	}
    	
    	produitSelectionne.setNom(nom_produit_textField.getText());
    	produitSelectionne.setRemarques(remarques_produit_textArea.getText());
    	produitSelectionne.setNom_complet(nom_complet_produit_textField.getText());
    	
    	annuler.setVisible(false);
    	editer.setVisible(true);
    	mise_a_jour_traitement.setVisible(false);
    	nom_produit_textField.setEditable(false);
		remarques_produit_textArea.setEditable(false);
		
		if (edit) {
			Produit.update(produitSelectionne);
			afficherProduit();
			rafraichirAffichage();
			onAnnulerEditButton();
		}
		else {
			
		   Produit.save(produitSelectionne);
		   afficherProduit();
		   onAnnulerEditButton();
		}
    	
    }
    
    public void afficherProduit(){

		remarques_produit_textArea.setEditable(false);
		nom_complet_produit_textField.setEditable(false);
		nom_produit_textField.setEditable(false);
        editer.setVisible(true);
        mise_a_jour_traitement.setVisible(false);
		annuler.setVisible(false);
		
		fiche_traitement_label.setText("FICHE PRODUIT :");
		nom_traitement_label.setText(produitSelectionne.getNom());
		nom_produit_textField.setText(produitSelectionne.getNom());
		nom_complet_produit_textField.setText(produitSelectionne.getNom_complet());
		remarques_produit_textArea.setText(produitSelectionne.getRemarques());
		//rafraichirAffichage();
    }

    @FXML
    public void onAjoutDetail(){}
    @FXML
    public void onVersFichiersButton(){}
    @FXML
    public void onVersModelesButton(){}
    @FXML
    public void onProduitSelect(){
    	
    	produitSelectionne = listView_produits.getSelectionModel().getSelectedItem();
    	Messages.setProduit(produitSelectionne);
    	afficherProduit();	
    }   

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		produitSelectionne = Messages.getProduit();

		nom_produit_textField.setEditable(false);
		nom_complet_produit_textField.setEditable(false);
		remarques_produit_textArea.setEditable(false);
        editer.setVisible(true);
        mise_a_jour_traitement.setVisible(false);
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
		versProduitsButton.setVisible(false);
		
		file_path_textField.setVisible(false);
		importerButton.setVisible(false);

		liste_produits  = FXCollections.observableArrayList();
		String jsonString = RestAccess.requestAll("produit");
		ObjectMapper om = new ObjectMapper();

		try {
			List<Produit> produits = om.readValue(jsonString, new TypeReference<List<Produit>>() {});
			liste_produits.addAll(produits);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		listView_produits.setItems(liste_produits);
		
		currentStage = Messages.getStage();
	}

}

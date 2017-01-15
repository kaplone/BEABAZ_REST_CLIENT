package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import enums.Progression;
import utils.JsonUtils;
import utils.RestAccess;
import models.Messages;
import models.Produit;
import models.Traitement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Fiche_traitement_controller  implements Initializable{
	
	@FXML
	private ObservableList<Traitement> liste_traitements;
	@FXML
	private ObservableList<Produit> liste_produits;
	@FXML
	private TextField file_path_textField;
	@FXML
	private ListView<Traitement> listView_traitements;
	@FXML
	private ListView<Produit> listView_produits;
	@FXML
	private Button nouveau_traitement;
	@FXML
	private Button nouveau_detail;
	@FXML
	private Button mise_a_jour_traitement;
	@FXML
	private Button annuler;
	@FXML
	private Button editer;
	@FXML
	private Button versCommandeButton;
	@FXML
	private Button versTraitementsButton;
	@FXML
	private Button versOeuvreButton;
	@FXML
	private Button versRapportButton;
	@FXML
	private Button importerButton;
	
	@FXML
	private Label fiche_traitement_label;
	@FXML
	private Label nom_traitement_label;
	@FXML
	private TextField nom_traitement_textField;
	@FXML
	private TextField nom_complet_traitement_textField;
	@FXML
	private TextArea remarques_traitement_textArea;
	
	@FXML
	private HBox produitsLiesHbox;

	private boolean edit = false;
	
	ObservableList<String> liste_noms_produits;

	Traitement traitementSelectionne;
	Produit produitSelectionne;
	
	Stage currentStage;

	private File file;
	
	@FXML
	public void onVersProduitsButton(){
		
		Scene fiche_produit_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_produit.fxml"), 1275, 722);
		fiche_produit_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_produit_scene);	
	}
	@FXML
    public void onVersAuteursButton(){
    	Scene fiche_auteur_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_auteur.fxml"), 1275, 722);
		fiche_auteur_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_auteur_scene);
    }
	@FXML
    public void onVersFichiersButton(){}
	
    @FXML
    public void onVersModelesButton(){
    	Scene fiche_modele_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_model.fxml"), 1275, 722);
		fiche_modele_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_modele_scene);
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
	public void onAjoutTraitement(){
	}

	@FXML
	public void onTraitementSelect(){
		
		traitementSelectionne = listView_traitements.getSelectionModel().getSelectedItem();
		//Messages.setTraitement(traitementSelectionne);
		affichageInfos();	
		affichageProduits();
		
		
		produitsLiesHbox.getChildren().clear();
		for (Produit p : traitementSelectionne.getListe_produits()){
			affichageProduitUtilise(p);
		}
	}
	
	public void affichageProduitUtilise(Produit produitSelectionne2){
		
		ImageView iv = new ImageView(new Image(Progression.NULL_.getUsedImage()));
		iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);
        iv.setFitWidth(15);
		
		
		Button b = new Button(produitSelectionne2.getNom());
		Button b2 = new Button("", iv);
		
		b2.setOnAction((event) -> deleteProduitLie((Button)event.getSource()));

		produitsLiesHbox.getChildren().add(b);
		produitsLiesHbox.getChildren().add(b2);
		HBox.setMargin(b2, new Insets(0,10,0,0));
	}
	
	@FXML
	public void onProduitSelect(){
		
		produitSelectionne = listView_produits.getSelectionModel().getSelectedItem();
		
		if (produitSelectionne != null){
			
//			traitementSelectionne.addProduit(produitSelectionne, Messages.getProduits_id().get(produitSelectionne));
			Traitement.update(traitementSelectionne);
			
			affichageProduitUtilise(produitSelectionne);

		}
	
	}
	
	public void deleteProduitLie(Button e){
		
		int index = produitsLiesHbox.getChildren().indexOf(e);
		
//		Produit produit = RestAccess.request("produit", "nom",  ((Button) produitsLiesHbox.getChildren().get(index -1)).getText()).as(Produit.class);
		
		produitsLiesHbox.getChildren().remove(index -1, index +1);
		
//		traitementSelectionne.deleteProduit(produit);
		
		Traitement.update(traitementSelectionne);
		
		produitsLiesHbox.getChildren().clear();
		for (Produit p : traitementSelectionne.getListe_produits()){
			produitSelectionne = p;
			affichageProduitUtilise(p);
		}
		
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
			Documents.read(file, "traitement");
			file_path_textField.setVisible(false);
			importerButton.setVisible(false);
			afficherTraitements();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    private void affichageInfos(){
	
    	nom_traitement_textField.setText(traitementSelectionne.getNom());
    	nom_traitement_label.setText(traitementSelectionne.getNom());
    	nom_complet_traitement_textField.setText(traitementSelectionne.getNom_complet());
    	remarques_traitement_textArea.setText(traitementSelectionne.getRemarques());
    	
    		
    }
    
    public void affichageProduits(){
    	
    	liste_produits.clear();
    	
    	JsonUtils.JsonToListObj(RestAccess.requestAll("produit"), liste_produits, new TypeReference<List<Produit>>() {});
    	
    	listView_produits.setItems(liste_produits);
    	
    	
    }
    
    public void onNouveauTraitementButton() {
    	
    	mise_a_jour_traitement.setText("Enregistrer");
    	nom_traitement_textField.setText("");
    	remarques_traitement_textArea.setText("");
    	nom_traitement_textField.setPromptText("saisir le nom affiché du nouveau traitement");
    	nom_complet_traitement_textField.setPromptText("saisir le nom complet du nouveau traitement");
    	remarques_traitement_textArea.setPromptText("éventuelles remarques");
    	nouveau_traitement.setVisible(false);
    	
    	traitementSelectionne = new Traitement();
    	
    	edit = false;
    	annuler.setVisible(true);
    	editer.setVisible(false);
    	mise_a_jour_traitement.setVisible(true);
    	nom_traitement_textField.setEditable(true);
    	nom_complet_traitement_textField.setEditable(true);
		remarques_traitement_textArea.setEditable(true);
    	
    	
    }
    
    public void onAnnulerButton() {
    	
    	nom_traitement_textField.setEditable(false);
    	nom_complet_traitement_textField.setEditable(false);
		remarques_traitement_textArea.setEditable(false);
    	
    	mise_a_jour_traitement.setText("Mise à jour");
    	nom_traitement_textField.setText("");
    	remarques_traitement_textArea.setText("");
    	nom_traitement_textField.setPromptText("");
    	remarques_traitement_textArea.setPromptText("");
    	nouveau_traitement.setText("Nouveau traitement");
    	rafraichirAffichage();
    	listView_traitements.getSelectionModel().select(traitementSelectionne);
    	affichageInfos();
    	
    }
    
    public void rafraichirAffichage(){
    	
    	liste_traitements = FXCollections.observableArrayList();
		liste_produits  = FXCollections.observableArrayList();
		
		JsonUtils.JsonToListObj(RestAccess.requestAll("traitement"), liste_traitements, new TypeReference<List<Traitement>>() {});
		JsonUtils.JsonToListObj(RestAccess.requestAll("produit"), liste_produits, new TypeReference<List<Produit>>() {});

		listView_traitements.setItems(liste_traitements);
		listView_produits.setItems(liste_produits);
    	
    }
    
    @FXML
    public void onEditerTraitementButton(){
    	

    	annuler.setVisible(true);
    	editer.setVisible(false);
    	mise_a_jour_traitement.setVisible(true);
    	nom_traitement_textField.setEditable(true);
    	nom_complet_traitement_textField.setEditable(true);
		remarques_traitement_textArea.setEditable(true);
		
		edit = true;

	
    }
    
    @FXML
    public void onAnnulerEditButton(){
    	
    	annuler.setVisible(false);
    	editer.setVisible(true);
    	mise_a_jour_traitement.setVisible(false);
    	nom_traitement_textField.setEditable(false);
    	nom_complet_traitement_textField.setEditable(false);
		remarques_traitement_textArea.setEditable(false);
		nouveau_traitement.setVisible(true);
		rafraichirAffichage();
		listView_traitements.getSelectionModel().select(traitementSelectionne);
    	affichageInfos();
    	
    	edit = false;
    	
    }
    
    @FXML
    public void onMiseAJourTraitementButton(){

    	if (traitementSelectionne == null) {
    		traitementSelectionne = new Traitement();
    	}
    	
    	traitementSelectionne.setNom(nom_traitement_textField.getText());
    	traitementSelectionne.setRemarques(remarques_traitement_textArea.getText());
    	traitementSelectionne.setNom_complet(nom_complet_traitement_textField.getText());
		
		if (edit) {
			Traitement.update(traitementSelectionne);
			afficherTraitement();
			rafraichirAffichage();
			onAnnulerEditButton();
		}
		else {

		   Traitement.save(traitementSelectionne);
		   afficherTraitement();
		   onAnnulerEditButton();
		}
    	
    }
    
    public void afficherTraitement(){

		remarques_traitement_textArea.setEditable(false);
        editer.setVisible(true);
        mise_a_jour_traitement.setVisible(false);
		annuler.setVisible(false);
		fiche_traitement_label.setText("FICHE TRAITEMENT :");
		nom_traitement_label.setText(traitementSelectionne.getNom());
		
		rafraichirAffichage();
    }
    
    public void afficherTraitements(){

		remarques_traitement_textArea.setEditable(false);
        editer.setVisible(true);
        mise_a_jour_traitement.setVisible(false);
		annuler.setVisible(false);
		fiche_traitement_label.setText("FICHE TRAITEMENT :");
		nom_traitement_textField.setDisable(true);
		remarques_traitement_textArea.setDisable(true);
		
//        liste_traitements.clear();
//        JsonUtils.JsonToListObj(RestAccess.requestAll("traitement"), liste_traitements, new TypeReference<List<Traitement>>() {});	
//		listView_traitements.setItems(liste_traitements);	
//		
		rafraichirAffichage();		
    }
    
    @FXML
    public void onAjoutProduit(){
    	
    	//Messages.setTacheTraitementEdited(listView_traitements.getSelectionModel().getSelectedItem());
    	
    	Scene fiche_produit_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_produit.fxml"), 1275, 722);
		fiche_produit_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_produit_scene);
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		//traitementSelectionne = Messages.getTraitement();
		
		liste_noms_produits = FXCollections.observableArrayList();

		nom_traitement_textField.setEditable(false);
		nom_complet_traitement_textField.setEditable(false);
		remarques_traitement_textArea.setEditable(false);
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
		versTraitementsButton.setVisible(false);
		versRapportButton.setVisible(false);
		
		file_path_textField.setVisible(false);
		importerButton.setVisible(false);
		
		
		liste_traitements = FXCollections.observableArrayList();
		JsonUtils.JsonToListObj(RestAccess.requestAll("traitement"), liste_traitements, new TypeReference<List<Traitement>>() {});
		
		liste_produits  = FXCollections.observableArrayList();
		JsonUtils.JsonToListObj(RestAccess.requestAll("produit"), liste_produits, new TypeReference<List<Produit>>() {});
		listView_produits.setItems(liste_produits);
		listView_traitements.setItems(liste_traitements);
		
		currentStage = Messages.getStage();
		
		affichageProduits();

	}

}

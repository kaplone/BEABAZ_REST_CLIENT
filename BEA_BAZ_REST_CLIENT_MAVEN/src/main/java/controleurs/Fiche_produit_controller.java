package controleurs;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.type.TypeReference;

import application.Documents;
import utils.JsonUtils;
import utils.RestAccess;
import models.Messages;
import models.Produit;
import models.TacheTraitement;
import models.Traitement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Fiche_produit_controller extends Fiche_controller implements Initializable{

	@FXML
	private ObservableList<Produit> liste_produits;
	@FXML
	private ListView<Traitement> listView_traitements;
	@FXML
	private ListView<Produit> listView_produits;

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
	
	private File file;
	
	Produit produitSelectionne;
	TacheTraitement traitementSelectionne;
	
	Stage currentStage;

	Produit detail;
		
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
			e.printStackTrace();
		}
	}
	
    private void affichageInfos(Produit produitSelectionne){
	
    	nom_produit_textField.setText(produitSelectionne.getNom());
    	remarques_produit_textArea.setText(produitSelectionne.getRemarques());
    	
    	liste_produits.clear();
    	JsonUtils.JsonToListObj(RestAccess.requestAll("produit"), liste_produits, new TypeReference<List<Produit>>() {});
    	
    	if (produitSelectionne != null){
    		listView_produits.setItems(liste_produits);		
    	}	
    }
    
    public void onNouveauProduitButton() {
    	super.onNouveauButton();   	
    	editability(true);
    	raz();
    	
    	produitSelectionne = new Produit();
    }
    
    public void onAnnulerButton() {
    	super.etatInitial();
    	editability(false);
    	raz();
    	
    	mise_a_jour.setText("Mise à jour");
    	nouveau.setText("Nouveau traitement");
    	rafraichirAffichage();
    	listView_produits.getSelectionModel().select(produitSelectionne);
    	affichageInfos(produitSelectionne);
    	
    }
    
    public void rafraichirAffichage(){

		liste_produits  = FXCollections.observableArrayList();
		JsonUtils.JsonToListObj(RestAccess.requestAll("produit"), liste_produits, new TypeReference<List<Produit>>() {});
		
		listView_produits.setItems(liste_produits);
    	
    }
    
    @FXML
    public void onEditerTraitementButton(){
    	super.onEditerButton();
    	editability(true);
    }
    
    @FXML
    public void onAnnulerEditButton(){
    	super.onAnnulerEditButton();
    	editability(false);
		nouveau.setVisible(true);
		
		rafraichirAffichage();
		listView_produits.getSelectionModel().select(produitSelectionne);
    	affichageInfos(produitSelectionne);    	
    }
    
    @FXML
    public void onMiseAJourTraitementButton(){
    	super.onMiseAJourButton();
    	editability(false);

    	if (produitSelectionne == null) {
    		produitSelectionne = new Produit();
    	}
    	
    	produitSelectionne.setNom(nom_produit_textField.getText());
    	produitSelectionne.setRemarques(remarques_produit_textArea.getText());
    	produitSelectionne.setNom_complet(nom_complet_produit_textField.getText());
		
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
		editability(false);
		editer.setVisible(true);
		
		fiche_traitement_label.setText("FICHE PRODUIT :");
		nom_traitement_label.setText(produitSelectionne.getNom());
		nom_produit_textField.setText(produitSelectionne.getNom());
		nom_complet_produit_textField.setText(produitSelectionne.getNom_complet());
		remarques_produit_textArea.setText(produitSelectionne.getRemarques());
		//rafraichirAffichage();
    }

//    @FXML
//    public void onAjoutDetail(){}

    @FXML
    public void onProduitSelect(){
    	
    	produitSelectionne = listView_produits.getSelectionModel().getSelectedItem();
    	Messages.setProduit(produitSelectionne);
    	afficherProduit();	
    }  
    
    public void editability(boolean bool){
    	remarques_produit_textArea.setEditable(bool);
		nom_complet_produit_textField.setEditable(bool);
		nom_produit_textField.setEditable(bool);
		
		prompt(bool);
    }
    
    public void raz(){
    	remarques_produit_textArea.setText("");
    	nom_complet_produit_textField.setText("");
    	nom_produit_textField.setText("");
    }
    
    public void prompt(boolean bool){
    	
    	if (bool){
    		remarques_produit_textArea.setPromptText("éventuelles remarques");
    		nom_complet_produit_textField.setPromptText("saisir le nom complet pour l'export");
        	nom_produit_textField.setPromptText("saisir le nom affiché dans les menus de l'interface");
    	}
    	else {
    		remarques_produit_textArea.setPromptText(null);
    		nom_complet_produit_textField.setPromptText(null);
    		nom_produit_textField.setPromptText(null);
    	}
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.init();
        editability(false);
        
        versProduitsButton.setVisible(false);
        file_path_textField.setVisible(false);
		importerButton.setVisible(false);

		produitSelectionne = Messages.getProduit();

		liste_produits  = FXCollections.observableArrayList();
		JsonUtils.JsonToListObj(RestAccess.requestAll("produit"), liste_produits, new TypeReference<List<Produit>>() {});
		
		listView_produits.setItems(liste_produits);
	}
}

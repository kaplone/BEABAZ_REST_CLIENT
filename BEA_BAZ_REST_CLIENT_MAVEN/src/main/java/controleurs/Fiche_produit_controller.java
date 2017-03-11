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
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Fiche_produit_controller extends Fiche_controller implements Initializable {

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
	private TextField nom_produit_textField;
	@FXML
	private TextField nom_complet_produit_textField;
	@FXML
	private TextArea remarques_produit_textArea;

	private File file;

	Produit produitSelectionne;
	TacheTraitement traitementSelectionne;

	Produit detail;

	protected File chooseExport() {

		Stage newStage = new Stage();

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Fichier à importer");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("feuille de calcul", "*.xlsx"));
		File selectedFile = fileChooser.showOpenDialog(newStage);
		if (selectedFile != null) {
			return selectedFile;
		} else {
			return (File) null;
		}
	}

	@FXML
	public void on_select_file_button() {

		file = chooseExport();

		if (file != null) {
			file_path_textField.setVisible(true);
			file_path_textField.setText(file.toString());
			importerButton.setVisible(true);
		}

	}

	@FXML
	public void on_import_file_button() {
		try {
			Documents.read(file, "produit");
			file_path_textField.setVisible(false);
			importerButton.setVisible(false);
			rafraichirAffichage();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void onProduitSelect() {
		super.onAnnulerEditButton();

		if (listView_produits.getSelectionModel().getSelectedItem() == null) {
			editer.setVisible(false);
		} else {

			if (listView_produits.getSelectionModel().getSelectedItem().getNom() != null) {
				produitSelectionne = listView_produits.getSelectionModel().getSelectedItem();
				afficherProduit();
				editer.setVisible(true);
			} else {
				editer.setVisible(false);
			}
		}
	}

	public void rafraichirAffichage() {

		liste_produits = FXCollections.observableArrayList();
		JsonUtils.JsonToListObj(RestAccess.requestAll("produit"), liste_produits, new TypeReference<List<Produit>>() {
		});

		listView_produits.setItems(liste_produits);
		afficherProduit();

	}

	public void onNouveauProduitButton() {
		super.onNouveauButton();
		
		produitSelectionne = new Produit();
	}
	
	@FXML
	public void onEditerTraitementButton() {
		super.onEditerButton();
	}

	@FXML
	public void onAnnulerEditButton() {
		super.onAnnulerEditButton();	
		onProduitSelect();
	}

	@FXML
	public void onMiseAJourTraitementButton() {
		
		produitSelectionne.setNom(nom_produit_textField.getText());
		produitSelectionne.setRemarques(remarques_produit_textArea.getText());
		produitSelectionne.setNom_complet(nom_complet_produit_textField.getText());

		if (edit) {
			produitSelectionne.update("produit");
		} else {

			produitSelectionne.save("produit", Produit.class);
			listView_produits.getSelectionModel().select(produitSelectionne);
			rafraichirAffichage();
		}
		
		Messages.resetTous_les_produits();
		super.onMiseAJourButton();
	}

	public void afficherProduit() {
		editability(false);
		editer.setVisible(true);
		prompt(false);
        
		if (produitSelectionne != null){
			nom_label.setText(produitSelectionne.getNom());
			nom_produit_textField.setText(produitSelectionne.getNom());
			nom_complet_produit_textField.setText(produitSelectionne.getNom_complet());
			remarques_produit_textArea.setText(produitSelectionne.getRemarques());
		}		
	}
    
	@Override
	public void editability(boolean bool) {
		remarques_produit_textArea.setEditable(bool);
		nom_complet_produit_textField.setEditable(bool);
		nom_produit_textField.setEditable(bool);

		prompt(bool);
	}
     
	@Override
	public void raz() {
		remarques_produit_textArea.setText("");
		nom_complet_produit_textField.setText("");
		nom_produit_textField.setText("");
	}
    
	@Override
	public void prompt(boolean bool) {

		if (bool) {
			remarques_produit_textArea.setPromptText("éventuelles remarques");
			nom_complet_produit_textField.setPromptText("saisir le nom complet pour l'export");
			nom_produit_textField.setPromptText("saisir le nom affiché dans les menus de l'interface");
		} else {
			remarques_produit_textArea.setPromptText(null);
			nom_complet_produit_textField.setPromptText(null);
			nom_produit_textField.setPromptText(null);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.init();

		versProduitsButton.setVisible(false);
		file_path_textField.setVisible(false);
		importerButton.setVisible(false);

		produitSelectionne = Messages.getProduit();

		liste_produits = FXCollections.observableArrayList();
		JsonUtils.JsonToListObj(RestAccess.requestAll("produit"), liste_produits, new TypeReference<List<Produit>>() {});

		listView_produits.setItems(liste_produits);
		
		listView_produits.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			onProduitSelect();
		});
		
		if (! liste_produits.isEmpty()){
			listView_produits.getSelectionModel().select(0);
		}
	}
}

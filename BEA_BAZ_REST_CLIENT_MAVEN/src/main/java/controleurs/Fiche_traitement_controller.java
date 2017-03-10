package controleurs;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import application.Documents;
import application.JfxUtils;
import enums.Progression;
import utils.JsonUtils;
import utils.RestAccess;
import models.Matiere;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Fiche_traitement_controller extends Fiche_controller implements Initializable {

	@FXML
	private TextField file_path_textField;
	@FXML
	private ListView<Traitement> listView_traitements;
	@FXML
	private ListView<Produit> listView_produits;
	@FXML
	private ListView<Produit> listView_all_produits;
	@FXML
	private Button nouveau_detail;
	@FXML
	private Button importerButton;
	
	@FXML
	private Button add_p;
	@FXML
	private Button remove_p;

	@FXML
	private TextField nom_traitement_textField;
	@FXML
	private TextField nom_complet_traitement_textField;
	@FXML
	private TextArea remarques_traitement_textArea;

	private ObservableList<Traitement> liste_traitements;
	private ObservableList<Produit> liste_produits;
	private ObservableList<Produit> liste_all_produits;
	private Set<Produit> produitsUtilises;
	private Map<String, Produit> map_produits;

	private Traitement traitementSelectionne;

	private File file;
	
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
			Documents.read(file, "traitement");
			file_path_textField.setVisible(false);
			importerButton.setVisible(false);
			rafraichirAffichage();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void onTraitementSelect() {
		super.onAnnulerEditButton();

		if (listView_traitements.getSelectionModel().getSelectedItem() == null) {
			editer.setVisible(false);
		} else {
			traitementSelectionne = listView_traitements.getSelectionModel().getSelectedItem();
			afficherTraitement();
			editer.setVisible(true);
		}
	}

	public void rafraichirAffichage() {

		liste_traitements = FXCollections.observableArrayList();

		JsonUtils.JsonToListObj(RestAccess.requestAll("traitement"), liste_traitements,
				new TypeReference<List<Traitement>>() {
				});

		listView_traitements.setItems(liste_traitements);
		afficherTraitement();
	}

	public void afficherproduits() {

		liste_all_produits.clear();
		liste_all_produits.addAll(Arrays.asList(Produit.retrouveProduits()));

		liste_produits.clear();
		liste_produits.addAll(produitsUtilises);
		listView_produits.setItems(liste_produits);

		miseAJourAffichageproduits();
	}

	public void miseAJourAffichageproduits() {

		listView_all_produits.setCellFactory(cell -> {

			return new ListCell<Produit>() {

				@Override
				protected void updateItem(Produit item, boolean empty) {
					super.updateItem(item, empty);

					if (item == null || empty) {
					}

					else {

						setText(item.getNom());

						if (liste_produits.contains(item)) {
							setDisable(true);
							setOpacity(0.5);
						}
					}
				}
			};
		});
	}

	public void ajouter_produit(String m) {
		if (m != null) {
			liste_produits.add(map_produits.get(m));
			listView_all_produits.getSelectionModel().select(null);
			listView_all_produits.getSelectionModel().clearSelection();
			miseAJourAffichageproduits();
			onEditerButton();
		}
	}

	public void retirer_produit(String m) {
		if (m != null) {
			liste_produits.remove(map_produits.get(m));
			miseAJourAffichageproduits();
			onEditerButton();
		}
	}

	public void affichageProduits() {

		liste_produits.clear();

		JsonUtils.JsonToListObj(RestAccess.requestAll("produit"), liste_produits, new TypeReference<List<Produit>>() {
		});

		listView_produits.setItems(liste_produits);

	}

	public void onNouveauTraitementButton() {
		super.onNouveauButton();

		traitementSelectionne = new Traitement();
		liste_produits.clear();
	}
	
	@FXML
	public void onEditerTraitementButton() {
		super.onEditerButton();
	}

	@FXML
	public void onAnnulerEditButton() {
		super.onAnnulerEditButton();
		onTraitementSelect();
	}

	@FXML
	public void onMiseAJourTraitementButton() {
		
		traitementSelectionne.setNom(nom_traitement_textField.getText());
		traitementSelectionne.setRemarques(remarques_traitement_textArea.getText());
		traitementSelectionne.setNom_complet(nom_complet_traitement_textField.getText());
		traitementSelectionne.setProduitsFromObservable(liste_produits);

		if (edit) {
			traitementSelectionne.update("traitement");
		} else {
			traitementSelectionne.save("traitement", Traitement.class);
			listView_traitements.getSelectionModel().select(traitementSelectionne);
			rafraichirAffichage();
		}
		
		super.onMiseAJourButton();
	}

	public void afficherTraitement() {
		nom_label.setText(traitementSelectionne.getNom());
		nom_traitement_textField.setText(traitementSelectionne.getNom());
		nom_complet_traitement_textField.setText(traitementSelectionne.getNom_complet());
		remarques_traitement_textArea.setText(traitementSelectionne.getRemarques());
		
		produitsUtilises = traitementSelectionne.getListe_produits_names().stream()
																		  .map(a -> map_produits.get(a))
																		  .collect(Collectors.toSet());
		liste_produits.setAll(produitsUtilises);
		miseAJourAffichageproduits();
	}

	@Override
    public void editability(boolean bool){
    	nom_traitement_textField.setEditable(bool);
    	nom_complet_traitement_textField.setEditable(bool);
		remarques_traitement_textArea.setEditable(bool);
		prompt(bool);
    }
    
    @Override
    public void raz(){
    	nom_traitement_textField.setText("");
    	nom_complet_traitement_textField.setText("");
    	remarques_traitement_textArea.setText("");
    }
    
    @Override
    public void prompt(boolean bool){
    	
    	if (bool){
    		nom_traitement_textField.setPromptText("saisir le nom affiché du nouveau traitement");
        	nom_complet_traitement_textField.setPromptText("saisir le nom complet du nouveau traitement");
        	remarques_traitement_textArea.setPromptText("éventuelles remarques");
    	}
    	else {
    		nom_traitement_textField.setPromptText(null);
        	nom_complet_traitement_textField.setPromptText(null);
        	remarques_traitement_textArea.setPromptText(null);
    	}	
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.init();
		
		versTraitementsButton.setVisible(false);

		nom_traitement_textField.setEditable(false);
		nom_complet_traitement_textField.setEditable(false);
		remarques_traitement_textArea.setEditable(false);

		file_path_textField.setVisible(false);
		importerButton.setVisible(false);
		
		produitsUtilises = new HashSet<>();
		map_produits = new HashMap<>();

		liste_traitements = FXCollections.observableArrayList();
		JsonUtils.JsonToListObj(RestAccess.requestAll("traitement"), liste_traitements,
				new TypeReference<List<Traitement>>() {
				});
		
		liste_produits = FXCollections.observableArrayList();
		liste_all_produits = FXCollections.observableArrayList();
		JsonUtils.JsonToListObj(RestAccess.requestAll("produit"), liste_all_produits,
				new TypeReference<List<Produit>>() {
				});
		listView_produits.setItems(liste_produits);
		listView_all_produits.setItems(liste_all_produits);
		
		liste_all_produits.forEach(a -> map_produits.put(a.getNom(), a));

		listView_traitements.setItems(liste_traitements);
		listView_traitements.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			onTraitementSelect();
		});
		if (! liste_traitements.isEmpty()){
			listView_traitements.getSelectionModel().select(0);
		}

		
		listView_all_produits.onDragDetectedProperty().set(dd -> {
			
			Dragboard dragBoard = listView_all_produits.startDragAndDrop(TransferMode.MOVE);			
			ClipboardContent content = new ClipboardContent();	
			if (listView_all_produits.getSelectionModel().getSelectedItem() != null){
				content.putString(listView_all_produits.getSelectionModel().getSelectedItem().getNom());			 
				dragBoard.setContent(content);
				 
				listView_produits.setOnDragOver(dd_ -> dd_.acceptTransferModes(TransferMode.MOVE));
				 
				listView_produits.setOnDragDropped(dd_ -> {
					ajouter_produit(dd_.getDragboard().getString());
				    dd_.setDropCompleted(true);
				 });
			}
		});
		
		listView_all_produits.setOnDragDropped(dd_ -> dd_.setDropCompleted(true));
		
		listView_produits.onDragDetectedProperty().set(dd -> {
			
			Dragboard dragBoard = listView_produits.startDragAndDrop(TransferMode.MOVE);			
			ClipboardContent content = new ClipboardContent();	
			if (listView_produits.getSelectionModel().getSelectedItem() != null){
				content.putString(listView_produits.getSelectionModel().getSelectedItem().getNom());			 
				dragBoard.setContent(content);
				 
				listView_all_produits.setOnDragOver(dd_ -> dd_.acceptTransferModes(TransferMode.MOVE));
				 
				listView_all_produits.setOnDragDropped(dd_ -> {
					retirer_produit(dd_.getDragboard().getString());
				    dd_.setDropCompleted(true);
				 });
			}	
		});
		
		listView_produits.setOnDragDropped(dd_ -> dd_.setDropCompleted(true));

		add_p.setOnAction(a -> ajouter_produit(listView_all_produits.getSelectionModel().getSelectedItem().getNom()));
		remove_p.setOnAction(a -> retirer_produit(listView_produits.getSelectionModel().getSelectedItem().getNom()));
	}
}

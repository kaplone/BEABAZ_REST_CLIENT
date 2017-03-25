package controleurs;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.jongo.MongoCursor;

import application.Documents;
import utils.Walk;
import models.Client;
import models.Commande;
import models.Contexte;
import models.Messages;
import models.Oeuvre;
import models.OeuvreTraitee;
import models.Traitement;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Fiche_commande_import_controller extends Fiche_controller implements Initializable{
	
	@FXML
	private ObservableList<Oeuvre> liste_oeuvres;
	@FXML
	private Label nomClientLabel;
	@FXML
	private Label fiche_commande_label;
	@FXML
	private Label nom_commande_label;
	@FXML
	private TextField nomCommandeTextField;
	@FXML
	private TableColumn<OeuvreTraitee, String> oeuvres_nom_colonne;
	@FXML
	private TableColumn<OeuvreTraitee, ImageView> oeuvres_fait_colonne;
	
	@FXML
	private TextArea remarques_client;
	@FXML
	private Button nouvelle_oeuvre;
	@FXML
	private Button mise_a_jour_commande;
	@FXML
	private Button annuler;
	@FXML
	private Button editer;
	@FXML
	private Button importCommandeButton;
	@FXML
	private Button versClientButton;
	@FXML
	private Button versCommandeButton;
	@FXML
	private Button versOeuvreButton;
	@FXML
	private Button versRapportButton;
	@FXML
	private Button versFichiersButton;
	@FXML
	private Button versModelesButton;
	@FXML
	private Button versTraitementsButton;
	@FXML
	private Button versAuteursButton;
	@FXML
	private Button versProduitsButton;
	@FXML
	private Button rapportsButton;
	@FXML
	private DatePicker dateCommandePicker;
	@FXML
	private DatePicker dateDebutProjetPicker;
	@FXML
	private DatePicker dateFinProjetPicker;
	
	@FXML
	private TextField file_path_textField;
	@FXML
	private Button select_file_button;
	@FXML
	private Button import_file_button;
	
	@FXML
	private GridPane grid;
	@FXML
	private HBox hbox_1;
	@FXML
	private HBox hbox_2;
	@FXML
	private HBox hbox_3;
	
	@FXML
	private VBox commandeExportVbox;
	
	@FXML
	private GridPane traitementGrid;
	
	@FXML
	private TableView<OeuvreTraitee> tableOeuvre;
	
	@FXML
	private Button import_rep_button;
	@FXML
	private Button select_rep_button;
	@FXML
	private TextField rep_path_textField;
	
	@FXML
	private Label message_label;
	
	private ArrayList<ChoiceBox<Traitement>> traitements_selectionnes;
	private ArrayList<Traitement> traitements_attendus;
	private ObservableList<Traitement> observableTraitements;

	
	private MongoCursor<OeuvreTraitee> oeuvresTraiteesCursor;
	private OeuvreTraitee oeuvreTraiteeSelectionne;
	
	private List<OeuvreTraitee> oeuvresTraitees;
	
	private Commande commande;
	private Commande commandeSelectionne;
	
	private Client client;
	
	private File file;
	private File dir;
	
	private static StringProperty bindLabel;
	
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
		file_path_textField.setText(file.toString());
	}
	@FXML
	public void on_import_file_button(){
		try {
			Documents.read(file);
			onVersCommandeButton();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void onImportImagesButton(){
		
		select_rep_button.setVisible(true);
		rep_path_textField.setVisible(true);
		import_rep_button.setVisible(true);
	}
	@FXML
	public void on_select_rep_button(){
		dir = chooseRep();
		rep_path_textField.setText(dir.toString());
	}
	@FXML
	public void on_import_rep_button(){

		Walk.walking(dir.toPath());
		onVersCommandeButton();	
	}
	
	
	
    protected File chooseRep(){
		
		Stage newStage = new Stage();
		
		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Dossier à importer");
		File selectedDir = dirChooser.showDialog(newStage);
		if (selectedDir != null) {
			 return selectedDir;
		}
		else {
			 return (File) null;
		}		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		bindLabel = new SimpleStringProperty("En attente d'import ...");
		message_label.textProperty().bind(bindLabel);
		
		commande = Messages.getCommande();
		commandeSelectionne = Messages.getCommande();
		client = Messages.getClient();

		versClientButton.setVisible(true);
		versCommandeButton.setVisible(false);
		versOeuvreButton.setVisible(false);
		versRapportButton.setVisible(false);
		
		versModelesButton.setVisible(true);
		versTraitementsButton.setVisible(true);
		versFichiersButton.setVisible(true);
		versProduitsButton.setVisible(true);
		versAuteursButton.setVisible(true);
		
		grid.setVisible(false);
		hbox_1.setVisible(false);
		hbox_2.setVisible(false);
		hbox_3.setVisible(false);
		
		currentStage = Messages.getStage();
		
		liste_oeuvres = FXCollections.observableArrayList();

		traitements_selectionnes = new ArrayList<>();
		oeuvresTraitees = new ArrayList<>();

	}
	@Override
	public void editability(boolean b) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void prompt(boolean b) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void raz() {
		// TODO Auto-generated method stub
		
	}
}

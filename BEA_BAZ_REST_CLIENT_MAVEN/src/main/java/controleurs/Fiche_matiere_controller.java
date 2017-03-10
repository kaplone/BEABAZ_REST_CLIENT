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
import models.Matiere;

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

public class Fiche_matiere_controller extends Fiche_controller implements Initializable {

	@FXML
	private ObservableList<Matiere> liste_matieres;
	@FXML
	private ListView<Matiere> listView_matieres;
	@FXML
	private Button importerButton;
	@FXML
	private TextField file_path_textField;

	@FXML
	private TextField nom_matiere_textField;
	@FXML
	private TextField nom_complet_matiere_textField;
	@FXML
	private TextArea remarques_matiere_textArea;

	private File file;
    
	private Matiere matiereSelectionne;

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
			Documents.read(file, "matiere");
			file_path_textField.setVisible(false);
			importerButton.setVisible(false);
			rafraichirAffichage();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void onMatiereSelect() {
		super.onAnnulerEditButton();

		if (listView_matieres.getSelectionModel().getSelectedItem() == null) {
			editer.setVisible(false);
		} else {
			matiereSelectionne = listView_matieres.getSelectionModel().getSelectedItem();
			afficherMatiere();
			editer.setVisible(true);
		}
	}
	
	public void rafraichirAffichage() {

		liste_matieres = FXCollections.observableArrayList();
		JsonUtils.JsonToListObj(RestAccess.requestAll("matiere"), liste_matieres, new TypeReference<List<Matiere>>() {});

		listView_matieres.setItems(liste_matieres);
		afficherMatiere();
	}

	public void onNouvelle_matiereButton() {
		super.onNouveauButton();

		matiereSelectionne = new Matiere();
	}
	
	@FXML
	public void onEditerMatiereButton() {
		super.onEditerButton();
	}

	public void onAnnulerEditButton() {
		super.onAnnulerEditButton();
		onMatiereSelect();
	}

	@FXML
	public void onMiseAJourMatiereButton() {

		matiereSelectionne.setNom(nom_matiere_textField.getText());
		matiereSelectionne.setRemarques(remarques_matiere_textArea.getText());
		matiereSelectionne.setNom_complet(nom_complet_matiere_textField.getText());

		if (edit) {
			matiereSelectionne.update("matiere");
		} else {
			matiereSelectionne.save("matiere", Matiere.class);
			listView_matieres.getSelectionModel().select(matiereSelectionne);
			rafraichirAffichage();
		}
		
		super.onMiseAJourButton();
	}

	public void afficherMatiere() {
		editability(false);
		editer.setVisible(true);
		prompt(false);
        
		if (matiereSelectionne != null){
			nom_label.setText(matiereSelectionne.getNom());
			nom_matiere_textField.setText(matiereSelectionne.getNom());
			nom_complet_matiere_textField.setText(matiereSelectionne.getNom_complet());
			remarques_matiere_textArea.setText(matiereSelectionne.getRemarques());
		}		
	}
    
	@Override
	public void editability(boolean bool) {
		nom_matiere_textField.setEditable(bool);
		nom_complet_matiere_textField.setEditable(bool);
		remarques_matiere_textArea.setEditable(bool);
		prompt(bool);
	}
    
	@Override
	public void raz() {
		nom_matiere_textField.setText("");
		nom_complet_matiere_textField.setText("");
		remarques_matiere_textArea.setText("");
	}
    
	@Override
	public void prompt(boolean bool) {

		if (bool) {
			nom_matiere_textField.setPromptText("saisir le nom affiché de la nouvelle matiere");
			nom_complet_matiere_textField.setPromptText("saisir le nom complet de la nouvelle matiere");
			remarques_matiere_textArea.setPromptText("éventuelles remarques");
		} else {
			nom_matiere_textField.setPromptText(null);
			nom_complet_matiere_textField.setPromptText(null);
			remarques_matiere_textArea.setPromptText(null);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.init();
		
		versMatieresButton.setVisible(false);

		file_path_textField.setVisible(false);
		importerButton.setVisible(false);

		liste_matieres = FXCollections.observableArrayList();
		JsonUtils.JsonToListObj(RestAccess.requestAll("matiere"), liste_matieres, new TypeReference<List<Matiere>>() {
		});

		listView_matieres.setItems(liste_matieres);

		listView_matieres.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			onMatiereSelect();
		});
        
		if (! liste_matieres.isEmpty()){
			listView_matieres.getSelectionModel().select(0);
		}
	}
}

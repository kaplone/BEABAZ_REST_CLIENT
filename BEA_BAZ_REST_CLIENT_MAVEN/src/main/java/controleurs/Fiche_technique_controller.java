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
import models.Technique;

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

public class Fiche_technique_controller extends Fiche_controller implements Initializable{

	@FXML
	private ObservableList<Technique> liste_techniques;
	@FXML
	private ListView<Technique> listView_techniques;
	
	@FXML
	private Button lier_button;
	@FXML
	private Button importerButton;
	@FXML
	private TextField file_path_textField;

	@FXML
	private TextField nom_technique_textField;
	@FXML
	private TextField nom_complet_technique_textField;
	@FXML
	private TextArea remarques_technique_textArea;
	
	private File file;

	Technique techniqueSelectionne;
	
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
			e.printStackTrace();
		}
	}
	
	 public void onTechniqueSelect(){
		 super.onAnnulerEditButton();
	    	
		if (listView_techniques.getSelectionModel().getSelectedItem() == null) {
			editer.setVisible(false);
		} else {
			if (listView_techniques.getSelectionModel().getSelectedItem().getNom() != null) {
				techniqueSelectionne = listView_techniques.getSelectionModel().getSelectedItem();
				afficherTechnique();
				editer.setVisible(true);
			} else {
				editer.setVisible(false);
			}
		}
	}
    
	public void rafraichirAffichage() {

		liste_techniques = FXCollections.observableArrayList();
		JsonUtils.JsonToListObj(RestAccess.requestAll("technique"), liste_techniques, new TypeReference<List<Technique>>() {});

		listView_techniques.setItems(liste_techniques);
		afficherTechnique();
	}

    public void onNouvelle_techniqueButton() {
    	super.onNouveauButton();

    	techniqueSelectionne = new Technique();    	
    }
    
    @FXML
    public void onEditerTraitementButton(){
    	super.onEditerButton();
    }
    
    @FXML
    public void onAnnulerEditButton(){
    	super.onAnnulerEditButton();  
    	onTechniqueSelect();
    }
    
    @FXML
    public void onMiseAJourTechniqueButton(){
	
    	techniqueSelectionne.setNom(nom_technique_textField.getText());
    	techniqueSelectionne.setRemarques(remarques_technique_textArea.getText());
    	techniqueSelectionne.setNom_complet(nom_complet_technique_textField.getText());

		if (edit) {
			techniqueSelectionne.update("technique");
		}
		else {
			techniqueSelectionne.save("technique", Technique.class);
		   listView_techniques.getSelectionModel().select(techniqueSelectionne);
		   rafraichirAffichage();
		}   
		super.onMiseAJourButton();
    }
    
    public void afficherTechnique(){
    	editability(false);
		editer.setVisible(true);
		prompt(false);
		
		if (techniqueSelectionne != null){
			nom_label.setText(techniqueSelectionne.getNom());
			nom_technique_textField.setText(techniqueSelectionne.getNom());
			nom_complet_technique_textField.setText(techniqueSelectionne.getNom_complet());
			remarques_technique_textArea.setText(techniqueSelectionne.getRemarques());
		}		
    }
   
    @Override
    public void editability(boolean bool){
    	nom_technique_textField.setEditable(bool);
    	nom_complet_technique_textField.setEditable(bool);
    	remarques_technique_textArea.setEditable(bool);
		prompt(bool);
    }
    
    @Override
    public void raz(){
    	nom_technique_textField.setText("");
    	nom_complet_technique_textField.setText("");
    	remarques_technique_textArea.setText("");
    }
    
    @Override
    public void prompt(boolean bool){
    	
    	if (bool){
    		nom_technique_textField.setPromptText("saisir le nom affiché de la nouvelle technique");
        	nom_complet_technique_textField.setPromptText("saisir le nom complet de la nouvelle technique");
        	remarques_technique_textArea.setPromptText("éventuelles remarques");
    	}
    	else {
    		nom_technique_textField.setPromptText(null);
        	nom_complet_technique_textField.setPromptText(null);
        	remarques_technique_textArea.setPromptText(null);
    	}	
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.init();
        
		versTechniquesButton.setVisible(false);

		file_path_textField.setVisible(false);
		importerButton.setVisible(false);

		liste_techniques  = FXCollections.observableArrayList();
		JsonUtils.JsonToListObj(RestAccess.requestAll("technique"), liste_techniques, new TypeReference<List<Technique>>() {});
		
		listView_techniques.setItems(liste_techniques);
		
		listView_techniques.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			onTechniqueSelect();
		});
		
		if (! liste_techniques.isEmpty()){
			listView_techniques.getSelectionModel().select(0);
		}
		
	}
}

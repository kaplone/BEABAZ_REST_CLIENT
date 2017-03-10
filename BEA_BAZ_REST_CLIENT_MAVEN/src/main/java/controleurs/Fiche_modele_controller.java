package controleurs;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

import org.jongo.MongoCursor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import application.Documents;
import utils.JsonUtils;
import utils.RestAccess;
import models.Model;
import models.Technique;
import models.Auteur;
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

public class Fiche_modele_controller extends Fiche_controller implements Initializable{
	
	@FXML
	private ObservableList<Model> liste_models;
	@FXML
	private ListView<Model> listView_model;

	@FXML
	private TextField nom_model_textField;
	@FXML
	private TextField file_path_model_textField;
	@FXML
	private TextArea remarques_model_textArea;	
	
	@FXML
	private Button cheminVersModelButton;
	
	MongoCursor<Model> modelCursor;
	Model modelSelectionne;
	
	Model model;
	
	private File file;

	@FXML
	public void onModelSelect(){
		super.onAnnulerEditButton();
		
		if (listView_model.getSelectionModel().getSelectedItem() == null){
			modelSelectionne = null;
			editer.setVisible(false);	
		}
		else {
			if (listView_model.getSelectionModel().getSelectedItem().getNom() != null){
				modelSelectionne = listView_model.getSelectionModel().getSelectedItem();	
				afficherModel();
				editer.setVisible(true);
    		}
    		else {
    			editer.setVisible(false);
    		}	
		}
		afficherModel();
		
	}
	
	public void afficherModel(){
		editability(false);
		editer.setVisible(true);
		prompt(false);
    	
    	if (modelSelectionne != null){
    		nom_model_textField.setText(modelSelectionne.getNom());
        	remarques_model_textArea.setText(modelSelectionne.getRemarques());
        	file_path_model_textField.setText(modelSelectionne.getCheminVersModelSTR());
    	}	
    	else {
    		raz();
    	}
		
		
	}
	
    public void onNouveauModelButton() {
    	super.onNouveauButton();
    	
    	modelSelectionne = new Model();
    }
    
    @FXML
    public void onEditerModelButton(){
    	super.onEditerButton();
    }
    
    @FXML
    public void onAnnulerEditButton(){
    	super.onAnnulerEditButton();
    	onModelSelect();
    	
    }
    
    @FXML
    public void onMiseAJourModelButton(){
    	
    	modelSelectionne.setNom(nom_model_textField.getText());
    	modelSelectionne.setRemarques(remarques_model_textArea.getText());
    	modelSelectionne.setCheminVersModelSTR(file_path_model_textField.getText());
    	
    	annuler.setVisible(false);
    	editer.setVisible(true);
    	mise_a_jour.setVisible(false);
    	nom_model_textField.setEditable(false);
		remarques_model_textArea.setEditable(false);
		
		if (edit) {
			modelSelectionne.update("model");
		}
		else {
			modelSelectionne.save("model", Model.class);
		    listView_model.getSelectionModel().select(modelSelectionne);
			rafraichirAffichage();
		}
		
		super.onMiseAJourButton();
    }
    
    public void rafraichirAffichage(){
    	
    	liste_models = FXCollections.observableArrayList();
		
    	String jsonString = RestAccess.requestAll("model");
		ObjectMapper om = new ObjectMapper();

		try {
			List<Model> models = om.readValue(jsonString, new TypeReference<List<Model>>() {});
			liste_models.addAll(models);
		} catch (IOException e) {
			e.printStackTrace();
		}
		listView_model.setItems(liste_models);
    	
    }
    
    @Override
    public void editability(boolean bool){
    	nom_model_textField.setEditable(bool);
    	file_path_model_textField.setEditable(bool);
		remarques_model_textArea.setEditable(bool);
		prompt(bool);
    }
    
    @Override
    public void raz(){
    	nom_model_textField.setText("");
    	file_path_model_textField.setText("");
    	remarques_model_textArea.setText("");
    }
    
    @Override
    public void prompt(boolean bool){
    	
    	if (bool){
    		nom_model_textField.setPromptText("saisir le nom affiché du nouveau modèle");
    		file_path_model_textField.setPromptText("saisir le nom complet du nouveau modèle");
        	remarques_model_textArea.setPromptText("éventuelles remarques");
    	}
    	else {
    		nom_model_textField.setPromptText(null);
    		file_path_model_textField.setPromptText(null);
        	remarques_model_textArea.setPromptText(null);
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
		file_path_model_textField.setText(file.toString());
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
		super.init();

		versModelesButton.setVisible(false);
		cheminVersModelButton.setVisible(false);
		
		liste_models = FXCollections.observableArrayList();
		JsonUtils.JsonToListObj(RestAccess.requestAll("model"), liste_models, new TypeReference<List<Model>>() {});
		
		listView_model.setItems(liste_models);
		
		listView_model.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			onModelSelect();
		});
		
		if (! liste_models.isEmpty()){
			listView_model.getSelectionModel().select(0);
		}
	}
}

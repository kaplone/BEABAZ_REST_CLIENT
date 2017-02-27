package controleurs;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.jongo.MongoCursor;

import application.Documents;
import application.JfxUtils;
import models.Contexte;
import models.Fichier;
import models.Messages;
import models.Oeuvre;
import models.OeuvreTraitee;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Fiche_fichier_controller extends Fiche_controller implements Initializable{
	
	private ObservableList<Fichier> liste_fichiers;
	@FXML
	private ListView<Fichier> fichiers_listView;

	@FXML
	private TextField nom_fichier_textField;
	@FXML
	private TextField fichier_legende_textField;
	@FXML
	private TextArea remarques_fichier_textArea;

	private ImageView fichier_imageView;
	
	@FXML
	private Label chemin_fichier_label;
	
	MongoCursor<Fichier> fichierCursor;
	Fichier fichierSelectionne;
	
	Stage currentStage;

	Fichier fichier;
	
	private File file;
	
	private Oeuvre oeuvre;
	private OeuvreTraitee oeuvreTraitee;

	
	@FXML
	public void onFichierSelect(){
		
		fichierSelectionne = fichiers_listView.getSelectionModel().getSelectedItem();	
		affichageInfos();
		
	}
	
    private void affichageInfos(){

    	nom_label.setText(fichierSelectionne.getNom());
    	nom_fichier_textField.setText(fichierSelectionne.getNom());
    	remarques_fichier_textArea.setText(fichierSelectionne.getRemarques());
    	fichier_legende_textField.setText(fichierSelectionne.getLegende());
    	chemin_fichier_label.setText(fichierSelectionne.getFichierLie().toString());
    	fichier_imageView.setImage(new Image(String.format("file:%s" ,fichierSelectionne.getFichierLie().toString())));
    	
	
    }
    
    public void onNouveauFichierButton() {
    	
    	mise_a_jour.setText("Enregistrer");
    	nom_fichier_textField.setText("");
    	remarques_fichier_textArea.setText("");
    	nom_fichier_textField.setPromptText("saisir le nom du nouvel model");
    	remarques_fichier_textArea.setPromptText("éventuelles remarques");
    	nouveau.setVisible(false);
    	
    	fichierSelectionne = new Fichier();
    	
    	edit = false;
    	annuler.setVisible(true);
    	editer.setVisible(false);
    	mise_a_jour.setVisible(true);
    	nom_fichier_textField.setEditable(true);
		remarques_fichier_textArea.setEditable(true);
    }
    
    public void onAnnulerButton() {
    	
    	mise_a_jour.setText("Mise à jour");
    	nom_fichier_textField.setText("");
    	remarques_fichier_textArea.setText("");
    	nom_fichier_textField.setPromptText("");
    	remarques_fichier_textArea.setPromptText("");
    	nouveau.setText("Nouvel model");
    	rafraichirAffichage();
    	fichiers_listView.getSelectionModel().select(fichierSelectionne);
    	affichageInfos();
    }
    
    public void rafraichirAffichage(){
	
		liste_fichiers = FXCollections.observableArrayList();
		oeuvreTraitee = Messages.getOeuvreTraiteeObj();
		oeuvre = oeuvreTraitee.getOeuvre();

		liste_fichiers.addAll(oeuvreTraitee.getFichiers().stream()
				                                         .map(a -> Fichier.retrouveFichier(new ObjectId(a.get("oeuvresTraitee_id"))))
				                                         .collect(Collectors.toList()));

	
		fichiers_listView.setItems(liste_fichiers);
    	
    }
    
    @FXML
    public void onEditerFichierButton(){
    	

    	annuler.setVisible(true);
    	editer.setVisible(false);
    	mise_a_jour.setVisible(true);
    	nom_fichier_textField.setEditable(true);
		remarques_fichier_textArea.setEditable(true);
		
		edit = true;

	
    }
    
    @FXML
    public void onAnnulerEditButton(){
    	
    	annuler.setVisible(false);
    	editer.setVisible(true);
    	mise_a_jour.setVisible(false);
    	nom_fichier_textField.setEditable(false);
		remarques_fichier_textArea.setEditable(false);
		nouveau.setVisible(true);
		rafraichirAffichage();
		fichiers_listView.getSelectionModel().select(fichierSelectionne);
    	affichageInfos();
    	
    	edit = false;
    	
    }
    
    @FXML
    public void onMiseAJourFichierButton(){
    	
    	if (fichierSelectionne == null){
    		fichierSelectionne = new Fichier();
    	}
    	
    	fichierSelectionne.setNom(nom_fichier_textField.getText());
    	fichierSelectionne.setRemarques(remarques_fichier_textArea.getText());
    	fichierSelectionne.setLegende(fichier_legende_textField.getText());

    	annuler.setVisible(false);
    	editer.setVisible(true);
    	mise_a_jour.setVisible(false);
    	nom_fichier_textField.setEditable(false);
		remarques_fichier_textArea.setEditable(false);
		
		if (edit) {
			Fichier.update(fichierSelectionne);
			rafraichirAffichage();
			onAnnulerEditButton();
		}
		else {
			
		   Fichier.save(fichierSelectionne);
		   onAnnulerEditButton();
		}
    	
    }
    
protected File chooseExport(){
		
		Stage newStage = new Stage();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Fichier à importer");
		fileChooser.getExtensionFilters().addAll(
		         new FileChooser.ExtensionFilter("image jpg", "*.jpg"));
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
		fichier_legende_textField.setText(file.toString());
		nom_fichier_textField.setText(file.getName().split("\\.")[0]);
	}
	@FXML
	public void on_import_file_button(){
		try {
			Documents.read(file, "produit");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		if (fichier != null){
			nom_fichier_textField.setEditable(false);
			remarques_fichier_textArea.setEditable(false);
	        editer.setVisible(true);
	        mise_a_jour.setVisible(false);
			annuler.setVisible(false);
		}
		else {
			nom_fichier_textField.setEditable(true);
			remarques_fichier_textArea.setEditable(true);
	        editer.setVisible(false);
	        mise_a_jour.setVisible(true);
	        mise_a_jour.setText("Enregistrer");
			annuler.setVisible(false);
		}
		
		versClientButton.setVisible(true);
		
		if(Messages.getCommande() != null){
			versCommandeButton.setVisible(true);
		}
		else {
			versCommandeButton.setVisible(false);
		}
		
		if (Messages.getOeuvreTraitee() != null){
			versOeuvreButton.setVisible(true);
		}
		else {
			versOeuvreButton.setVisible(false);
		}
		
		
		versRapportButton.setVisible(false);
		versAuteursButton.setVisible(true);
		
		versModelsButton.setVisible(false);

		currentStage = Messages.getStage();
		
		liste_fichiers = FXCollections.observableArrayList();
		oeuvreTraitee = Messages.getOeuvreTraiteeObj();
			
		liste_fichiers.addAll(oeuvreTraitee.getFichiers().stream()
                										 .map(a -> Fichier.retrouveFichier(new ObjectId(a.get("oeuvresTraitee_id"))))
                										 .collect(Collectors.toList()));

		fichiers_listView.setItems(liste_fichiers);
		
		int index = 0;
		
		for (Fichier f : liste_fichiers){
			if (f.getNom().equals(fichier.getNom())){
				break;
			}
			else {
				index ++;
			}
		}
		
		fichiers_listView.getSelectionModel().select(index);
		fichierSelectionne = fichier;
		affichageInfos();
	}

}

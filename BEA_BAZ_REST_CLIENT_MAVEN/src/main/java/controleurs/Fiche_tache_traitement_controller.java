package controleurs;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.jongo.MongoCursor;

import application.JfxUtils;
import enums.Progression;
import fr.opensagres.xdocreport.template.velocity.internal.Foreach;
import utils.RestAccess;
import models.Commande;
import models.Messages;
import models.OeuvreTraitee;
import models.Produit;
import models.TacheTraitement;
import models.Traitement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Fiche_tache_traitement_controller extends Fiche_controller implements Initializable{

	@FXML
	private TableView<TacheTraitement> traitements_associes_tableView;
	@FXML
	private TableColumn<TacheTraitement, String> traitements_associes_tableColumn;
	@FXML
	private TableColumn<TacheTraitement, ImageView> traitements_associes_faits_tableColumn;
	@FXML
	private TextField file_path_textField;

	@FXML
	private Label ot_label;
	@FXML
	private Label t_label;
	@FXML
	private TextArea remarques_traitement_textArea;
	
	@FXML
	private TextField complement_textField;
	
	@FXML
	private RadioButton fait_radio;
	@FXML
	private RadioButton todo_radio;
	@FXML
	private RadioButton so_radio;
	
	@FXML
	private ImageView coche_fait;
	@FXML
	private ImageView coche_todo;
	@FXML
	private ImageView coche_so;

	@FXML
	private Label complementTraitementLabel;
	
	@FXML
	private Button add_pr;
	@FXML
	private Button remove_pr;
	
	@FXML
	private ListView<String> produits_listView;
	@FXML
	private ListView<String> produits_pre_listview;
	@FXML
	private ListView<String> produits_reste_listView;
	
	private ObservableList<String> liste_produits;
	
	private ObservableList<String> liste_traitements;

	private MongoCursor<TacheTraitement> traitementCursor;
	private MongoCursor<Traitement> tousLesTraitementsCursor;
	private Map<String, ObjectId> tousLesTraitements_id;
	
	
	private TacheTraitement tacheTraitementSelectionne;

	private ObservableList<TacheTraitement> observable_liste_tachestraitements_lies;

	private Progression progres;

	private Traitement traitementSource;
	private OeuvreTraitee ot;

	
	private int selectedIndex;
	
	@FXML
	public void onTacheSelect(){
		super.onAnnulerEditButton();
    	
		if (traitements_associes_tableView.getSelectionModel().getSelectedItem() == null){
			tacheTraitementSelectionne = null;
			editer.setVisible(false);	
		}
		else {
			if (traitements_associes_tableView.getSelectionModel().getSelectedItem().getNom() != null){
				tacheTraitementSelectionne = traitements_associes_tableView.getSelectionModel().getSelectedItem();	
				afficherTache();
				editer.setVisible(true);
    		}
    		else {
    			editer.setVisible(false);
    		}	
		}
		
	}
	
    public void affichageProduitsUtilises(){
		
	}
    
    public void affichageProduitsUtilises(String p){
    	affichageProduitsUtilises();
    }
    
    public void onAnnulerButton() {
    	
    	mise_a_jour.setText("Mise à jour");
    	//nom_traitement_textField.setText("");
    	remarques_traitement_textArea.setText("");
    	//nom_traitement_textField.setPromptText("");
    	remarques_traitement_textArea.setPromptText("");
    	rafraichirAffichage();
    	traitements_associes_tableView.getSelectionModel().select(tacheTraitementSelectionne);
    	afficherTraitementsAssocies();
    	
    }
    
    public void rafraichirAffichage(){
    	
    	nom_label.setText(tacheTraitementSelectionne.getNom());
    	complement_textField.setText(tacheTraitementSelectionne.getComplement());
    	remarques_traitement_textArea.setText(tacheTraitementSelectionne.getRemarques());
    	
    	editer.setVisible(true);
        mise_a_jour.setVisible(false);
		annuler.setVisible(false);
    	
    }
    
    @FXML
    public void onEditerTraitementButton(){
    	super.onEditerButton();
    }
    
    @FXML
    public void onAnnulerEditButton(){
    	super.onAnnulerEditButton();  
    }
    
    @FXML
    public void onMiseAJourTraitementButton(){
    	super.onMiseAJourButton();

    	tacheTraitementSelectionne.setComplement(complement_textField.getText());
    	tacheTraitementSelectionne.setRemarques(remarques_traitement_textArea.getText());
		
		if (edit) {
			TacheTraitement.update(tacheTraitementSelectionne);
		}
		else {
			
		   TacheTraitement.save(tacheTraitementSelectionne);
		   afficherTraitementsAssocies();
		}
    	
    }
    
    @FXML
    public void onAjoutProduit(){
    	
    	//Messages.setTacheTraitementEdited(listView_traitements.getSelectionModel().getSelectedItem());
    	
    	Scene fiche_produit_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_produit.fxml"), 1275, 722);
		fiche_produit_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_produit_scene);
    }

    public void onFait_radio(){
    	
    	tacheTraitementSelectionne.setFait_(Progression.FAIT_);
    	//MongoAccess.update("tacheTraitement", tacheTraitementSelectionne);
    	afficherTraitementsAssocies();
    	checkIfCompleted();
    }
    public void onTodo_radio(){
    	
    	tacheTraitementSelectionne.setFait_(Progression.TODO_);
    	//MongoAccess.update("tacheTraitement", tacheTraitementSelectionne);
    	afficherTraitementsAssocies();
    	checkIfCompleted();
    }
    public void onSo_radio(){
    	
    	tacheTraitementSelectionne.setFait_(Progression.NULL_);
    	//MongoAccess.update("tacheTraitement", tacheTraitementSelectionne);
    	afficherTraitementsAssocies();
    	checkIfCompleted();
    }
    
    public void checkIfCompleted(){

    	progres = Progression.FAIT_;
    	
    	for (TacheTraitement ttt : traitements_associes_tableView.getItems()){

    		if (ttt.getFait_().equals(Progression.TODO_)){
    			progres = Progression.TODO_;
    			break;
    		}
    		
    	}
    	ot.setProgressionOeuvreTraitee(progres);
    	//MongoAccess.update("oeuvreTraitee", ot);
    }

    public void afficherTraitementsAssocies(){
    	
    	observable_liste_tachestraitements_lies.setAll(Messages.getOeuvreTraiteeObj().getTraitementsAttendus_obj());
    	
		traitements_associes_tableColumn.setCellValueFactory(new PropertyValueFactory<TacheTraitement, String>("nom"));
		traitements_associes_faits_tableColumn.setCellValueFactory(new PropertyValueFactory<TacheTraitement, ImageView>("icone_progression"));
		
		traitements_associes_tableView.setItems(observable_liste_tachestraitements_lies);

		traitements_associes_tableView.getSelectionModel().clearAndSelect(selectedIndex);

		afficherProgression();
    	afficherProduits();
    }
    
    public void onTraitementAssocieSelect(){
    	
    	tacheTraitementSelectionne = traitements_associes_tableView.getSelectionModel().getSelectedItem();
    	selectedIndex = traitements_associes_tableView.getSelectionModel().getSelectedIndex();
    	
    	Messages.setTacheTraitement(tacheTraitementSelectionne);
    	
    	afficherTraitementsAssocies();	
    }
    
    public void afficherTache(){
    	
    	remarques_traitement_textArea.setEditable(false);
        editer.setVisible(true);
        mise_a_jour.setVisible(false);
		annuler.setVisible(false);
		//nom_traitement_textField.setDisable(true);
		remarques_traitement_textArea.setDisable(true);
		nom_label.setText(tacheTraitementSelectionne.getNom());

		t_label.setText(tacheTraitementSelectionne.getTraitement().getNom_complet());

		for (String p : tacheTraitementSelectionne.getProduitsLies_names()){
			affichageProduitsUtilises(p);
		}
		
		rafraichirAffichage();
    }
    
    public void afficherProgression(){
    	

		progres = tacheTraitementSelectionne.getFait_();
    	
        switch (progres){
		
		case TODO_ : todo_radio.setSelected(true);
		             break;
		case NULL_ : so_radio.setSelected(true);
                     break;
		case FAIT_ : fait_radio.setSelected(true);
                     break;
		}
		
    }
    
    public void afficherProduits(){
    	
    	liste_produits.clear();
    	liste_produits.addAll(tacheTraitementSelectionne.getTraitement().getProduits().keySet());
    	produits_listView.setItems(liste_produits);
    }
    
    @Override
    public void editability(boolean bool){
    	complement_textField.setEditable(bool);
    	remarques_traitement_textArea.setEditable(bool);
		prompt(bool);
    }
    
    @Override
    public void raz(){
    	complement_textField.setText("");
    	remarques_traitement_textArea.setText("");
    }
    
    @Override
    public void prompt(boolean bool){
    	
    	if (bool){
    		complement_textField.setPromptText("Complément éventuel du traitement");
    		remarques_traitement_textArea.setPromptText("éventuelles remarques");
    	}
    	else {
    		complement_textField.setPromptText(null);
    		remarques_traitement_textArea.setPromptText(null);
    	}	
    }


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.init();
		versOeuvreButton.setVisible(true);
		
		coche_fait.setImage(new Image(Progression.FAIT_.getUsedImage()));
		coche_todo.setImage(new Image(Progression.TODO_.getUsedImage()));
		coche_so.setImage(new Image(Progression.NULL_.getUsedImage()));
		
		tacheTraitementSelectionne = Messages.getTacheTraitement();
		
		traitements_associes_tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			onTacheSelect();
		});
		
		ot = Messages.getOeuvreTraiteeObj();
		
//		if (! liste_auteurs.isEmpty()){
//			listView_auteur.getSelectionModel().select(0);
//		}
		
		traitementSource = tacheTraitementSelectionne.getTraitement();
		
		ot_label.setText(ot.getNom());
		t_label.setText(traitementSource.getNom());
		
		liste_traitements = FXCollections.observableArrayList();
		liste_produits  = FXCollections.observableArrayList();
		observable_liste_tachestraitements_lies = FXCollections.observableArrayList();

		afficherTraitementsAssocies();
		rafraichirAffichage();

	}
}

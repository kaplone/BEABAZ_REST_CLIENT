package controleurs;

import java.net.URL;
import java.util.ResourceBundle;

import application.JfxUtils;
import enums.Progression;

import models.Messages;
import models.OeuvreTraitee;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

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
	private ListView<String> produits_pre_listView;
	@FXML
	private ListView<String> produits_reste_listView;
	
	private ObservableList<String> liste_produits;
	private ObservableList<String> liste_pre_produits;
	private ObservableList<String> liste_reste_produits;	
	
	private TacheTraitement tacheTraitementSelectionne;

	private ObservableList<TacheTraitement> observable_liste_tachestraitements_lies;

	private Progression progres;

	private Traitement traitementSource;
	private OeuvreTraitee ot;
	
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
		rafraichirAffichage();
		
	}
    
    public void rafraichirAffichage(){
		afficherTache();
		afficherProduits();    	
		afficherProgression();
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

    	tacheTraitementSelectionne.setComplement(complement_textField.getText());
    	tacheTraitementSelectionne.setRemarques(remarques_traitement_textArea.getText());
		
		if (edit) {
			tacheTraitementSelectionne.update("tacheTraitement");
		}
		else {
			
			tacheTraitementSelectionne.save("tacheTraitement", TacheTraitement.class);
		   afficherTraitementsAssocies();
		}
		
		super.onMiseAJourButton();
    	
    }
    
    @FXML
    public void onAjoutProduit(){
    	
    	Scene fiche_produit_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_produit.fxml"), 1275, 722);
		fiche_produit_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_produit_scene);
    }

    public void onFait_radio(){
    	
    	tacheTraitementSelectionne.setFait_(Progression.FAIT_);
    	afficherTraitementsAssocies();
    	checkIfCompleted();
    }
    public void onTodo_radio(){
    	
    	tacheTraitementSelectionne.setFait_(Progression.TODO_);
    	afficherTraitementsAssocies();
    	checkIfCompleted();
    }
    public void onSo_radio(){
    	
    	tacheTraitementSelectionne.setFait_(Progression.NULL_);
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
    }

    public void afficherTraitementsAssocies(){
    	
    	observable_liste_tachestraitements_lies.setAll(Messages.getOeuvreTraiteeObj().accesseurTraitementsAttendus_obj());
    	
		traitements_associes_tableColumn.setCellValueFactory(new PropertyValueFactory<TacheTraitement, String>("nom"));
		traitements_associes_faits_tableColumn.setCellValueFactory(new PropertyValueFactory<TacheTraitement, ImageView>("icone_progression"));
		
		traitements_associes_tableView.setItems(observable_liste_tachestraitements_lies);
    }
    
    public void afficherTache(){
    	editability(false);
    	
    	System.out.println(remarques_traitement_textArea);
    	System.out.println(tacheTraitementSelectionne);
    	
    	remarques_traitement_textArea.setText(tacheTraitementSelectionne.getRemarques());
		complement_textField.setText(tacheTraitementSelectionne.getComplement());
		nom_label.setText(tacheTraitementSelectionne.getNom());		
		
		if (tacheTraitementSelectionne.getTraitement() != null){
			t_label.setText(tacheTraitementSelectionne.getTraitement().getNom_complet());
		}
		else {
			t_label.setText(tacheTraitementSelectionne.getNom_complet());
		}
		
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
    	
    	if (tacheTraitementSelectionne.getTraitement() != null && tacheTraitementSelectionne.getTraitement().getProduits() != null){
    		
    		liste_produits.setAll(tacheTraitementSelectionne.getTraitement().getProduits().keySet());
        	produits_listView.setItems(liste_produits);
        	
        	liste_pre_produits.setAll(tacheTraitementSelectionne.getTraitement().getListe_produits_names());
        	produits_pre_listView.setItems(liste_pre_produits);

    	}

    	liste_reste_produits = Messages.getTous_les_nom_de_produits();
    	produits_reste_listView.setItems(liste_reste_produits);
    	
    	rafraichirProduits();    	
    }
    
    public void rafraichirProduits(){
    	
    	produits_pre_listView.setCellFactory(cell -> {

			return new ListCell<String>() {

				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (item == null || empty) {
					}

					else {

						setText(item);

						if (liste_produits.contains(item)) {
							setDisable(true);
							setOpacity(0.5);
						}
					}
				}
			};
		});
    	
    	
    	liste_reste_produits.removeAll(liste_pre_produits);
    	produits_reste_listView.setCellFactory(cell -> {

			return new ListCell<String>() {

				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (item == null || empty) {
					}

					else {

						setText(item);

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
			liste_produits.add(m);
			produits_pre_listView.getSelectionModel().select(null);
			produits_pre_listView.getSelectionModel().clearSelection();
			produits_reste_listView.getSelectionModel().select(null);
			produits_reste_listView.getSelectionModel().clearSelection();
            rafraichirProduits();
			onEditerButton();
		}
	}

	public void retirer_produit(String m) {
		if (m != null) {
			liste_produits.remove(m);
			rafraichirProduits();
			onEditerButton();
		}
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
		
		tacheTraitementSelectionne = Messages.getTraitementSelectionne();
		
		traitements_associes_tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			onTacheSelect();
		});
		
		ot = Messages.getOeuvreTraiteeObj();

		traitementSource = tacheTraitementSelectionne.getTraitement();
		
		System.out.println("traitementSource : " + traitementSource);
		
		ot_label.setText(ot.getNom());
		if (traitementSource != null){
			t_label.setText(traitementSource.getNom());
		}
		else {
			t_label.setText(tacheTraitementSelectionne.getNom());
		}
		

		liste_produits  = FXCollections.observableArrayList();
		liste_pre_produits  = FXCollections.observableArrayList();
		liste_reste_produits  = FXCollections.observableArrayList();
		observable_liste_tachestraitements_lies = FXCollections.observableArrayList();
		
		produits_listView.onDragDetectedProperty().set(dd -> {

			Dragboard dragBoard = produits_listView.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			content.putString(produits_listView.getSelectionModel().getSelectedItem());
			dragBoard.setContent(content);

			produits_pre_listView.setOnDragOver(dd_ -> dd_.acceptTransferModes(TransferMode.MOVE));

			produits_pre_listView.setOnDragDropped(dd_ -> {
				retirer_produit(dd_.getDragboard().getString());
				dd_.setDropCompleted(true);
			});
			
			produits_reste_listView.setOnDragOver(dd_ -> dd_.acceptTransferModes(TransferMode.MOVE));

			produits_reste_listView.setOnDragDropped(dd_ -> {
				retirer_produit(dd_.getDragboard().getString());
				dd_.setDropCompleted(true);
			});

		});

		produits_pre_listView.setOnDragDropped(dd_ -> dd_.setDropCompleted(true));

		produits_pre_listView.onDragDetectedProperty().set(dd -> {

			Dragboard dragBoard = produits_pre_listView.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			content.putString(produits_pre_listView.getSelectionModel().getSelectedItem());
			dragBoard.setContent(content);

			produits_listView.setOnDragOver(dd_ -> dd_.acceptTransferModes(TransferMode.MOVE));

			produits_listView.setOnDragDropped(dd_ -> {
				ajouter_produit(dd_.getDragboard().getString());
				dd_.setDropCompleted(true);
			});
		});
		
		produits_reste_listView.setOnDragDropped(dd_ -> dd_.setDropCompleted(true));

		produits_reste_listView.onDragDetectedProperty().set(dd -> {

			Dragboard dragBoard = produits_reste_listView.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			content.putString(produits_reste_listView.getSelectionModel().getSelectedItem());
			dragBoard.setContent(content);

			produits_listView.setOnDragOver(dd_ -> dd_.acceptTransferModes(TransferMode.MOVE));

			produits_listView.setOnDragDropped(dd_ -> {
				ajouter_produit(dd_.getDragboard().getString());
				dd_.setDropCompleted(true);
			});
		});

		afficherTraitementsAssocies();
		rafraichirAffichage();
		
		traitements_associes_tableView.getSelectionModel().select(tacheTraitementSelectionne);

	}
}

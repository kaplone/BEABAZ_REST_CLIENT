package controleurs;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.ComparatorUtils;
import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.type.TypeReference;

import application.JfxUtils;
import enums.Progression;
import models.Auteur;
import models.Client;
import models.Commande;
import models.Contexte;
import models.Messages;
import models.Model;
import models.OeuvreTraitee;
import models.Produit;
import models.Traitement;
import utils.JsonUtils;
import utils.RestAccess;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class Fiche_commande_controller extends Fiche_controller implements Initializable{

	@FXML
	private TextField nomCommandeTextField;

	@FXML
	private TextArea remarques;
	@FXML
	private Button nouvelle_oeuvre;
	@FXML
	private Button importCommandeButton;
	@FXML
	private DatePicker dateCommandePicker;
	@FXML
	private DatePicker dateDebutProjetPicker;
	@FXML
	private DatePicker dateFinProjetPicker;
	
	@FXML
	private RadioButton fait_radioButton;
	
	@FXML
	private ChoiceBox<String> modelChoiceBox;
	
	@FXML
	private VBox commandeExportVbox;
	
	@FXML
	private Button add_t;
	@FXML
	private Button remove_t;
	
	@FXML
	private ListView<String> listView_traitements;
	@FXML
	private ListView<String> listView_all_traitements;
	
	private ObservableList<Traitement> liste_traitements_obj;
	private ObservableList<String> liste_traitements;
	private ObservableList<String> liste_all_traitements;
	private Set<String> traitementsUtilises;
	private Map<String, Traitement> map_traitements;
 
	private static ObservableList<String> observableModeles;	
	private ObservableList<Map<String,String>> obs_oeuvres;
	
	private Commande commande;
	
	private Client client;
	private Model modele;
    private Auteur auteur;

	private String modele_name;
    private String auteur_name;

    private static List<Model> models;
    private static List<Auteur> auteurs;
    
    @FXML
    public void onImporterButton(){
    	Scene fiche_commande_import_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_commande_import.fxml"), Contexte.largeurFenetre, Contexte.hauteurFenetre);
		fiche_commande_import_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_commande_import_scene);
    }
	
	@FXML
	public void onEditerButton(){
		super.onEditerButton();
		editability(true);
		
		afficherModeles();
		afficherAuteurs();
		importCommandeButton.setDisable(true); 
		//commandeExportVbox.setVisible(false); // c'est quoi ???
		versRapportButton.setVisible(false);
		nomCommandeTextField.setDisable(false);		
	}
	
	@FXML
	public void onAnnulerButton(){
		boolean edit_save = edit;
		super.onAnnulerEditButton();
		nouveau.setVisible(false);
		editability(false);
		
		importCommandeButton.setDisable(false);
		nomCommandeTextField.setDisable(true);

		if (edit_save) {

			afficherCommande();
			afficherModeles();
			afficherAuteurs();
			afficherOeuvres();
			
		}
		else {
			onVersClientButton();
		}
	}
	
	@FXML
	public void onMiseAJourButton(){		
		nouveau.setVisible(false);

		if (Messages.getCommande() == null){
			commande = new Commande();
		}		

		commande.setLocalDateCommande(dateCommandePicker.getValue());
		commande.setLocalDateDebutProjet(dateDebutProjetPicker.getValue());
		commande.setLocalDateFinProjet(dateFinProjetPicker.getValue());
		commande.setRemarques(remarques.getText());
		commande.setNom(nomCommandeTextField.getText());
		modele_name = modelChoiceBox.getSelectionModel().getSelectedItem();		
		auteur_name = auteursChoiceBox.getSelectionModel().getSelectedItem();
		
		commande.setModele_map(modele_name);
		commande.setAuteur_map(auteur_name);
		
		commande.setListe_traitements(liste_traitements);
		commande.setMap_traitements(map_traitements);
	
		if (edit) {
			commande.update("commande");
		}
		else {
		   commande = commande.save("commande", Commande.class);		   
		   client.getCommandes_id().put(commande.getNom(), commande.get_id());
		   client.update("client");
		}

		Messages.setCommande(commande);
		
		super.onMiseAJourButton();
	
		afficherTraitements();
		afficherCommande();
	    afficherModeles();
	    afficherAuteurs();
	    afficherOeuvres();
	    

	}
	
	public void afficherCommande(){
		
		editability(false);
		
		importCommandeButton.setDisable(false);
		
		nom_label.setText(commande.getNom());

//		fiche_commande_label.setText("FICHE COMMANDE :");
//		nomClientLabel.setText(client.getNom());
//		nomCommandeTextField.setDisable(true);
//		nomClientLabel.setText(client.getNom());
		
		loadCommande(commande);
	}
	
    
	@FXML
    public void onExporterToutButton(){
		
//		OeuvreTraitee[] oeuvresAExporter = OeuvreTraitee.retrouveOeuvreTraiteesCommande(commande, fait_radioButton.isSelected());
//		
//		for ( OeuvreTraitee ot : oeuvresAExporter){
//			
//			System.out.println("export de la fiche : " + ot.getCote() + " en cours ...");
//			
//			Oeuvre o = ot.getOeuvre();
//			FreeMarkerMaker.odt2pdf(o, ot);
//		}
		
		 
	}
	
	@FXML
    public void onVersRapportButton(){}
	
	public void loadCommande(Commande c){
		
		if (c != null){
		
			dateCommandePicker.setValue(c.getDateCommande_LocalDate());
			dateDebutProjetPicker.setValue(c.getDateDebutProjet_LocalDate());;
			dateFinProjetPicker.setValue(c.getDateFinProjet_LocalDate());
			remarques.setText(c.getRemarques());
			nom_label.setText(c.getNom());
			nomCommandeTextField.setText(c.getNom());	
		}
	}
	
	public void afficherTraitements() {

		//liste_all_traitements.setAll(Traitement.retrouveTraitementsStr());
		//liste_traitements.setAll(traitementsUtilises);
		//listView_traitements.setItems(liste_traitements);

		miseAJourAffichagetraitements();
	}

	public void miseAJourAffichagetraitements() {

		listView_all_traitements.setCellFactory(cell -> {

			return new ListCell<String>() {

				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (item == null || empty) {
					}

					else {
						setText(item);
						
						if (liste_traitements.stream()
								             .map(a -> {
								            	 if (a.split(": ").length >1){
								            		 return a.split(": ")[1];
								            	 }
								            	 else {
								            		 return a;
								            	 }
								            	 
								             })
								             .anyMatch(b -> b.equals(item))){
							setDisable(true);
							setOpacity(0.5);
						}
					}
				}
			};
		});
	}

	public void ajouter_traitement(String m) {
		if (m != null) {
			
			int index = liste_traitements.size();
			
			liste_traitements.add(String.format("%02d: %s",++index,  map_traitements.get(m).getNom()));
			listView_all_traitements.getSelectionModel().select(null);
			listView_all_traitements.getSelectionModel().clearSelection();
			miseAJourAffichagetraitements();
			onEditerButton();
		}
	}

	public void retirer_traitement(String m) {
		
		if (m != null) {
			ObservableList<String> temp_liste_traitements = FXCollections.observableArrayList();
			
			liste_traitements.remove(m);
			
			if (m.split(": ").length > 1){
				int head = Integer.parseInt(m.split(": ")[0]);
				liste_traitements.forEach(a -> {
					String temp_str = a.split(": ")[0];
					int temp = Integer.parseInt(temp_str);
					if (temp > head){
						temp_liste_traitements.add(String.format("%02d: %s", temp - 1, a.split(": ")[1]));
					}
					else {
						temp_liste_traitements.add(a);
					}	
				});
				liste_traitements.setAll(temp_liste_traitements);
				liste_traitements.sort((a, b) -> a.compareToIgnoreCase(b));
			}
				
			miseAJourAffichagetraitements();
			onEditerButton();
		}
	}
    
    public Comparator<? super Map<String, Object>> otTrieeParNom = 
    		(Map<String, Object> o1, Map<String, Object> o2)-> o1.get("oeuvresTraitee_string").toString().compareTo(o2.get("oeuvresTraitee_string").toString());
    
    public void afficherOeuvres(){
        
    	if(obs_oeuvres == null){
    		try {
    			obs_oeuvres = FXCollections.observableArrayList(commande.getOeuvresTraitees());
    		}
    		catch (NullPointerException npe){
    			
    		}
    		
    	}
    	 
    	oeuvres_nom_colonne.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("oeuvresTraitee_string").toString()));
		oeuvres_fait_colonne.setCellValueFactory(data -> new SimpleObjectProperty<ImageView>(getImageView(data)));
		tableOeuvre.setItems(obs_oeuvres);
	}
    
    public ImageView getImageView(CellDataFeatures<Map<String, String>, ImageView> data){
    	
    	ImageView imv = new ImageView();
    	imv.setFitWidth(20);
    	imv.setFitHeight(20);
    	imv.setImage(new Image(Progression.valueOf(data.getValue().get("oeuvresTraitee_etat").toString()).getUsedImage()));
    	
    	return imv; 	
    }

    public void afficherAuteurs(){
	    if (auteurs == null){
	    	observableAuteurs = FXCollections.observableArrayList();
	    	auteurs = Arrays.asList(Auteur.retrouveAuteurs());
	    	observableAuteurs.add(null);
	    	for (Auteur auteur : auteurs){
				observableAuteurs.add(auteur.getNom());
			}
	    }
	    
		commande.addAuteurs(auteurs);
	    
	    auteursChoiceBox.setItems(observableAuteurs);

		if (commande != null && commande.getAuteur() != null){
			auteursChoiceBox.getSelectionModel().select(commande.getAuteur().get("auteur_string"));
		}	
	}
    
    public void afficherModeles(){
      	if (models == null){
      		observableModeles = FXCollections.observableArrayList();
      		models = Arrays.asList(Model.retrouveModels());
      		observableModeles.add(null);
            for (Model model : models){
    			observableModeles.add(model.getNom());
    		}            
      	}
        
      	commande.addModels(models);
      	
      	modelChoiceBox.setItems(observableModeles);

    	if (commande != null && commande.getModele() != null){      			
			modelChoiceBox.getSelectionModel().select(commande.getModele().get("modele_string"));
    	}	
    }
    
    public void onOeuvreSelect(){
    	
    	OeuvreTraitee ot_ = OeuvreTraitee.retrouveOeuvreTraitee(new ObjectId(tableOeuvre.getSelectionModel().getSelectedItem().get("oeuvresTraitee_id")));
    	Messages.setOeuvreTraiteeObj(ot_);

    	Scene fiche_oeuvre_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_oeuvre_v2.fxml"), Contexte.largeurFenetre, Contexte.hauteurFenetre);
		fiche_oeuvre_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		currentStage.setScene(fiche_oeuvre_scene);
    	
    }
    
    public void editability(boolean bool){
    	importCommandeButton.setDisable(bool);
    	
		dateCommandePicker.setDisable(!bool);
		dateDebutProjetPicker.setDisable(!bool);
		dateFinProjetPicker.setDisable(!bool);

		remarques.setEditable(bool);
		auteursChoiceBox.setDisable(!bool);
		modelChoiceBox.setDisable(!bool);
		prompt(bool);
    }
    
    public void raz(){
//    	nom_auteur_textField.setText("");
//    	nom_complet_auteur_textField.setText("");
//    	remarques_auteur_textArea.setText("");
    }
    
    public void prompt(boolean bool){
    	
    	if (bool){
//    		nom_auteur_textField.setPromptText("saisir le nom affiché du nouvel auteur");
//        	nom_complet_auteur_textField.setPromptText("saisir le nom complet du nouvel auteur");
//        	remarques_auteur_textArea.setPromptText("éventuelles remarques");
    	}
    	else {
//    		nom_auteur_textField.setPromptText(null);
//        	nom_complet_auteur_textField.setPromptText(null);
//        	remarques_auteur_textArea.setPromptText(null);
    	}	
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.init();
		
		Contexte.setFiche_commande_controller(this);
		
		versCommandeButton.setVisible(false);
		
		client = Messages.getClient(); 

		traitementsUtilises = new HashSet<>();
		liste_traitements = FXCollections.observableArrayList();
		listView_traitements.setItems(liste_traitements);	
		
		if (liste_traitements_obj == null){
			liste_traitements_obj = FXCollections.observableArrayList();
			
			JsonUtils.JsonToListObj(RestAccess.requestAll("traitement"), liste_traitements_obj, new TypeReference<List<Traitement>>() {});
			
			map_traitements = new HashMap<>();
			liste_all_traitements = FXCollections.observableArrayList();
			
			liste_traitements_obj.forEach(a -> {
				map_traitements.put(a.getNom(), a);
				liste_all_traitements.add(a.getNom());		
			});
			
			listView_all_traitements.setItems(liste_all_traitements);
		}
		
		listView_all_traitements.onDragDetectedProperty().set(dd -> {
			
			Dragboard dragBoard = listView_all_traitements.startDragAndDrop(TransferMode.MOVE);			
			ClipboardContent content = new ClipboardContent();	
			if (listView_all_traitements.getSelectionModel().getSelectedItem() != null){
				content.putString(listView_all_traitements.getSelectionModel().getSelectedItem());			 
				dragBoard.setContent(content);
				 
				listView_traitements.setOnDragOver(dd_ -> dd_.acceptTransferModes(TransferMode.MOVE));
				 
				listView_traitements.setOnDragDropped(dd_ -> {
					ajouter_traitement(dd_.getDragboard().getString());
				    dd_.setDropCompleted(true);
				 });
			}
		});
		
		listView_all_traitements.setOnDragDropped(dd_ -> dd_.setDropCompleted(true));
		
		listView_traitements.onDragDetectedProperty().set(dd -> {
			
			Dragboard dragBoard = listView_traitements.startDragAndDrop(TransferMode.MOVE);			
			ClipboardContent content = new ClipboardContent();	
			if (listView_traitements.getSelectionModel().getSelectedItem() != null){
				content.putString(listView_traitements.getSelectionModel().getSelectedItem());			 
				dragBoard.setContent(content);
				 
				listView_all_traitements.setOnDragOver(dd_ -> dd_.acceptTransferModes(TransferMode.MOVE));
				 
				listView_all_traitements.setOnDragDropped(dd_ -> {
					retirer_traitement(dd_.getDragboard().getString());
				    dd_.setDropCompleted(true);
				 });
			}	
		});
		
		listView_traitements.setOnDragDropped(dd_ -> dd_.setDropCompleted(true));

		add_t.setOnAction(a -> ajouter_traitement(listView_all_traitements.getSelectionModel().getSelectedItem()));
		remove_t.setOnAction(a -> retirer_traitement(listView_traitements.getSelectionModel().getSelectedItem()));
	
        if (Messages.getCommande() != null) {
        	
        	commande = Messages.getCommande();
        	
        	nouveau.setVisible(false);
        	annuler.setVisible(false);
        	editer.setVisible(true);
        	mise_a_jour.setVisible(false);
        	
        	editability(false);
        	
        	traitementsUtilises = commande.getTraitements_attendus_names();
        	
        	System.out.println("traitements_utilises = " + traitementsUtilises);
        			
        	liste_traitements.setAll(traitementsUtilises);
        	liste_traitements.sort((a, b) -> a.compareToIgnoreCase(b));
        	
            
        	if (commande.getModeleObj() != null){
        		modele_name = commande.getModeleObj().getNom();	
        	}
            if (commande.getAuteurObj() != null){
            	auteur_name = commande.getAuteurObj().getNom();
        	}    		
    		
    		dateCommandePicker.setValue(commande.getDateCommande_LocalDate());
    		dateDebutProjetPicker.setValue(commande.getDateDebutProjet_LocalDate());
    		dateFinProjetPicker.setValue(commande.getDateFinProjet_LocalDate());
    		
    		afficherCommande();
    		afficherAuteurs();
    		afficherModeles();
    		afficherOeuvres();
		}

		else { 
			super.onNouveauButton();

			
			commande = new Commande();
			//liste_oeuvres = FXCollections.observableArrayList();
			
			afficherModeles();
			afficherAuteurs();
			
			dateCommandePicker.setValue(LocalDate.now());
			dateDebutProjetPicker.setValue(LocalDate.now());
			dateFinProjetPicker.setValue(LocalDate.now());

			commandeExportVbox.setVisible(false);
			versRapportButton.setVisible(false);

			try {
			    nomCommandeTextField.setText(client.getNom() + "_" + LocalDate.now());
			}
			catch (NullPointerException npe) {
				
			}
		}

        afficherTraitements();
	}
}

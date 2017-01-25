package application;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import enums.Progression;
import models.Auteur;
import models.Client;
import models.Commande;
import models.Messages;
import models.Model;
import models.Oeuvre;
import models.OeuvreTraitee;
import models.Traitement;
import utils.FreeMarkerMaker;
import utils.JsonUtils;
import utils.RestAccess;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Fiche_commande_controller  implements Initializable{
	
//	@FXML
//	private ObservableList<Oeuvre> liste_oeuvres;
	@FXML
	private Label nomClientLabel;
	@FXML
	private Label fiche_commande_label;
	@FXML
	private Label nom_commande_label;
	@FXML
	private TextField nomCommandeTextField;

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
	private Button versProduitsButton;
	@FXML
	private Button versAuteursButton;
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
	private ChoiceBox<String> auteursChoiceBox;
	
	@FXML
	private VBox commandeExportVbox;
	@FXML
	private TableView<Map<String, String>> tableOeuvre;
	@FXML
	private TableColumn<Map<String, String>, String> oeuvres_nom_colonne;
	@FXML
	private TableColumn<Map<String, String>, ImageView> oeuvres_fait_colonne;
	
	@FXML
	private GridPane traitementGrid;
	
	private ArrayList<ChoiceBox<Traitement>> traitements_selectionnes;
	private ArrayList<Traitement> traitements_attendus;
	private Map<String, String> traitements_attendus_id;
    
	private ObservableList<Traitement> liste_traitements;
//	private ObservableList<String> observableTraitements;
//	private Map<String, String> traitements_id;
	
	private ObservableList<String> observableAuteurs;
	private Map<String, String> auteurs_id;
	
	private ObservableList<String> observableModeles;
	private Map<String, String> modeles_id;

	private List<Map<String, Object>> listeOeuvresTraitees;
//	private OeuvreTraitee[] oeuvresTraitees;
	private ObservableList<Map<String, String>> obs_oeuvres;
	
	private Stage currentStage;
	
	private Commande commande;
	
	private Client client;
	private Model model;
    private Auteur auteur;
    
    private String client_name;
	private String model_name;
    private String auteur_name;
    
    private int index;
    private int i;
	
	private boolean edit = false;
	
	@FXML
	public void onVersClientButton(){
		
		Scene fiche_client_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_client.fxml"), 1275, 722);
		fiche_client_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_client_scene);	
	}
	
	@FXML
	public void onVersProduitsButton(){
		
		Scene fiche_produit_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_produit.fxml"), 1275, 722);
		fiche_produit_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_produit_scene);	
	}
	@FXML
    public void onVersAuteursButton(){
    	Scene fiche_auteur_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_auteur.fxml"), 1275, 722);
		fiche_auteur_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_auteur_scene);
    }
	@FXML
    public void onVersTraitementsButton(){
		Scene fiche_traitement_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_traitement.fxml"), 1275, 722);
		fiche_traitement_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_traitement_scene);
    }
    @FXML
    public void onVersModelesButton(){
    	Scene fiche_model_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_model.fxml"), 1275, 722);
		fiche_model_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_model_scene);
    }
    @FXML
    public void onMatieres_button(){
    	Scene fiche_matiere_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_matiere.fxml"), 1275, 722);
		fiche_matiere_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_matiere_scene);
    }
    @FXML
    public void onTechniques_button(){
    	Scene fiche_technique_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_technique.fxml"), 1275, 722);
		fiche_technique_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_technique_scene);
    }
    @FXML
    public void onVersCommandeButton(){}
    @FXML
    public void onVersOeuvreButton(){}
    
    @FXML
    public void onImporterButton(){
    	Scene fiche_commande_import_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_commande_import.fxml"), 1275, 722);
		fiche_commande_import_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_commande_import_scene);
    }
    @FXML
    public void onVersFichiersButton(){}
    @FXML
    public void onVersTraitementButton(){
		Scene fiche_traitement_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_traitement.fxml"), 1275, 722);
		fiche_traitement_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_traitement_scene);
    }
    @FXML
    public void onVersModeleButton(){}
	
	@FXML
	public void onEditerButton(){
		
		importCommandeButton.setDisable(true);
		dateCommandePicker.setEditable(true);
		dateDebutProjetPicker.setEditable(true);
		dateFinProjetPicker.setEditable(true);
		remarques_client.setEditable(true);
        editer.setVisible(false);
        mise_a_jour_commande.setText("Mise à jour");
        mise_a_jour_commande.setVisible(true);
		annuler.setVisible(true);
		commandeExportVbox.setVisible(false);
		versRapportButton.setVisible(false);
		versModelesButton.setVisible(false);
		versTraitementsButton.setVisible(false);
		versFichiersButton.setVisible(false);
		fiche_commande_label.setText("FICHE COMMANDE :");
		nom_commande_label.setText(commande.getNom());
		nomCommandeTextField.setDisable(false);
		edit = true;
		
	}
	
	@FXML
	public void onAnnulerButton(){
		
		importCommandeButton.setDisable(false);
		dateCommandePicker.setEditable(false);
		dateDebutProjetPicker.setEditable(false);
		dateFinProjetPicker.setEditable(false);
		remarques_client.setEditable(false);
        editer.setVisible(true);
        mise_a_jour_commande.setVisible(false);
		annuler.setVisible(false);
		fiche_commande_label.setText("FICHE COMMANDE :");
		nomCommandeTextField.setDisable(true);
		
		
		if (edit) {
			afficherCommande();
			afficherModeles();
			afficherAuteurs();
			afficherOeuvres();
			
		}
		else {
			onVersClientButton();
		}
		edit = false;
	}
	
	@FXML
	public void onMiseAJourButton(){

		if (Messages.getCommande() == null){
			commande = new Commande();
		}
		else{
			commande = Commande.retrouveCommande(Messages.getCommande().get_id()); 
		}		

//		commande.setDateCommande(dateCommandePicker.getValue());
//		commande.setDateDebutProjet(dateDebutProjetPicker.getValue());
//		commande.setDateFinProjet(dateFinProjetPicker.getValue());
		commande.setRemarques(remarques_client.getText());
		commande.setNom(nomCommandeTextField.getText());
		//model_name = modelChoiceBox.getSelectionModel().getSelectedItem();
		//model = Model.retrouveModel(modeles_id.get(model_name));
		
		//auteur_name = auteursChoiceBox.getSelectionModel().getSelectedItem();
		//auteur = Auteur.retrouveAuteur(auteurs_id.get(auteur_name));
		//commande.setAuteur_id(auteurs_id.get(auteur_name).toString());
		
        traitements_attendus.clear();
        commande.setTraitements_attendus_map(new HashMap<String, String>());
		
		for (Node cb : traitementGrid.getChildren()){
			
			ChoiceBox<String> cbox = (ChoiceBox<String>) cb;
			String t = cbox.getValue();

			if (t != null && 
				!traitements_attendus.contains(t)){
				
				Traitement traitement_attendu = ((ChoiceBox<Traitement>) cb).getValue();
				
				traitements_attendus.add(traitement_attendu);
				commande.addTraitement_attendu_map(traitement_attendu.getNom(), traitement_attendu.get_id().toString());
				
			}
			
		}
		
		//traitements_attendus = commande.getTraitements_attendus();
		//traitements_attendus_id = commande.getTraitements_attendus_id();
//		System.out.println("mise a jour : " + traitements_attendus_id.keySet());
	
		if (edit) {
			Commande.update(commande);
		}
		else {
		   Commande.save(commande);
		   
		   client.getCommandes_id().put(commande.getNom(), commande.get_id().toString());
		   
		   RestAccess.update("client", client);
		}

		Messages.setCommande(commande);
	
		afficherTraitements();
		afficherCommande();
	    afficherModeles();
	    afficherAuteurs();
	    afficherOeuvres();
	    

	}
	
	public void afficherCommande(){
		
		importCommandeButton.setDisable(false);
		dateCommandePicker.setEditable(false);
		dateDebutProjetPicker.setEditable(false);
		dateFinProjetPicker.setEditable(false);
		remarques_client.setEditable(false);
        editer.setVisible(true);
        mise_a_jour_commande.setVisible(false);
		annuler.setVisible(false);
		fiche_commande_label.setText("FICHE COMMANDE :");
		nomClientLabel.setText(client.getNom());
		nomCommandeTextField.setDisable(true);
		nomClientLabel.setText(client.getNom());

		for (ChoiceBox<Traitement> cbt : traitements_selectionnes){
			cbt.getSelectionModel().clearSelection();
		}
        
        int i = 0;
        
        ObservableList<Traitement> menuList = FXCollections.observableArrayList(traitements_attendus);        
        menuList.add(null);

		for (Traitement t : traitements_attendus){

			traitements_selectionnes.get(i).setItems(menuList);
			traitements_selectionnes.get(i).getSelectionModel().select(i);
			i++;
		}
		
		loadCommande(commande);
	}
	
    
	@FXML
    public void onExporterToutButton(){
		
		OeuvreTraitee[] oeuvresAExporter = OeuvreTraitee.retrouveOeuvreTraiteesCommande(commande, fait_radioButton.isSelected());
		
		for ( OeuvreTraitee ot : oeuvresAExporter){
			
			System.out.println("export de la fiche : " + ot.getCote() + " en cours ...");
			
			Oeuvre o = ot.getOeuvre();
			FreeMarkerMaker.odt2pdf(o, ot);
		}
		
		 
	}
	@FXML
    public void onRapportsButton(){}
	@FXML
    public void onVersRapportButton(){}
	
	public void loadCommande(Commande c){
		
		if (c != null){
		
			dateCommandePicker.setValue(c.getDateCommande());;
//			dateDebutProjetPicker.setValue(c.getDateDebutProjet());;
//			dateFinProjetPicker.setValue(c.getDateFinProjet());
			remarques_client.setText(c.getRemarques());
			nom_commande_label.setText(c.getNom());
			nomCommandeTextField.setText(c.getNom());	
		}
	}
	
	
    
    public void afficherTraitements(){
    	
    	if (liste_traitements == null){
    		
    		liste_traitements = FXCollections.observableArrayList();
    		JsonUtils.JsonToListObj(RestAccess.requestAll("traitement"), liste_traitements, new TypeReference<List<Traitement>>() {});
    		
    		for (Node cb : traitementGrid.getChildren()){			
    			((ChoiceBox<Traitement>) cb).setItems(liste_traitements);
    		}
    	}
    	
		for (int i = 0; i < commande.getTraitements_attendus_names().size(); i++){
			((ChoiceBox<String>)traitementGrid.getChildren().get(i)).getSelectionModel().select(i);
    	}
    }
    
    public Comparator<? super Map<String, Object>> otTrieeParNom = 
    		(Map<String, Object> o1, Map<String, Object> o2)-> o1.get("oeuvresTraitee_string").toString().compareTo(o2.get("oeuvresTraitee_string").toString());
    
    public void afficherOeuvres(){

		obs_oeuvres = FXCollections.observableArrayList(commande.getOeuvresTraitees());

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
    
    public void afficherAuteur(){
    	
    	observableAuteurs = FXCollections.observableArrayList();
    	observableAuteurs.add(commande.getAuteur_name());
    	auteursChoiceBox.setItems(observableAuteurs);
    }
    
    public void afficherAuteurs(){
	
	    index = 0;
		
		observableAuteurs = FXCollections.observableArrayList();
		auteurs_id = new TreeMap<>();
		
		if (commande != null && commande.getAuteur() != null){
			auteur = commande.getAuteur();
		}
		else {
			Auteur[] auteurs = Auteur.retrouveAuteurs();
			
			observableAuteurs.add(null);
			for (Auteur auteur : auteurs){
				observableAuteurs.add(auteur.getNom());

			}
		}
		
		
		for (String aut : observableAuteurs){
			
			if (auteur != null && auteur.getNom().equals(aut)){
				break;
			}
			index++;
		}

		auteursChoiceBox.setItems(observableAuteurs);
		auteursChoiceBox.getSelectionModel().select(index);
	}
    
    public void afficherModele(){
    	
    	observableModeles = FXCollections.observableArrayList();
    	observableModeles.add(commande.getModele_name());
    	modelChoiceBox.setItems(observableModeles);
    }
    
    public void afficherModeles(){
    	
		
		index = 0;
    	
    	observableModeles = FXCollections.observableArrayList();
    	modeles_id = new TreeMap<>();
    	
    	if (commande != null && commande.getModel() != null){
   
			model = commande.getModel();
    	}
    	else {
    		Model[] models = Model.retrouveModels();
            for (Model model : models){
    			observableModeles.add(model.getNom());
    		} 
    	}
    	
    	for (String mo: observableModeles){
    		
    		if (model != null && model.getNom().equals(mo)){
				break;
			}
			index++;
    	}

		modelChoiceBox.setItems(observableModeles);

		modelChoiceBox.getSelectionModel().select(index);
    }
    
    public void onOeuvreSelect(){
    	
    	String ots = RestAccess.request("oeuvreTraitee", new ObjectId(tableOeuvre.getSelectionModel().getSelectedItem().get("oeuvresTraitee_id")));
    	
    	ObjectMapper mapper = new ObjectMapper();
    	
    	OeuvreTraitee ot_;
		try {
			ot_ = mapper.readValue(ots, OeuvreTraitee.class);
			Messages.setOeuvreTraitee(ot_);
	    	//TODO java.lang.ClassCastException: java.util.LinkedHashMap cannot be cast to models.OeuvreTraitee
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	Scene fiche_oeuvre_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_oeuvre.fxml"), 1275, 722);
		fiche_oeuvre_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_oeuvre_scene);
    	
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
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
		
		currentStage = Messages.getStage();
		
//		observableTraitements = FXCollections.observableArrayList();
//		traitements_id = new TreeMap<>();
		traitements_selectionnes = new ArrayList<>();
		observableAuteurs = FXCollections.observableArrayList();
		traitements_attendus = new ArrayList<>();
//		traitements_attendus_id = new TreeMap<>();
	
        if (Messages.getCommande() != null) {
        	
        	commande = Messages.getCommande();
        	System.out.println("commande trouvée : " + commande);
        	
        	System.out.println(commande.getAuteur_id());
        	System.out.println(commande.getAuteur_name());
        	//System.out.println(commande.getAuteur());
        	
        	commande.setModele_map(new AbstractMap.SimpleEntry<>(commande.getModele_name(), commande.getModele_id()));
    		commande.setAuteur_map(new AbstractMap.SimpleEntry<>(commande.getAuteur_name(), commande.getAuteur_id()));

        	model_name = commande.getModele_name();
        	
    		auteur_name = commande.getAuteur_name();
    		
    		//traitements_attendus_id = commande.getTraitements_attendus_id();
    		traitements_attendus.addAll(traitements_attendus);
    		
    		System.out.println("afficher commande");
    		afficherCommande();
    		System.out.println("afficher oeuvres");
    		afficherOeuvres();
		}

		else { 
			
			commande = new Commande();
			//liste_oeuvres = FXCollections.observableArrayList();
			
			dateCommandePicker.setValue(LocalDate.now());
			dateDebutProjetPicker.setValue(LocalDate.now());
			dateFinProjetPicker.setValue(LocalDate.now());
			
			importCommandeButton.setDisable(true);
			dateCommandePicker.setEditable(true);
			dateDebutProjetPicker.setEditable(true);
			dateFinProjetPicker.setEditable(true);
			remarques_client.setEditable(true);
	        editer.setVisible(false);
	        mise_a_jour_commande.setText("Créer");
	        mise_a_jour_commande.setVisible(true);
			annuler.setVisible(true);
			commandeExportVbox.setVisible(false);
			versRapportButton.setVisible(false);
			
			fiche_commande_label.setText("FICHE COMMANDE (nouvelle commande) :");
			nom_commande_label.setText("");

			try {
			    nomClientLabel.setText(client.getNom());
			    nomCommandeTextField.setText(client.getNom() + "_" + LocalDate.now());
			}
			catch (NullPointerException npe) {
				
			}
		}
		
//		afficherAuteurs();
//		afficherModeles();
        
        afficherTraitements();
		
		afficherAuteur();
		afficherModele();
	}
}

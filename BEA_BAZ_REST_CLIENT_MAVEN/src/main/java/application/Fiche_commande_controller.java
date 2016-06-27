package application;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.jongo.MongoCursor;

import enums.Progression;
import models.Auteur;
import models.Client;
import models.Commande;
import models.Messages;
import models.Model;
import models.Oeuvre;
import models.OeuvreTraitee;
import models.TacheTraitement;
import models.Traitement;
import utils.FreeMarkerMaker;
import utils.RestAccess;
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
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Fiche_commande_controller  implements Initializable{
	
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
	private TableView<OeuvreTraitee> tableOeuvre;
	@FXML
	private TableColumn<OeuvreTraitee, String> oeuvres_nom_colonne;
	@FXML
	private TableColumn<OeuvreTraitee, ImageView> oeuvres_fait_colonne;
	
	@FXML
	private GridPane traitementGrid;
	
	private ArrayList<ChoiceBox<String>> traitements_selectionnes;
	private ArrayList<String> traitements_attendus;
	private Map<String, String> traitements_attendus_id;

	private ObservableList<String> observableTraitements;
	private Map<String, String> traitements_id;
	
	private ObservableList<String> observableAuteurs;
	private Map<String, String> auteurs_id;
	
	private ObservableList<String> observableModeles;
	private Map<String, String> modeles_id;

	private List<OeuvreTraitee> listeOeuvresTraitees;
	private OeuvreTraitee[] oeuvresTraitees;
	private ObservableList<OeuvreTraitee> obs_oeuvres;
	
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
		model_name = modelChoiceBox.getSelectionModel().getSelectedItem();
		model = Model.retrouveModel(modeles_id.get(model_name));
		commande.setModele_id(modeles_id.get(model_name).toString());
		auteur_name = auteursChoiceBox.getSelectionModel().getSelectedItem();
		auteur = Auteur.retrouveAuteur(auteurs_id.get(auteur_name));
		commande.setAuteur_id(auteurs_id.get(auteur_name).toString());
		
        traitements_attendus.clear();
        commande.setTraitements_attendus_id(new HashMap<String, String>());
		
		for (Node cb : traitementGrid.getChildren()){
			
			ChoiceBox<String> cbox = (ChoiceBox<String>) cb;
			String t = cbox.getValue();

			if (t != null && 
				!traitements_attendus.contains(t)){
				
				String traitement_attendu = ((ChoiceBox<String>) cb).getValue();
				
				traitements_attendus.add(traitement_attendu);
				commande.addTraitement_attendu_id(traitement_attendu, traitements_id.get(traitement_attendu));
				
			}
			
		}
		
		traitements_attendus_id = commande.getTraitements_attendus_id();
		System.out.println("mise a jour : " + traitements_attendus_id.keySet());
	
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

		for (ChoiceBox<String> cbt : traitements_selectionnes){
			cbt.getSelectionModel().clearSelection();
		}
        
        int i = 0;
        System.out.println("afficher : " + traitements_attendus_id.keySet());
        
        ObservableList<String> menuList = FXCollections.observableArrayList(traitements_attendus_id.keySet());        
        menuList.add(null);

		for (String t : traitements_attendus_id.keySet()){

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
    	
    	observableTraitements.clear();
    	
    	Traitement[] traitements = Traitement.retrouveTraitements();
    	
    	for (Traitement t : traitements){
    		observableTraitements.add(t.getNom());
    	}
    	
    	

		for (Node cb : traitementGrid.getChildren()){
			
			((ChoiceBox<String>) cb).setItems(observableTraitements);
			traitements_selectionnes.add(((ChoiceBox<String>) cb));
		}
    }
    
    public Comparator<OeuvreTraitee> otTrieeParNom = 
    		(OeuvreTraitee o1, OeuvreTraitee o2)-> o1.getNom().compareTo(o2.getNom());
    
    public void afficherOeuvres(){
    	
    	obs_oeuvres = FXCollections.observableArrayList();
    	listeOeuvresTraitees = new ArrayList<>();

		oeuvresTraitees = OeuvreTraitee.retrouveOeuvreTraiteesCommande(commande, false);
		
		System.out.println("lise des oeuvres : " + oeuvresTraitees);
		
		for (OeuvreTraitee ot : oeuvresTraitees){
			System.out.println("ajout ot : " + ot);
			listeOeuvresTraitees.add(ot);
		}
		
		listeOeuvresTraitees.sort(otTrieeParNom);
		
		obs_oeuvres = FXCollections.observableArrayList(listeOeuvresTraitees);
	
    	oeuvres_nom_colonne.setCellValueFactory(new PropertyValueFactory<OeuvreTraitee, String>("nom"));
		oeuvres_fait_colonne.setCellValueFactory(new PropertyValueFactory<OeuvreTraitee, ImageView>("icone_progression"));
    	
		tableOeuvre.setItems(obs_oeuvres);

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
    	
    	Messages.setOeuvreTraitee((OeuvreTraitee) tableOeuvre.getSelectionModel().getSelectedItem());
    	
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
		
		observableTraitements = FXCollections.observableArrayList();
		traitements_id = new TreeMap<>();
		traitements_selectionnes = new ArrayList<>();
		observableAuteurs = FXCollections.observableArrayList();
		traitements_attendus = new ArrayList<>();
		traitements_attendus_id = new TreeMap<>();
		
		afficherTraitements();
		
        if (Messages.getCommande() != null) {
        	
        	commande = Messages.getCommande();
        	System.out.println("commande trouvée : " + commande);

        	model = Model.retrouveModel(commande.getModele_id());
        	
    		auteur = Auteur.retrouveAuteur(commande.getAuteur_id());
    		
    		traitements_attendus_id = commande.getTraitements_attendus_id();
    		traitements_attendus.addAll(traitements_attendus_id.keySet());
    		
    		System.out.println("afficher commande");
    		afficherCommande();
    		System.out.println("afficher oeuvres");
    		afficherOeuvres();
		}

		else { 
			
			commande = new Commande();
			liste_oeuvres = FXCollections.observableArrayList();
			
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
		
		afficherAuteurs();
		afficherModeles();
		
	}
}

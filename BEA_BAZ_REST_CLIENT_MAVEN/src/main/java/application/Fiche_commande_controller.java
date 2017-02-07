package application;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import enums.Progression;
import models.Auteur;
import models.Client;
import models.Commande;
import models.Contexte;
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
import javafx.scene.control.ComboBox;
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

public class Fiche_commande_controller extends Fiche_controller implements Initializable{
	
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
	private GridPane traitementGrid;
	
	private ArrayList<ChoiceBox<Traitement>> traitements_selectionnes;
	private ArrayList<Traitement> traitements_attendus;
    
	private ObservableList<Traitement> liste_traitements;
	private ObservableList<String> observableTraitements;

	private static ObservableList<String> observableModeles;

	private List<Map<String, Object>> listeOeuvresTraitees;
//	private OeuvreTraitee[] oeuvresTraitees;
	
	
	private Commande commande;
	
	private Client client;
	private Model modele;
    private Auteur auteur;
    
    private String client_name;
	private String modele_name;
    private String auteur_name;
    
    private int index;
    private int i;
    
    private static List<Model> models;
    private static List<Auteur> auteurs;
    
    DateTimeFormatter formatter;
    
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
		
		fiche_commande_label.setText("FICHE COMMANDE :");
		nom_commande_label.setText(commande.getNom());
		nomCommandeTextField.setDisable(false);
		
	}
	
	@FXML
	public void onAnnulerButton(){
		boolean edit_save = edit;
		super.onAnnulerEditButton();
		nouveau.setVisible(false);
		editability(false);
		
		importCommandeButton.setDisable(false);

		fiche_commande_label.setText("FICHE COMMANDE :");
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
		boolean edit_save = edit;
		super.onMiseAJourButton();
		nouveau.setVisible(false);

		if (Messages.getCommande() == null){
			commande = new Commande();
		}
		else{
			commande = Commande.retrouveCommande(Messages.getCommande().get_id()); 
		}		

		commande.setLocalDateCommande(dateCommandePicker.getValue());
		commande.setLocalDateDebutProjet(dateDebutProjetPicker.getValue());
		commande.setLocalDateFinProjet(dateFinProjetPicker.getValue());
		commande.setRemarques(remarques.getText());
		commande.setNom(nomCommandeTextField.getText());
		//model_name = modelChoiceBox.getSelectionModel().getSelectedItem();
		//model = Model.retrouveModel(modeles_id.get(model_name));
		
		//auteur_name = auteursChoiceBox.getSelectionModel().getSelectedItem();
		//auteur = Auteur.retrouveAuteur(auteurs_id.get(auteur_name));
		//commande.setAuteur_id(auteurs_id.get(auteur_name).toString());
		
        traitements_attendus.clear();
		
		for (Node cb : traitementGrid.getChildren()){
			
			ChoiceBox<String> cbox = (ChoiceBox<String>) cb;
			String t = cbox.getValue();

			if (t != null && 
				!traitements_attendus.contains(t)){
				
				Traitement traitement_attendu = ((ChoiceBox<Traitement>) cb).getValue();
				
				traitements_attendus.add(traitement_attendu);

				
			}
			
		}
		
		//traitements_attendus = commande.getTraitements_attendus();
		//traitements_attendus_id = commande.getTraitements_attendus_id();
//		System.out.println("mise a jour : " + traitements_attendus_id.keySet());
	
		if (edit_save) {
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
		
		editability(false);
		
		importCommandeButton.setDisable(false);

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
    public void onRapportsButton(){}
	@FXML
    public void onVersRapportButton(){}
	
	public void loadCommande(Commande c){
		
		if (c != null){
		
			dateCommandePicker.setValue(c.getDateCommande());;
//			dateDebutProjetPicker.setValue(c.getDateDebutProjet());;
//			dateFinProjetPicker.setValue(c.getDateFinProjet());
			remarques.setText(c.getRemarques());
			nom_commande_label.setText(c.getNom());
			nomCommandeTextField.setText(c.getNom());	
		}
	}
	
	
    
    public void afficherTraitements(){
    	
    	if (liste_traitements == null){
    		
    		liste_traitements = FXCollections.observableArrayList();
    		JsonUtils.JsonToListObj(RestAccess.requestAll("traitement"), liste_traitements, new TypeReference<List<Traitement>>() {});
    		
    		List<String> liste_traitements_str = liste_traitements.stream()
                    											  .map(a -> a.getNom())
                    											  .collect(Collectors.toList());
    		
    		
    		
    		for (Node cb : traitementGrid.getChildren()){			
    			((ChoiceBox<String>) cb).setItems(FXCollections.observableArrayList(liste_traitements_str));
    		}
    	}
    	
		for (int i = 0; i < commande.getTraitements_attendus().size(); i++){
			((ChoiceBox<String>)traitementGrid.getChildren().get(i)).getSelectionModel().select(commande.getTraitements_attendus().get(i).keySet().iterator().next());
    	}
    }
    
    public Comparator<? super Map<String, Object>> otTrieeParNom = 
    		(Map<String, Object> o1, Map<String, Object> o2)-> o1.get("oeuvresTraitee_string").toString().compareTo(o2.get("oeuvresTraitee_string").toString());
    
    public void afficherOeuvres(){
        
    	if(obs_oeuvres == null){
    		obs_oeuvres = FXCollections.observableArrayList(commande.getOeuvresTraitees());
    	}
    	else {
    		obs_oeuvres.clear();
    		obs_oeuvres.addAll(commande.getOeuvresTraitees());
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
    
    public void afficherAuteur(){
    	
    	if (auteurs != null){
    		afficherAuteurs();
    	}
    	else {
    		observableAuteurs = FXCollections.observableArrayList();
        	observableAuteurs.add(auteur_name);
        	auteursChoiceBox.setItems(observableAuteurs);
        	auteursChoiceBox.getSelectionModel().select(0);
    	}
    }
    
    public void afficherAuteurs(){
	
	    index = 0;
	    
	    if (auteurs == null){
	    	observableAuteurs = FXCollections.observableArrayList();
	    	auteurs = Arrays.asList(Auteur.retrouveAuteurs());
	    	observableAuteurs.add(null);
	    	for (Auteur auteur : auteurs){
				observableAuteurs.add(auteur.getNom());

			}
	    }
	    
	    auteursChoiceBox.setItems(observableAuteurs);

		if (commande != null && commande.getAuteur() != null){
			auteur = commande.getAuteurObj();
			for (String aut : observableAuteurs){
				
				if (auteur != null && auteur.getNom().equals(aut)){
					break;
				}
				index++;
			}
			auteursChoiceBox.getSelectionModel().select(index);
		}	
	}
    
    public void afficherModele(){
    	
    	if (models != null){
    		afficherModeles();
    	}
    	else {
    		observableModeles = FXCollections.observableArrayList();
        	observableModeles.add(modele_name);
        	modelChoiceBox.setItems(observableModeles);
        	modelChoiceBox.getSelectionModel().select(0);
    	}	
    }
    
    public void afficherModeles(){
    	
		index = 0;

      	if (models == null){
      		observableModeles = FXCollections.observableArrayList();
      		models = Arrays.asList(Model.retrouveModels());
      		observableModeles.add(null);
            for (Model model : models){
    			observableModeles.add(model.getNom());
    		}
      	}

      	modelChoiceBox.setItems(observableModeles);

    	if (commande != null && commande.getModele() != null){      		
			modele = commande.getModeleObj();
			
			for (String mo: observableModeles){
	    		
	    		if (modele != null && modele.getNom().equals(mo)){
					break;
				}
				index++;
	    	}			
			modelChoiceBox.getSelectionModel().select(index);
    	}	
    }
    
    public void onOeuvreSelect(){
    	
    	OeuvreTraitee ot_ = OeuvreTraitee.retrouveOeuvreTraitee(new ObjectId(tableOeuvre.getSelectionModel().getSelectedItem().get("oeuvresTraitee_id")));
    	Messages.setOeuvreTraiteeObj(ot_);

    	Scene fiche_oeuvre_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_oeuvre_v2.fxml"), Contexte.largeurFenetre, Contexte.hauteurFenetre);
		fiche_oeuvre_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		System.out.println(currentStage);
		currentStage.setScene(fiche_oeuvre_scene);
    	
    }
    
    public void editability(boolean bool){
    	importCommandeButton.setDisable(bool);
    	
		dateCommandePicker.setDisable(!bool);
		dateDebutProjetPicker.setDisable(!bool);
		dateFinProjetPicker.setDisable(!bool);
		for (Node cb : traitementGrid.getChildren()){
			((ChoiceBox<Traitement>) cb).setDisable(!bool);
		}
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
		
		observableTraitements = FXCollections.observableArrayList();

		traitements_selectionnes = new ArrayList<>();
		traitements_attendus = new ArrayList<>();
		
		commande = Messages.getCommande();
	
        if (Messages.getCommande() != null) {
        	
        	nouveau.setVisible(false);
        	annuler.setVisible(false);
        	editer.setVisible(true);
        	mise_a_jour.setVisible(false);
        	
        	editability(false);

        	modele_name = commande.getModeleObj().getNom();	
    		auteur_name = commande.getAuteurObj().getNom();
    		
    		dateCommandePicker.setValue(commande.getDateCommande());
    		dateDebutProjetPicker.setValue(commande.getDateDebutProjet());
    		dateFinProjetPicker.setValue(commande.getDateFinProjet());
    		
    		afficherCommande();
    		afficherAuteur();
    		afficherModele();
    		afficherOeuvres();
		}

		else { 
			editability(true);
			super.onCreerButton();

			
			commande = new Commande();
			//liste_oeuvres = FXCollections.observableArrayList();
			
			afficherModeles();
			afficherAuteurs();
			
			dateCommandePicker.setValue(LocalDate.now());
			dateDebutProjetPicker.setValue(LocalDate.now());
			dateFinProjetPicker.setValue(LocalDate.now());

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

        afficherTraitements();
	}
}

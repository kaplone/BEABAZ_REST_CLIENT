package controleurs;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class Fiche_commande_controller extends Fiche_controller implements Initializable{
	
	@FXML
	private Label nomClientLabel;
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

	private static ObservableList<String> observableModeles;	
	
	private Commande commande;
	
	private Client client;
	private Model modele;
    private Auteur auteur;

	private String modele_name;
    private String auteur_name;
    
    private int index;
    
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
		else{
			commande = Commande.retrouveCommande(new ObjectId(Messages.getCommande().get_id())); 
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
		
		System.out.println(commande);
	
		if (edit) {
			commande.update("commande");
		}
		else {
		   commande.save("commande", Commande.class);
		   System.out.println(client);
		   System.out.println(client.getCommandes_id());
		   System.out.println(commande);
		   System.out.println(commande.getNom());
		   System.out.println(commande.get_id());
		   
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
//	@FXML
//    public void onRapportsButton(){}
	@FXML
    public void onVersRapportButton(){}
	
	public void loadCommande(Commande c){
		
		if (c != null){
		
			dateCommandePicker.setValue(c.getDateCommande_LocalDate());
//			dateDebutProjetPicker.setValue(c.getDateDebutProjet());;
//			dateFinProjetPicker.setValue(c.getDateFinProjet());
			remarques.setText(c.getRemarques());
			nom_label.setText(c.getNom());
			nomCommandeTextField.setText(c.getNom());	
		}
	}
	
	
    
    public void afficherTraitements(){
    	
    	liste_traitements = Messages.getTous_les_traitements();
		List<String> liste_traitements_str = liste_traitements.stream()
                											  .map(a -> a.getNom())
                											  .collect(Collectors.toList());
	
		for (Node cb : traitementGrid.getChildren()){			
			((ChoiceBox<String>) cb).setItems(FXCollections.observableArrayList(liste_traitements_str));
    	}
    	
    	if (commande != null && commande.getTraitements_attendus() != null){
    		for (int i = 0; i < commande.getTraitements_attendus().size(); i++){
    			((ChoiceBox<String>)traitementGrid.getChildren().get(i)).getSelectionModel().select(commande.getTraitements_attendus().get(i).keySet().iterator().next());
        	}
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
    		
    		dateCommandePicker.setValue(commande.getDateCommande_LocalDate());
    		dateDebutProjetPicker.setValue(commande.getDateDebutProjet_LocalDate());
    		dateFinProjetPicker.setValue(commande.getDateFinProjet_LocalDate());
    		
    		afficherCommande();
    		afficherAuteur();
    		afficherModele();
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
			    nomClientLabel.setText(client.getNom());
			    nomCommandeTextField.setText(client.getNom() + "_" + LocalDate.now());
			}
			catch (NullPointerException npe) {
				
			}
		}

        afficherTraitements();
	}
}

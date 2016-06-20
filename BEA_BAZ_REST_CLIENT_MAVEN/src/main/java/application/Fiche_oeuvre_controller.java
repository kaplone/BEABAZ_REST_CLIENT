package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.jongo.MongoCursor;

import enums.EtatFinal;
import enums.Progression;
import utils.FreeMarkerMaker;
import utils.MongoAccess;
import utils.Normalize;
import models.Auteur;
import models.Commande;
import models.Fichier;
import models.Matiere;
import models.Messages;
import models.Oeuvre;
import models.OeuvreTraitee;
import models.Produit;
import models.TacheTraitement;
import models.Technique;
import models.Traitement;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class Fiche_oeuvre_controller  implements Initializable{

	@FXML
	private TableView<TacheTraitement> traitements_attendus_tableView;
	@FXML
	private TableColumn<TacheTraitement, String> traitements_attendus_tableColumn;
	@FXML
	private TableColumn<TacheTraitement, ImageView> faits_attendus_tableColumn;
	
	@FXML
	private ListView<String> fichiers_listView;

	@FXML
	private Button annuler;
	@FXML
	private Button editer;
	@FXML
	private Button versOeuvreButton;
	@FXML
	private Button versTraitementsButton;
	@FXML
	private Button versModelesButton;
	@FXML
	private Button versRapportButton;
	@FXML
	private Button versFichiersButton;
	@FXML
	private Button versProduitsButton;
	@FXML
	private Button versAuteursButton;
	@FXML
	private Button mise_a_jour_oeuvre;
	
	@FXML
	private Polygon precedent_fleche;
	@FXML
	private Polygon suivant_fleche;
	
	@FXML
	private Label fiche_oeuvre_label;
	@FXML
	private Label nom_oeuvre_label;
	@FXML
	private ChoiceBox<String> auteursChoiceBox;
	@FXML
	private TextField numero_archive_6s_textField;
	@FXML
	private TextField titre_textField;
	@FXML
	private TextField date_oeuvre_textField;
	@FXML
	private TextField dimensions_textField;
	@FXML
	private TextArea inscriptions_textArea;
	@FXML
	private TextArea degradations_textArea;
	@FXML
	private TextArea remarques_textArea;
	@FXML
	private TextArea observations_textArea;
	@FXML
	private HBox matieres_hbox;
	@FXML
	private HBox techniques_hbox;
	@FXML
	private ListView<String> matieres_listView;
	@FXML
	private ListView<String> techniques_listView;
	
	@FXML
	private ImageView preview_imageView;
	
	
	@FXML
	private GridPane grid;
	@FXML
	private ChoiceBox<EtatFinal> etat_final_choiceBox;
	@FXML
	private TextArea complement_etat_textArea;
	
	@FXML
	private TableView<OeuvreTraitee> tableOeuvre;
	@FXML
	private TableColumn<OeuvreTraitee, String> oeuvres_nom_colonne;
	@FXML
	private TableColumn<OeuvreTraitee, ImageView> oeuvres_fait_colonne;
	//private TableColumn<OeuvreTraitee, String> oeuvres_fait_colonne;

	private List<OeuvreTraitee> oeuvresTraitees;
	
	private boolean edit = false;
	private Oeuvre oeuvreSelectionne;
	private OeuvreTraitee oeuvreTraiteeSelectionne;
	private MongoCursor<TacheTraitement> traitementCursor ;
	private ObservableList<TacheTraitement> traitementsAttendus;
	private ObservableList<String> observableAuteurs;
	private ObservableList<String> observableFichiers;
	private TacheTraitement traitementSelectionne;
	private String commandeSelectionne;
	
	private Stage currentStage;
	private Auteur auteur;
	
	boolean directSelect = false;
	
	private MongoCursor<OeuvreTraitee> oeuvresTraiteesCursor;
	private MongoCursor<Matiere> matieresCursor;
	private MongoCursor<Technique> techniquesCursor;
	
	private ObservableList<String> matieres;
	private ObservableList<String> techniques;
	
	private Set<String> matieresUtilisees;
	private Set<String> techniquesUtilisees;
	
	private Matiere matiere;
	private Technique technique;
	
	private ObjectId matiereSelectionne_id;
	private Map<String, ObjectId> matieres_id;
	
	private ObjectId techniqueSelectionne_id;
	private Map<String, ObjectId> techniques_id;
	
	private Map<String, ObjectId> auteurs_id;
	
	private Map<String, ObjectId> fichiers_id;
	
	private ObservableList<EtatFinal> etatsFinaux;
	
	@FXML
	public void onVersProduitsButton(){
		
		Scene fiche_produit_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_produit.fxml"), 1275, 722);
		fiche_produit_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_produit_scene);	
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
    public void onVersAuteursButton(){
    	Scene fiche_auteur_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_auteur.fxml"), 1275, 722);
		fiche_auteur_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_auteur_scene);
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
    public void onVersClientButton(){
    	Scene fiche_client_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_client.fxml"), 1275, 722);
		fiche_client_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_client_scene);
    }
	@FXML
	public void onVersCommandeButton(){
		
		Scene fiche_commande_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_commande.fxml"), 1275, 722);
		fiche_commande_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_commande_scene);	
	}	
	
	@FXML
	public void onOeuvreSelect(){
	    
		oeuvreTraiteeSelectionne = (OeuvreTraitee) tableOeuvre.getSelectionModel().getSelectedItem();
		Messages.setOeuvreTraitee(oeuvreTraiteeSelectionne);
		
		Messages.setAuteur(null);
		
		directSelect = true;
		reloadOeuvre();
	
	}

	
	public void reloadOeuvre(){
		
//		complement_etat_textArea.textProperty().removeListener((observable, oldValue, newValue) -> {
//			onEditerOeuvreButton();
//		});
//		auteursChoiceBox.valueProperty().removeListener((observable, oldValue, newValue) -> {
//			onEditerOeuvreButton();
//		});
//		etat_final_choiceBox.valueProperty().removeListener((observable, oldValue, newValue) -> {
//			onEditerOeuvreButton();
//		});
        
		oeuvreTraiteeSelectionne = Messages.getOeuvreTraitee();
		oeuvreSelectionne = oeuvreTraiteeSelectionne.getOeuvre();
		
		matieresUtilisees = oeuvreSelectionne.getMatieresUtilisees_id().keySet();
		techniquesUtilisees = oeuvreSelectionne.getTechniquesUtilisees_id().keySet();

		Messages.setOeuvre_index(tableOeuvre.getSelectionModel().getSelectedIndex());
		Messages.setTraitementsAttendus(null);
		Messages.setObservableFichiers(null);
        Messages.setFichiers_id(null);
 
		if (directSelect){
		   tableOeuvre.scrollTo(Messages.getOeuvre_index() -9);
		  // tableOeuvre.getSelectionModel().clearAndSelect(Messages.getOeuvre_index());
		  // tableOeuvre.getSelectionModel().focus(Messages.getOeuvre_index());
		}
		else {
			tableOeuvre.scrollTo(Messages.getOeuvre_index());
		}
		
		numero_archive_6s_textField.setText(oeuvreSelectionne.getCote_archives_6s());
		titre_textField.setText(oeuvreSelectionne.getTitre_de_l_oeuvre());

		date_oeuvre_textField.setText(oeuvreSelectionne.getDate());
		dimensions_textField.setText(oeuvreSelectionne.getDimensions());
		inscriptions_textArea.setText(oeuvreSelectionne.getInscriptions_au_verso());
		degradations_textArea.setText(oeuvreTraiteeSelectionne.getAlterations().stream()
				                                                               .map(o -> o.replace("oui/non", ""))
				                                                               .collect(Collectors.joining("\n")));
		observations_textArea.setText(oeuvreTraiteeSelectionne.getObservations());
		remarques_textArea.setText(oeuvreTraiteeSelectionne.getRemarques());
		
		matieres_hbox.getChildren().clear();
		techniques_hbox.getChildren().clear();
		
		if (oeuvreSelectionne.getMatieresUtilisees_id() != null){
			for (String m : oeuvreSelectionne.getMatieresUtilisees_id().keySet()){
				affichageMatieresUtilises(m);
			}
		}
		
		if (oeuvreSelectionne.getTechniquesUtilisees_id() != null){
			for (String t : oeuvreSelectionne.getTechniquesUtilisees_id().keySet()){
				affichageTechniquesUtilises(t);
			}
		}
		
		Fichier fichierSelectionne = MongoAccess.request("fichier", "nom", oeuvreSelectionne.getCote_archives_6s(), true).as(Fichier.class);
		
//		System.out.println(fichierSelectionne.getFichierLie().toString());
		
		preview_imageView.setImage(new Image(String.format("file:%s" ,fichierSelectionne.getFichierLie().toString())));
		
		etat_final_choiceBox.getSelectionModel().select(oeuvreTraiteeSelectionne.getEtat());
		complement_etat_textArea.setText(oeuvreTraiteeSelectionne.getComplement_etat());

		editer.setVisible(true);
        mise_a_jour_oeuvre.setVisible(false);
		annuler.setVisible(false);
	
		afficherTraitements();
		afficherFichiers();
		
//		complement_etat_textArea.textProperty().addListener((observable, oldValue, newValue) -> {
//			onEditerOeuvreButton();
//		});
		
		if(Messages.getAuteur() != null){
			auteur = Messages.getAuteur();
		}
		else {
			auteur = MongoAccess.request("auteur", oeuvreSelectionne.getAuteur()).as(Auteur.class).next();
			Messages.setAuteur(auteur);
		}
		
		
		afficherAuteurs();
		auteursChoiceBox.getSelectionModel().select(auteur.getNom());
		
//		auteursChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
//			
//			System.out.println(oldValue);
//			System.out.println(newValue);
//			
//			if(newValue != null && ! newValue.equals(auteur.getNom())){
//				
//				System.out.println(auteurs_id);
//				System.out.println(auteurs_id.get(newValue));
//				
//				auteur = MongoAccess.request("auteur", auteurs_id.get(newValue)).as(Auteur.class).next();
//				
//				Messages.setAuteur(auteur);
//				onEditerOeuvreButton();
//			}	
//		});
//		
//		etat_final_choiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
//			onEditerOeuvreButton();
//		});
		
	}
	
	public void afficherTraitements(){
		
		traitementsAttendus.clear();
		
		if (Messages.getTraitementsAttendus() == null){
			for (ObjectId tt_id : oeuvreTraiteeSelectionne.getTraitementsAttendus_id()){
				
				traitementsAttendus.add(MongoAccess.request("tacheTraitement", tt_id).as(TacheTraitement.class).next());
			}
			Messages.setTraitementsAttendus(traitementsAttendus);
		}
		else {
			traitementsAttendus.addAll(Messages.getTraitementsAttendus());
		}

		traitements_attendus_tableColumn.setCellValueFactory(new PropertyValueFactory<TacheTraitement, String>("nom"));
		faits_attendus_tableColumn.setCellValueFactory(new PropertyValueFactory<TacheTraitement, ImageView>("icone_progression"));
		traitements_attendus_tableView.setItems(traitementsAttendus);

	}
	
    public void afficherAuteurs(){
    	
    	int index = 0;
    	
    	observableAuteurs = FXCollections.observableArrayList();
    	
    	if (Messages.getAuteurs_id() == null){
    		
            MongoCursor<Auteur> auteurCursor = MongoAccess.request("auteur").as(Auteur.class);
 
            observableAuteurs.add(null);
    		
    		while (auteurCursor.hasNext()){
    			Auteur auteur_ = auteurCursor.next();
    			observableAuteurs.addAll(auteur_.getNom());
    			auteurs_id.put(auteur_.getNom(), auteur_.get_id());
    			if (auteur != null && auteur.getNom().equals(auteur_.getNom())){
    				index ++;
    				break;
    			}
    		}	
    		Messages.setAuteurs_id(auteurs_id);
    	}
    	else {
    		observableAuteurs.addAll(Messages.getAuteurs_id().keySet());
    		for (String s : Messages.getAuteurs_id().keySet()){
    			if (auteur != null && auteur.getNom().equals(s)){
    				break;
    			}
    			else{
    				index ++;
    			}
    		}
    	}

		auteursChoiceBox.setItems(observableAuteurs);
		auteursChoiceBox.getSelectionModel().select(index);
	}

    @FXML
    public void onEditerOeuvreButton(){
    	
    	annuler.setVisible(true);
    	editer.setVisible(false);
    	mise_a_jour_oeuvre.setVisible(true);
    	
    	numero_archive_6s_textField.setEditable(true);
		titre_textField.setEditable(true);
		date_oeuvre_textField.setEditable(true);
		dimensions_textField.setEditable(true);
		inscriptions_textArea.setEditable(true);
		degradations_textArea.setEditable(true);
		observations_textArea.setEditable(true);
		remarques_textArea.setEditable(true);

    }
//    
    @FXML
    public void onAnnulerEditButton(){
    	
    	annuler.setVisible(false);
    	editer.setVisible(true);
        mise_a_jour_oeuvre.setVisible(false);
    	
    	numero_archive_6s_textField.setEditable(false);
		titre_textField.setEditable(false);
		date_oeuvre_textField.setEditable(false);
		dimensions_textField.setEditable(false);
		inscriptions_textArea.setEditable(false);
		degradations_textArea.setEditable(false);
		observations_textArea.setEditable(false);
		remarques_textArea.setEditable(false);
		
		reloadOeuvre();
    	
    }
    
    @FXML
    public void onMiseAJourOeuvreButton(){

    	oeuvreSelectionne.setCote_archives_6s(numero_archive_6s_textField.getText());
    	oeuvreSelectionne.setTitre_de_l_oeuvre(titre_textField.getText());
    	oeuvreSelectionne.setDate(date_oeuvre_textField.getText());
    	oeuvreSelectionne.setDimensions(dimensions_textField.getText());
    	oeuvreSelectionne.setInscriptions_au_verso(inscriptions_textArea.getText());
    	oeuvreSelectionne.setAuteur(Messages.getAuteurs_id().get(auteursChoiceBox.getSelectionModel().getSelectedItem()));
    	
    	oeuvreTraiteeSelectionne.setAlterations(new ArrayList(Arrays.asList(degradations_textArea.getText().split(System.getProperty("line.separator")))));
    	oeuvreTraiteeSelectionne.setObservations(observations_textArea.getText());
    	oeuvreTraiteeSelectionne.setRemarques(remarques_textArea.getText());
    	oeuvreTraiteeSelectionne.setEtat(etat_final_choiceBox.getSelectionModel().getSelectedItem());
    	oeuvreTraiteeSelectionne.setComplement_etat(complement_etat_textArea.getText());
    	
    	

		OeuvreTraitee.update(oeuvreTraiteeSelectionne);
		Oeuvre.update(oeuvreSelectionne);
		
		onAnnulerEditButton();

    	
    }


    @FXML
    public void onVersOeuvreButton(){}
    @FXML
    public void onVersFichiersButton(){}
    @FXML
    public void onExporter_rapport_button(){
    	
    	FreeMarkerMaker.odt2pdf(oeuvreSelectionne, oeuvreTraiteeSelectionne);
    };
    
    
    public void afficherOeuvres(){
    	
    	ObservableList<OeuvreTraitee> obs_oeuvres;
    	
    	if (Messages.getObservablOeuvresTraitees() == null){
    		oeuvresTraitees = new ArrayList<>();
    		
    		oeuvresTraitees = Messages.getOeuvresTraitees_id()
                                      .values()
                                      .stream()
                                      .map(a -> MongoAccess.request("oeuvreTraitee", a).as(OeuvreTraitee.class).next())
                                      .collect(Collectors.toList());
    		
    		obs_oeuvres = FXCollections.observableArrayList(oeuvresTraitees);

    	}
    	else {
    		obs_oeuvres = Messages.getObservablOeuvresTraitees();
    	}

		oeuvres_nom_colonne.setCellValueFactory(new PropertyValueFactory<OeuvreTraitee, String>("nom"));
		oeuvres_fait_colonne.setCellValueFactory(new PropertyValueFactory<OeuvreTraitee, ImageView>("icone_progression"));
		//oeuvres_fait_colonne.setCellValueFactory(new PropertyValueFactory<OeuvreTraitee, String>("fait"));

		tableOeuvre.setItems(obs_oeuvres);
		
		tableOeuvre.getSelectionModel().clearAndSelect(Messages.getOeuvre_index());
		tableOeuvre.getSelectionModel().focus(Messages.getOeuvre_index());
		
	}
    
   public void afficherTechniques(){
	   
	   techniques.clear();
    	
       if (Messages.getTechniques_id() == null){
    	   
    	   System.out.println("Messages.getTechniques_id() == null");
    	   techniques_id = new TreeMap<>();

           techniquesCursor = MongoAccess.request("technique").as(Technique.class);
    		
    		while (techniquesCursor.hasNext()){
    			
    			Technique t = techniquesCursor.next();
    			techniques.add(t.getNom());
    			techniques_id.put(t.getNom(), t.get_id());
    		}
    		Messages.setTechniques_id(techniques_id);
       }
       else {
    	   System.out.println("Messages.getTechniques_id() != null");
    	   
    	   techniques_id = Messages.getTechniques_id();
    	   techniques.addAll(Messages.getTechniques_id().keySet());
       }
       techniques_listView.setItems(techniques);		
	}
    
   public void afficherMatieres(){
   	
       matieres.clear();
       matieres_id = new TreeMap<>();
       
       if (Messages.getMatieres_id() == null){
    	   
    	   matieresCursor = MongoAccess.request("matiere").as(Matiere.class);
   		
   		   while (matieresCursor.hasNext()){
   			   
   			   Matiere m = matieresCursor.next();
   			   matieres.add(m.getNom());
   			   matieres_id.put(m.getNom(), m.get_id());
   		   }
   		   Messages.setMatieres_id(matieres_id);
       }
       else {
    	   matieres_id = Messages.getMatieres_id();
    	   matieres.addAll(matieres_id.keySet());
       }
       
		matieres_listView.setItems(matieres);	
	}
    
    public void afficherFichiers(){
    	
    	observableFichiers.clear();
    	fichiers_id = new TreeMap<>();
    	
    	if (Messages.getFichiers_id() == null){
    		
    		try {
    	    	
    	    	for (ObjectId fichier_id : oeuvreTraiteeSelectionne.getFichiers_id().values()){
    	    		
    	    		Fichier f = MongoAccess.request("fichier", fichier_id).as(Fichier.class).next();
    	    		
    	    		observableFichiers.add(f.getNom());	
    	    		fichiers_id.put(f.getNom(), f.get_id());
    	    	}
    	    	
    	    	observableFichiers.sort(new Comparator<String>() {

    				@Override
    				public int compare(String o1, String o2) {
    				
    					return String.format("%s_%02d", o1.split("\\.")[1], Integer.parseInt(o1.split("\\.")[2]))
    				.compareTo(String.format("%s_%02d", o2.split("\\.")[1], Integer.parseInt(o2.split("\\.")[2])));
    				}
    			});
    	    	Messages.setFichiers_id(fichiers_id);
    	    	
        	}
        	catch (NullPointerException npe ){
        		
        	}
    		
    	}
    	else {
    		observableFichiers.addAll(Messages.getFichiers_id().keySet().stream().map(a -> Normalize.normalizeDenormStringField(a)).collect(Collectors.toList()));
    	}
    	
    	fichiers_listView.setItems(observableFichiers);
    	
    }
    
    @FXML
    public void onTraitementAttenduSelect(){
    	
    	TacheTraitement ttAtt = traitements_attendus_tableView.getSelectionModel().getSelectedItem();
    	Messages.setTacheTraitement(ttAtt);
    	
    	Scene fiche_tache_traitement_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_tache_traitement.fxml"), 1275, 722);
		fiche_tache_traitement_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_tache_traitement_scene);
    	
    }

    @FXML
    public void onFichierSelect(){
    	
    	String f = fichiers_listView.getSelectionModel().getSelectedItem();
    	Fichier fichier = MongoAccess.request("fichier", fichiers_id.get(f)).as(Fichier.class).next();
    	
    	Messages.setFichier(fichier);
    	
    	Scene fiche_fichier_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_fichier.fxml"), 1275, 722);
		fiche_fichier_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_fichier_scene);
    	
    }
    
    @FXML
	public void onMatiereSelect(){
    	
    	String m = matieres_listView.getSelectionModel().getSelectedItem();
	
		oeuvreSelectionne.addMatiere(m, matieres_id.get(m));

		MongoAccess.update("oeuvre", oeuvreSelectionne);

		matieres_hbox.getChildren().clear();
		
		for (String m_ : oeuvreSelectionne.getMatieresUtilisees_names()){
			affichageMatieresUtilises(m);
		}
		
		
	}
    
    @FXML
	public void onTechniqueSelect(){
		
        String t = techniques_listView.getSelectionModel().getSelectedItem();
        
        System.out.println(t);
        System.out.println(techniques_id);
			
		oeuvreSelectionne.addTechnique(t, techniques_id.get(t));

		MongoAccess.update("oeuvre", oeuvreSelectionne);
		
		techniques_hbox.getChildren().clear();
		
		for (String t_ : oeuvreSelectionne.getTechniquesUtilisees_names()){
			affichageTechniquesUtilises(t);
		}
		
	}
    
    public void affichageMatieresUtilises(String m){

		ImageView iv = new ImageView(new Image(Progression.NULL_.getUsedImage()));
		iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);
        iv.setFitWidth(15);
		
		
		Button b = new Button(m);
		Button b2 = new Button("", iv);
		
		b2.setOnAction((event) -> deleteMatiereLie((Button)event.getSource()));

		matieres_hbox.getChildren().add(b);
		matieres_hbox.getChildren().add(b2);
		HBox.setMargin(b2, new Insets(0,10,0,0));
	}
    
    public void deleteMatiereLie(Button e){
		
		int index = matieres_hbox.getChildren().indexOf(e);

		Matiere matiere = MongoAccess.request("matiere", "nom",  ((Button) matieres_hbox.getChildren().get(index -1)).getText()).as(Matiere.class);
		
		matieres_hbox.getChildren().remove(index, index +1);
		
		oeuvreSelectionne.deleteMatiere(matiere.getNom());
		
		OeuvreTraitee.update(oeuvreTraiteeSelectionne);
		Oeuvre.update(oeuvreSelectionne);
		
		matieres_hbox.getChildren().clear();
		for (String m : oeuvreSelectionne.getMatieresUtilisees_names()){
			affichageMatieresUtilises(m);
			
		}
		//affichageMatieresUtilisees();
	}

    

    public void affichageTechniquesUtilises(String t){

		ImageView iv = new ImageView(new Image(Progression.NULL_.getUsedImage()));
		iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);
        iv.setFitWidth(15);
		
		
		Button b = new Button(t);
		Button b2 = new Button("", iv);
		
		b2.setOnAction((event) -> deleteTechniqueLie((Button)event.getSource()));

		techniques_hbox.getChildren().add(b);
		techniques_hbox.getChildren().add(b2);
		HBox.setMargin(b2, new Insets(0,10,0,0));
	}	
    
// pareil pour technique (deleteTechnique liee(), deleteTechnique, getTechniques, affichageTechniqueUtilisées)
    
    public void deleteTechniqueLie(Button e){
		
		int index = techniques_hbox.getChildren().indexOf(e);

		Technique technique = MongoAccess.request("technique", "nom",  ((Button) techniques_hbox.getChildren().get(index -1)).getText()).as(Technique.class);
		
		techniques_hbox.getChildren().remove(index -1, index +1);
		
		oeuvreSelectionne.deleteTechnique(technique.getNom());
		
		OeuvreTraitee.update(oeuvreTraiteeSelectionne);
		Oeuvre.update(oeuvreSelectionne);
		
		techniques_hbox.getChildren().clear();
		for (String t : oeuvreSelectionne.getTechniquesUtilisees_names()){
			affichageTechniquesUtilises(t);	
		}
		//
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		oeuvreSelectionne = Messages.getOeuvre();
		oeuvreTraiteeSelectionne = Messages.getOeuvreTraitee();

		if (Messages.getAuteurs_id() != null){
			auteurs_id = Messages.getAuteurs_id();
		}
		else {
			auteurs_id = new TreeMap<>();
			MongoCursor<Auteur> auteurCursor = MongoAccess.request("auteur").as(Auteur.class);
			
			while (auteurCursor.hasNext()){
				Auteur a = auteurCursor.next();
				auteurs_id.put(a.getNom(), a.get_id());
			}
			Messages.setAuteurs_id(auteurs_id);
		}
		fichiers_id = new TreeMap<>();

		traitementSelectionne = Messages.getTacheTraitement();
		oeuvreSelectionne = Messages.getOeuvre();
		oeuvreTraiteeSelectionne = Messages.getOeuvreTraitee();
		commandeSelectionne = Messages.getCommande();
		auteur = Messages.getAuteur();

		numero_archive_6s_textField.setEditable(false);
		titre_textField.setEditable(false);
		date_oeuvre_textField.setEditable(false);
		dimensions_textField.setEditable(false);
		inscriptions_textArea.setEditable(false);
		degradations_textArea.setEditable(false);
		observations_textArea.setEditable(false);
		remarques_textArea.setEditable(false);

		numero_archive_6s_textField.setText(oeuvreSelectionne.getCote_archives_6s());
		titre_textField.setText(oeuvreSelectionne.getTitre_de_l_oeuvre());
		date_oeuvre_textField.setText(oeuvreSelectionne.getDate());
		dimensions_textField.setText(oeuvreSelectionne.getDimensions());
		inscriptions_textArea.setText(oeuvreSelectionne.getInscriptions_au_verso());
		degradations_textArea.setText(oeuvreSelectionne.get_observations());

        editer.setVisible(true);
        mise_a_jour_oeuvre.setVisible(false);
		annuler.setVisible(false);

		versOeuvreButton.setVisible(false);
		versRapportButton.setVisible(false);
		
		matieres = FXCollections.observableArrayList();
		techniques = FXCollections.observableArrayList();
	
		etatsFinaux = FXCollections.observableArrayList(EtatFinal.values());
		etat_final_choiceBox.setItems(etatsFinaux);
	
		traitementsAttendus = FXCollections.observableArrayList();
		observableFichiers = FXCollections.observableArrayList();
		
		oeuvresTraitees = new ArrayList<OeuvreTraitee>();

		
		currentStage = Messages.getStage();
		
		complement_etat_textArea.textProperty().addListener((observable, oldValue, newValue) -> {
			onEditerOeuvreButton();
		});
		
        auteursChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			
			System.out.println(oldValue);
			System.out.println(newValue);
			
			if(newValue != null && ! newValue.equals(auteur.getNom())){
				
				System.out.println(auteurs_id);
				System.out.println(auteurs_id.get(newValue));
				
				auteur = MongoAccess.request("auteur", auteurs_id.get(newValue)).as(Auteur.class).next();
				
				Messages.setAuteur(auteur);
				onEditerOeuvreButton();
			}	
		});
		
		etat_final_choiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			onEditerOeuvreButton();
		});

		afficherOeuvres();
		afficherMatieres();
		afficherTechniques();
		reloadOeuvre();

	}
}

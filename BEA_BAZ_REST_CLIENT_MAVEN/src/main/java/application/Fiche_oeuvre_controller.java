package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.type.TypeReference;

import enums.EtatFinal;
import enums.Progression;
import utils.FreeMarkerMaker;
import utils.JsonUtils;
import utils.RestAccess;
import utils.RestObjectMapper;
import models.Auteur;
import models.Commande;
import models.Contexte;
import models.Fichier;
import models.Matiere;
import models.Messages;
import models.Oeuvre;
import models.OeuvreTraitee;
import models.TacheTraitement;
import models.Technique;
import models.Traitement;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
	private TableView<Map<String, String>> tableOeuvre;
	@FXML
	private TableColumn<Map<String, String>, String> oeuvres_nom_colonne;
	@FXML
	private TableColumn<Map<String, String>, ImageView> oeuvres_fait_colonne;
	//private TableColumn<OeuvreTraitee, String> oeuvres_fait_colonne;

	private List<Map<String, String>> oeuvresTraitees;
	
	private boolean edit = false;
	private Oeuvre oeuvreSelectionne;
	private OeuvreTraitee oeuvreTraiteeSelectionne;
	private List<TacheTraitement> traitementCursor ;
	private ObservableList<TacheTraitement> traitementsAttendus;
	private TacheTraitement traitementSelectionne;
	private Commande commandeSelectionne;
	
	private Stage currentStage;
	private Auteur auteur;
	
	boolean directSelect = false;
	
	private List<OeuvreTraitee> oeuvresTraiteesCursor;
	
	private ObservableList<String> matieres;
	private ObservableList<String> techniques;
	private ObservableList<String> fichiers;
	private ObservableList<String> auteurs;
	
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
		
		Scene fiche_produit_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_produit.fxml"), Contexte.largeurFenetre, Contexte.hauteurFenetre);
		fiche_produit_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_produit_scene);	
	}
	@FXML
    public void onVersTraitementsButton(){
    	Scene fiche_traitement_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_traitement.fxml"), Contexte.largeurFenetre, Contexte.hauteurFenetre);
		fiche_traitement_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_traitement_scene);
    }
    @FXML
    public void onVersModelesButton(){
    	Scene fiche_model_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_model.fxml"), Contexte.largeurFenetre, Contexte.hauteurFenetre);
		fiche_model_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_model_scene);
    }
    @FXML
    public void onVersAuteursButton(){
    	Scene fiche_auteur_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_auteur.fxml"), Contexte.largeurFenetre, Contexte.hauteurFenetre);
		fiche_auteur_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_auteur_scene);
    }
    @FXML
    public void onMatieres_button(){
    	Scene fiche_matiere_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_matiere.fxml"), Contexte.largeurFenetre, Contexte.hauteurFenetre);
		fiche_matiere_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_matiere_scene);
    }
    @FXML
    public void onTechniques_button(){
    	Scene fiche_technique_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_technique.fxml"), Contexte.largeurFenetre, Contexte.hauteurFenetre);
		fiche_technique_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_technique_scene);
    }
    
    @FXML
    public void onVersClientButton(){
    	Scene fiche_client_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_client.fxml"), Contexte.largeurFenetre, Contexte.hauteurFenetre);
		fiche_client_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_client_scene);
    }
	@FXML
	public void onVersCommandeButton(){
		
		Scene fiche_commande_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_commande.fxml"), Contexte.largeurFenetre, Contexte.hauteurFenetre);
		fiche_commande_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_commande_scene);	
	}	
	
	@FXML
	public void onOeuvreSelect(){
	    
		oeuvreTraiteeSelectionne = (OeuvreTraitee) tableOeuvre.getSelectionModel().getSelectedItem();
		Messages.setOeuvreTraitee(oeuvreTraiteeSelectionne);
		
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
		
		matieresUtilisees = oeuvreSelectionne.getMatieresUtilisees_names();
		techniquesUtilisees = oeuvreSelectionne.getTechniquesUtilisees_names();
 
		if (directSelect){
		   tableOeuvre.scrollTo(tableOeuvre.getSelectionModel().getSelectedIndex() -9);
		  // tableOeuvre.getSelectionModel().clearAndSelect(Messages.getOeuvre_index());
		  // tableOeuvre.getSelectionModel().focus(Messages.getOeuvre_index());
		}
		else {
			tableOeuvre.scrollTo(tableOeuvre.getSelectionModel().getSelectedIndex());
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
		
		if (oeuvreSelectionne.getMatieresUtilisees_names() != null){
			for (String m : oeuvreSelectionne.getMatieresUtilisees_names()){
				affichageMatieresUtilises(m);
			}
		}
		
		if (oeuvreSelectionne.getTechniquesUtilisees_names() != null){
			for (String t : oeuvreSelectionne.getTechniquesUtilisees_names()){
				affichageTechniquesUtilises(t);
			}
		}
		
		String fichierSelectionne_str = RestAccess.request("fichier", "nom", oeuvreSelectionne.getCote_archives_6s(), true);
		
		Fichier fichierSelectionne = Fichier.fromJson(fichierSelectionne_str);
		
		
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
		
//		String auteur_str = RestAccess.request("auteur", "nom", oeuvreSelectionne.getAuteur());
//
//		auteur = Auteur.fromJson(auteur_str);
//		
//		
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
//				auteur = RestAccess.request("auteur", auteurs_id.get(newValue)).as(Auteur.class).next();
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
				
		for (String tt : oeuvreTraiteeSelectionne.getTraitementsAttendus_names()){
			
			System.out.println( "tt = " + tt);
			//traitementsAttendus.add(TacheTraitement.retrouveTacheTraitement(tt));	
			
			traitementsAttendus.add(RestObjectMapper.retrouveObjet("tacheTraitement", new ObjectId(tt), TacheTraitement.class));
		}

		traitements_attendus_tableColumn.setCellValueFactory(new PropertyValueFactory<TacheTraitement, String>("nom"));
		faits_attendus_tableColumn.setCellValueFactory(new PropertyValueFactory<TacheTraitement, ImageView>("icone_progression"));
		traitements_attendus_tableView.setItems(traitementsAttendus);

	}
	
    public void afficherAuteurs(){
    	
    	int index = 0;
    	
    	auteurs.clear();
    	auteurs.add(null);
        matieres.addAll(Arrays.asList(Auteur.retrouveAuteurs())
                .stream()
                .map(a -> a.getNom())
                .collect(Collectors.toList()));
         
 	   auteursChoiceBox.setItems(auteurs);	

//		for (Auteur auteur  : Auteur.retrouveAuteurs()){
//			if (auteur != null && auteur.getNom().equals(oeuvreSelectionne.getAuteur())){
//				index ++;
//				break;
//			}
//		}	
//
//		auteursChoiceBox.getSelectionModel().select(index);
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
    	oeuvreSelectionne.setAuteur(auteursChoiceBox.getSelectionModel().getSelectedItem());
    	
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
    
    public Comparator<OeuvreTraitee> otTrieeParNom = 
    		(OeuvreTraitee o1, OeuvreTraitee o2)-> o1.getNom().compareTo(o2.getNom());
    
    public void afficherOeuvres(){
		
		ObservableList<Map<String, String>> liste_oeuvres = FXCollections.observableArrayList();

		
		liste_oeuvres = FXCollections.observableArrayList(commandeSelectionne.getOeuvresTraitees());
	
		oeuvres_nom_colonne.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("oeuvresTraitee_string").toString()));
		oeuvres_fait_colonne.setCellValueFactory(data -> new SimpleObjectProperty<ImageView>(getImageView(data)));
    	
		tableOeuvre.setItems(liste_oeuvres);
		
	}
    
    public ImageView getImageView(CellDataFeatures<Map<String, String>, ImageView> data){
    	
    	ImageView imv = new ImageView();
    	imv.setFitWidth(20);
    	imv.setFitHeight(20);
    	imv.setImage(new Image(Progression.valueOf(data.getValue().get("oeuvresTraitee_etat").toString()).getUsedImage()));
    	
    	return imv; 	
    }
    
   public void afficherTechniques(){
	   
	   techniques.clear();
	   
       techniques.addAll(Arrays.asList(Technique.retrouveTechniques())
    		                   .stream()
                               .map(a -> a.getNom())
                               .collect(Collectors.toList()));
       
       techniques_listView.setItems(techniques);		
	}
    
   public void afficherMatieres(){
   	
       matieres.clear();
       matieres.addAll(Arrays.asList(Matiere.retrouveMatieres())
               .stream()
               .map(a -> a.getNom())
               .collect(Collectors.toList()));
        
	   matieres_listView.setItems(matieres);	
	}
    
    public void afficherFichiers(){
    	
    	fichiers.clear();
    	fichiers.addAll(Arrays.asList(Fichier.retrouveFichiers())
                .stream()
                .map(a -> a.getNom())
                .collect(Collectors.toList()));
    	
    	fichiers_listView.setItems(fichiers);
    	
    	fichiers.sort(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
			
				return String.format("%s_%02d", o1.split("\\.")[1], Integer.parseInt(o1.split("\\.")[2]))
			.compareTo(String.format("%s_%02d", o2.split("\\.")[1], Integer.parseInt(o2.split("\\.")[2])));
			}
		});
    	
    	//fichiers.addAll(fichiers.stream().map(a -> Normalize.normalizeDenormStringField(a)).collect(Collectors.toList()));

    	
    	
    }
    
    @FXML
    public void onTraitementAttenduSelect(){
    	
    	TacheTraitement ttAtt = traitements_attendus_tableView.getSelectionModel().getSelectedItem();
    	Messages.setTacheTraitement(ttAtt);
    	
    	Scene fiche_tache_traitement_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_tache_traitement.fxml"), Contexte.largeurFenetre, Contexte.hauteurFenetre);
		fiche_tache_traitement_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_tache_traitement_scene);
    	
    }

    @FXML
    public void onFichierSelect(){
    	
    	String f = fichiers_listView.getSelectionModel().getSelectedItem();
    	Fichier fichier = Fichier.retrouveFichier(f);
    	
    	Messages.setFichier(fichier);
    	
    	Scene fiche_fichier_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_fichier.fxml"), Contexte.largeurFenetre, Contexte.hauteurFenetre);
		fiche_fichier_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_fichier_scene);
    	
    }
    
    @FXML
	public void onMatiereSelect(){
    	
    	String m = matieres_listView.getSelectionModel().getSelectedItem();
    	Matiere matiere = Matiere.retrouveMatiere(m);
		oeuvreSelectionne.addMatiereUtilisee(matiere);

		RestAccess.update("oeuvre", oeuvreSelectionne);

		matieres_hbox.getChildren().clear();
		
		for (String m_ : oeuvreSelectionne.getMatieresUtilisees_names()){
			affichageMatieresUtilises(m);
		}
		
		
	}
    
    @FXML
	public void onTechniqueSelect(){
		
        String t = techniques_listView.getSelectionModel().getSelectedItem();
			
		oeuvreSelectionne.addTechniqueUtilisee(Technique.retrouveTechnique(t));

		RestAccess.update("oeuvre", oeuvreSelectionne);
		
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

		Matiere matiere = Matiere.retrouveMatiere(((Button) matieres_hbox.getChildren().get(index -1)).getText());
		
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

		Technique technique = Technique.retrouveTechnique((((Button) techniques_hbox.getChildren().get(index -1)).getText()));
		
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
		
		
		
		oeuvreTraiteeSelectionne = Messages.getOeuvreTraitee();
		System.out.println("oeuvreTraiteeSelectionne : " + oeuvreTraiteeSelectionne); // retourne null : pourquoi ?
		
		oeuvreSelectionne = oeuvreTraiteeSelectionne.getOeuvre(); // retourne null : pourquoi ?
		System.out.println("oeuvreSelectionne :" + oeuvreSelectionne); 
		
		commandeSelectionne = oeuvreTraiteeSelectionne.getCommande();
		auteur = oeuvreSelectionne.getAuteur_obj();

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
		auteurs = FXCollections.observableArrayList();
		traitementsAttendus = FXCollections.observableArrayList();
	
		etatsFinaux = FXCollections.observableArrayList(EtatFinal.values());
		etat_final_choiceBox.setItems(etatsFinaux);
	
		traitementsAttendus = FXCollections.observableArrayList();
		fichiers = FXCollections.observableArrayList();
		
		oeuvresTraitees = new ArrayList<Map<String, String>>();

		
		currentStage = Messages.getStage();
		
		complement_etat_textArea.textProperty().addListener((observable, oldValue, newValue) -> {
			onEditerOeuvreButton();
		});
		
        auteursChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			
			System.out.println(oldValue);
			System.out.println(newValue);
			
			if(newValue != null && ! newValue.equals(auteur.getNom())){
				auteur = Auteur.retrouveAuteur(newValue);
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

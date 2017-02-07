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

public class Fiche_oeuvre_controller extends Fiche_controller implements Initializable{

	@FXML
	private ListView<TacheTraitement> traitements_attendus_all_tableView;
	@FXML
	private TableColumn<TacheTraitement, String> traitements_attendus_tableColumn;
	@FXML
	private TableColumn<TacheTraitement, ImageView> faits_attendus_tableColumn;
	@FXML
	private TableView<TacheTraitement> traitements_attendus_tableView;
	
	@FXML
	private ListView<String> fichiers_listView;
	
	@FXML
	private Polygon precedent_fleche;
	@FXML
	private Polygon suivant_fleche;
	
	@FXML
	private Label fiche_oeuvre_label;
	@FXML
	private Label nom_oeuvre_label;
	
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
	private ListView<String> matieres_all_listView;
	@FXML
	private ListView<String> techniques_all_listView;
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

	private List<Map<String, String>> oeuvresTraitees;

	private Oeuvre oeuvreSelectionneObj;
	private OeuvreTraitee oeuvreTraiteeSelectionneObj;
	private Map<String, String> oeuvreTraiteeSelectionne;

	private ObservableList<TacheTraitement> traitementsAttendus;
	private TacheTraitement traitementSelectionne;
	private Commande commandeSelectionne;

	private Auteur auteur;
	
	boolean directSelect = false;
	
	private ObservableList<String> matieres;
	private ObservableList<String> techniques;
	private ObservableList<String> fichiers;
	
	private Set<String> matieresUtilisees;
	private Set<String> techniquesUtilisees;
	
	private Matiere matiere;
	private Technique technique;

	
	private ObservableList<EtatFinal> etatsFinaux;	
	
	@FXML
	public void onOeuvreSelect(){	
		directSelect = true;
		reloadOeuvre();	
	}

	public void reloadOeuvre(){
		
		oeuvreTraiteeSelectionneObj = OeuvreTraitee.retrouveOeuvreTraitee(new ObjectId(tableOeuvre.getSelectionModel().getSelectedItem().get("oeuvresTraitee_id")));
    	Messages.setOeuvreTraiteeObj(oeuvreTraiteeSelectionneObj);

		oeuvreSelectionneObj = oeuvreTraiteeSelectionneObj.getOeuvre();
		
		matieresUtilisees = oeuvreSelectionneObj.getMatieresUtilisees_names();
		techniquesUtilisees = oeuvreSelectionneObj.getTechniquesUtilisees_names();
 
		if (directSelect){
		   tableOeuvre.scrollTo(tableOeuvre.getSelectionModel().getSelectedIndex() -9);
		}
		else {
			tableOeuvre.scrollTo(tableOeuvre.getSelectionModel().getSelectedIndex());
		}
		
		numero_archive_6s_textField.setText(oeuvreSelectionneObj.getCote_archives_6s());
		titre_textField.setText(oeuvreSelectionneObj.getTitre_de_l_oeuvre());

		date_oeuvre_textField.setText(oeuvreSelectionneObj.getDate());
		dimensions_textField.setText(oeuvreSelectionneObj.getDimensions());
		inscriptions_textArea.setText(oeuvreSelectionneObj.getInscriptions_au_verso());
		degradations_textArea.setText(oeuvreTraiteeSelectionneObj.getAlterations().stream()
				                                                               .map(o -> o.replace("oui/non", ""))
				                                                               .collect(Collectors.joining("\n")));
		observations_textArea.setText(oeuvreTraiteeSelectionneObj.getObservations());
		remarques_textArea.setText(oeuvreTraiteeSelectionneObj.getRemarques());
		
		if (oeuvreSelectionneObj.getMatieresUtilisees_names() != null){
			for (String m : oeuvreSelectionneObj.getMatieresUtilisees_names()){
				affichageMatieresUtilises(m);
			}
		}
		
		if (oeuvreSelectionneObj.getTechniquesUtilisees_names() != null){
			for (String t : oeuvreSelectionneObj.getTechniquesUtilisees_names()){
				affichageTechniquesUtilises(t);
			}
		}
		
		String fichierSelectionne_str = RestAccess.request("fichier", "nom", oeuvreSelectionneObj.getCote_archives_6s(), true);
		
		Fichier fichierSelectionne = Fichier.fromJson(fichierSelectionne_str);
		
		preview_imageView.setImage(new Image(String.format("file:%s" ,fichierSelectionne.getFichierLie().toString())));
		
		etat_final_choiceBox.getSelectionModel().select(oeuvreTraiteeSelectionneObj.getEtat());
		complement_etat_textArea.setText(oeuvreTraiteeSelectionneObj.getComplement_etat());
		nom_oeuvre_label.setText(oeuvreSelectionneObj.getNom());
	
		afficherTraitements();
		afficherFichiers();	
		afficherAuteurs();		
	}
	
	public void afficherTraitements(){
		
		traitementsAttendus.clear();
				
		for (String tt : oeuvreTraiteeSelectionneObj.getTraitementsAttendus_names()){

			//traitementsAttendus.add(TacheTraitement.retrouveTacheTraitement(tt));	
			
			traitementsAttendus.add(RestObjectMapper.retrouveObjet("tacheTraitement", new ObjectId(tt), TacheTraitement.class));
		}

		traitements_attendus_tableColumn.setCellValueFactory(new PropertyValueFactory<TacheTraitement, String>("nom"));
		faits_attendus_tableColumn.setCellValueFactory(new PropertyValueFactory<TacheTraitement, ImageView>("icone_progression"));
		traitements_attendus_all_tableView.setItems(traitementsAttendus);

	}
	
    public void afficherAuteurs(){
    	
    	Contexte.getFiche_commande_controller().afficherAuteurs();
    	auteursChoiceBox.setItems(observableAuteurs);
    	auteursChoiceBox.getSelectionModel().select(auteur.getNom());
	}

    @FXML
    public void onEditerOeuvreButton(){
    	super.onEditerButton();
    	editability(true);
    }
    
    @FXML
    public void onAnnulerEditButton(){
    	super.onAnnulerEditButton();
    	editability(false);
		
		reloadOeuvre();
    	
    }
    
    @FXML
    public void onMiseAJourOeuvreButton(){
    	super.onMiseAJourButton();
    	editability(false);

    	oeuvreSelectionneObj.setCote_archives_6s(numero_archive_6s_textField.getText());
    	oeuvreSelectionneObj.setTitre_de_l_oeuvre(titre_textField.getText());
    	oeuvreSelectionneObj.setDate(date_oeuvre_textField.getText());
    	oeuvreSelectionneObj.setDimensions(dimensions_textField.getText());
    	oeuvreSelectionneObj.setInscriptions_au_verso(inscriptions_textArea.getText());
    	oeuvreSelectionneObj.setAuteur(auteursChoiceBox.getSelectionModel().getSelectedItem());
    	
    	oeuvreTraiteeSelectionneObj.setAlterations(new ArrayList(Arrays.asList(degradations_textArea.getText().split(System.getProperty("line.separator")))));
    	oeuvreTraiteeSelectionneObj.setObservations(observations_textArea.getText());
    	oeuvreTraiteeSelectionneObj.setRemarques(remarques_textArea.getText());
    	oeuvreTraiteeSelectionneObj.setEtat(etat_final_choiceBox.getSelectionModel().getSelectedItem());
    	oeuvreTraiteeSelectionneObj.setComplement_etat(complement_etat_textArea.getText());

		OeuvreTraitee.update(oeuvreTraiteeSelectionneObj);
		Oeuvre.update(oeuvreSelectionneObj);
		
		onAnnulerEditButton();

    	
    }


    @FXML
    public void onVersOeuvreButton(){}
    @FXML
    public void onVersFichiersButton(){}
    @FXML
    public void onExporter_rapport_button(){
    	
    	FreeMarkerMaker.odt2pdf(oeuvreSelectionneObj, oeuvreTraiteeSelectionneObj);
    };
    
    public Comparator<OeuvreTraitee> otTrieeParNom = 
    		(OeuvreTraitee o1, OeuvreTraitee o2)-> o1.getNom().compareTo(o2.getNom());
    
    public void afficherOeuvres(){

    	Contexte.getFiche_commande_controller().afficherOeuvres();
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
    
   public void afficherTechniques(){
	   
	   techniques.clear();
	   
       techniques.addAll(Arrays.asList(Technique.retrouveTechniques())
    		                   .stream()
                               .map(a -> a.getNom())
                               .collect(Collectors.toList()));
       
       techniques_all_listView.setItems(techniques);		
	}
    
   public void afficherMatieres(){
   	
       matieres.clear();
       matieres.addAll(Arrays.asList(Matiere.retrouveMatieres())
               .stream()
               .map(a -> a.getNom())
               .collect(Collectors.toList()));
        
	   matieres_all_listView.setItems(matieres);	
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
    	
    	TacheTraitement ttAtt = traitements_attendus_all_tableView.getSelectionModel().getSelectedItem();
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
    	
    	String m = matieres_all_listView.getSelectionModel().getSelectedItem();
    	Matiere matiere = Matiere.retrouveMatiere(m);
		oeuvreSelectionneObj.addMatiereUtilisee(matiere);

		RestAccess.update("oeuvre", oeuvreSelectionneObj);

		//matieres_hbox.getChildren().clear();
		
		for (String m_ : oeuvreSelectionneObj.getMatieresUtilisees_names()){
			affichageMatieresUtilises(m);
		}
		
		
	}
    
    @FXML
	public void onTechniqueSelect(){
		
        String t = techniques_all_listView.getSelectionModel().getSelectedItem();
			
		oeuvreSelectionneObj.addTechniqueUtilisee(Technique.retrouveTechnique(t));

		RestAccess.update("oeuvre", oeuvreSelectionneObj);
		
		//techniques_hbox.getChildren().clear();
		
		for (String t_ : oeuvreSelectionneObj.getTechniquesUtilisees_names()){
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

//		matieres_hbox.getChildren().add(b);
//		matieres_hbox.getChildren().add(b2);
		HBox.setMargin(b2, new Insets(0,10,0,0));
	}
    
    public void deleteMatiereLie(Button e){
		
//		int index = matieres_hbox.getChildren().indexOf(e);
//
//		Matiere matiere = Matiere.retrouveMatiere(((Button) matieres_hbox.getChildren().get(index -1)).getText());
//		
//		matieres_hbox.getChildren().remove(index, index +1);
		
		oeuvreSelectionneObj.deleteMatiere(matiere.getNom());
		
		OeuvreTraitee.update(oeuvreTraiteeSelectionneObj);
		Oeuvre.update(oeuvreSelectionneObj);
		
//		matieres_hbox.getChildren().clear();
		for (String m : oeuvreSelectionneObj.getMatieresUtilisees_names()){
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

//		techniques_hbox.getChildren().add(b);
//		techniques_hbox.getChildren().add(b2);
		HBox.setMargin(b2, new Insets(0,10,0,0));
	}	
    
// pareil pour technique (deleteTechnique liee(), deleteTechnique, getTechniques, affichageTechniqueUtilisées)
    
    public void deleteTechniqueLie(Button e){
		
//		int index = techniques_hbox.getChildren().indexOf(e);
//
//		Technique technique = Technique.retrouveTechnique((((Button) techniques_hbox.getChildren().get(index -1)).getText()));
//		
//		techniques_hbox.getChildren().remove(index -1, index +1);
		
		oeuvreSelectionneObj.deleteTechnique(technique.getNom());
		
		OeuvreTraitee.update(oeuvreTraiteeSelectionneObj);
		Oeuvre.update(oeuvreSelectionneObj);
		
//		techniques_hbox.getChildren().clear();
		for (String t : oeuvreSelectionneObj.getTechniquesUtilisees_names()){
			affichageTechniquesUtilises(t);	
		}
		//
	}
    
    public void editability(boolean bool){
		numero_archive_6s_textField.setEditable(bool);
		titre_textField.setEditable(bool);
		date_oeuvre_textField.setEditable(bool);
		dimensions_textField.setEditable(bool);
		inscriptions_textArea.setEditable(bool);
		degradations_textArea.setEditable(bool);
		observations_textArea.setEditable(bool);
		remarques_textArea.setEditable(bool);
		
		prompt(bool);
    }
    
    public void raz(){
    	numero_archive_6s_textField.setText("");
		titre_textField.setText("");
		date_oeuvre_textField.setText("");
		dimensions_textField.setText("");
		inscriptions_textArea.setText("");
		degradations_textArea.setText("");
		observations_textArea.setText("");
		remarques_textArea.setText("");
    }
    
    public void prompt(boolean bool){
    	
    	if (bool){
    		numero_archive_6s_textField.setPromptText("numéro d'inventaire (devrait être présent après l'import)");
    		titre_textField.setPromptText("titre de l'oeuvre (devrait être présent après l'import)");
    		date_oeuvre_textField.setPromptText("Date de l'oeuvre");
    		dimensions_textField.setPromptText("Dimensions de l'oeuvre");
    		inscriptions_textArea.setPromptText("Inscriptions");
    		degradations_textArea.setPromptText("Dégradations");
    		observations_textArea.setPromptText("Observations");
    		remarques_textArea.setPromptText("éventuelles remarques");
    	}
    	else {
    		numero_archive_6s_textField.setPromptText(null);
    		titre_textField.setPromptText(null);
    		date_oeuvre_textField.setPromptText(null);
    		dimensions_textField.setPromptText(null);
    		inscriptions_textArea.setPromptText(null);
    		degradations_textArea.setPromptText(null);
    		observations_textArea.setPromptText(null);
    		remarques_textArea.setPromptText(null);
    	}
    }	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.init();
        editability(false);
        
        versOeuvreButton.setVisible(false);
        versOeuvreButton.setVisible(false);
		versRapportButton.setVisible(false);
		
		oeuvreTraiteeSelectionneObj = Messages.getOeuvreTraiteeObj();
		
		oeuvreSelectionneObj = oeuvreTraiteeSelectionneObj.getOeuvre();
		
		commandeSelectionne = oeuvreTraiteeSelectionneObj.getCommande();
		auteur = oeuvreSelectionneObj.getAuteur_obj();
		
		nom_oeuvre_label.setText(oeuvreSelectionneObj.getNom());

		numero_archive_6s_textField.setText(oeuvreSelectionneObj.getCote_archives_6s());
		titre_textField.setText(oeuvreSelectionneObj.getTitre_de_l_oeuvre());
		date_oeuvre_textField.setText(oeuvreSelectionneObj.getDate());
		dimensions_textField.setText(oeuvreSelectionneObj.getDimensions());
		inscriptions_textArea.setText(oeuvreSelectionneObj.getInscriptions_au_verso());
		degradations_textArea.setText(oeuvreSelectionneObj.get_observations());

		matieres = FXCollections.observableArrayList();
		techniques = FXCollections.observableArrayList();
		traitementsAttendus = FXCollections.observableArrayList();
	
		etatsFinaux = FXCollections.observableArrayList(EtatFinal.values());
		etat_final_choiceBox.setItems(etatsFinaux);
	
		traitementsAttendus = FXCollections.observableArrayList();
		fichiers = FXCollections.observableArrayList();
		
		oeuvresTraitees = new ArrayList<Map<String, String>>();
		
		complement_etat_textArea.textProperty().addListener((observable, oldValue, newValue) -> {
			onEditerOeuvreButton();
		});
		
//        auteursChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
//			
//			if(newValue != null && ! newValue.equals(auteur.getNom())){
//				auteur = Auteur.retrouveAuteur(newValue);
//				onEditerOeuvreButton();
//			}	
//		});
		
		etat_final_choiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			onEditerOeuvreButton();
		});

		afficherOeuvres();
		afficherMatieres();
		afficherTechniques();
		afficherAuteurs();
		//reloadOeuvre();

	}
}

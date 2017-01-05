package application;

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

public class Fiche_tache_traitement_controller  implements Initializable{

	@FXML
	private TableView<TacheTraitement> traitements_associes_tableView;
	@FXML
	private TableColumn<TacheTraitement, String> traitements_associes_tableColumn;
	@FXML
	private TableColumn<TacheTraitement, ImageView> traitements_associes_faits_tableColumn;
	@FXML
	private TextField file_path_textField;
	@FXML
	private ListView<String> listView_produits;
	@FXML
	private Button nouveau_detail;
	@FXML
	private Button mise_a_jour_traitement;
	@FXML
	private Button annuler;
	@FXML
	private Button editer;
	@FXML
	private Button versCommandeButton;
	@FXML
	private Button versTraitementsButton;
	@FXML
	private Button versOeuvreButton;
	@FXML
	private Button versRapportButton;
	
	@FXML
	private Label fiche_traitement_label;
	@FXML
	private Label nom_traitement_label;
	@FXML
	private Label ot_label;
	@FXML
	private Label t_label;
	@FXML
	private Label commande_label;
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
	private Label produitUtiliseLabel;
	@FXML
	private Label complementTraitementLabel;
	
	@FXML
	private HBox produitsLiesHbox;
	
	@FXML
	private ListView<String> tous_les_traitements_listView;
	private ObservableList<String> liste_traitements;
	
	private ObservableList<String> liste_produits;
	
	private boolean edit = false;

	private MongoCursor<TacheTraitement> traitementCursor;
	private MongoCursor<Traitement> tousLesTraitementsCursor;
	private Map<String, ObjectId> tousLesTraitements_id;
	
	
	private TacheTraitement tacheTraitementSelectionne;
	private Traitement traitementSelectionne;
	private String produitSelectionne;
	
	private ArrayList<TacheTraitement> liste_tachesTraitements;
	private ObservableList<TacheTraitement> observable_liste_tachestraitements_lies;
	private Map<String, ObjectId> tacheTraitements_id;
	private TacheTraitement tt_enplus; 

	
	private Progression progres;
	
	private Stage currentStage;

	private Produit detail;
	
	private Traitement traitementSource;
	private OeuvreTraitee ot;
	private String commande;
	
	private File file;
	
	private int selectedIndex;
	
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
    public void onVersFichiersButton(){}
	@FXML
    public void onVersModelesButton(){
    	Scene fiche_model_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_model.fxml"), 1275, 722);
		fiche_model_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_model_scene);
    }
	
	@FXML
	public void onVersCommandeButton(){
		
		Scene fiche_commande_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_commande.fxml"), 1275, 722);
		fiche_commande_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_commande_scene);	
	}	

	
	@FXML
	public void onProduitSelect(){
		
        produitSelectionne = listView_produits.getSelectionModel().getSelectedItem();
		
//		if (produitSelectionne != null && ! tacheTraitementSelectionne.getProduitsLies_names().contains(produitSelectionne)){
//			
//			Messages.setNom_produit(produitSelectionne);
//			
//			if (Messages.getProduits_id() == null){
//				
//				Map<String, ObjectId> produits = new TreeMap<>();
//				MongoCursor<Produit> produit_cursor = MongoAccess.request("produit").as(Produit.class);
//				
//				while (produit_cursor.hasNext()){
//					Produit p = produit_cursor.next();
//					produits.put(p.getNom(), p.get_id());
//				}
//				
//				Messages.setProduits_id(produits);
//			}
//			
//			tacheTraitementSelectionne.addProduit(Messages.getProduits_id().get(produitSelectionne));
//			
//			MongoAccess.update("tacheTraitement", tacheTraitementSelectionne);
//			
//			affichageProduitsUtilises();
//			afficherTraitementsAssocies();
//            
//		}
		
	}
	
    public void affichageProduitsUtilises(){
		
		ImageView iv = new ImageView(new Image(Progression.NULL_.getUsedImage()));
		iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);
        iv.setFitWidth(15);
		
		
		Button b = new Button(produitSelectionne);
		Button b2 = new Button("", iv);
		
		b2.setOnAction((event) -> deleteProduitLie((Button)event.getSource()));

		produitsLiesHbox.getChildren().add(b);
		produitsLiesHbox.getChildren().add(b2);
		HBox.setMargin(b2, new Insets(0,10,0,0));
	}
    
    public void affichageProduitsUtilises(String p){
    	produitSelectionne = p;
    	affichageProduitsUtilises();
    }

    public void deleteProduitLie(Button e){
		
		int index = produitsLiesHbox.getChildren().indexOf(e);
		
		//Produit produit = MongoAccess.request("produit", "nom",  ((Button) produitsLiesHbox.getChildren().get(index -1)).getText()).as(Produit.class);
		
		String texte_produit = ((Button) produitsLiesHbox.getChildren().get(index -1)).getText();
		
		produitsLiesHbox.getChildren().remove(index -1, index +1);
		
		tacheTraitementSelectionne.deleteProduit(texte_produit);
		
		TacheTraitement.update(tacheTraitementSelectionne);
		
		produitsLiesHbox.getChildren().clear();
		for (String p : tacheTraitementSelectionne.getProduitsLies_names()){
			affichageProduitsUtilises(p);
		}
		
	}
    
    public void onAnnulerButton() {
    	
    	mise_a_jour_traitement.setText("Mise à jour");
    	//nom_traitement_textField.setText("");
    	remarques_traitement_textArea.setText("");
    	//nom_traitement_textField.setPromptText("");
    	remarques_traitement_textArea.setPromptText("");
    	rafraichirAffichage();
    	traitements_associes_tableView.getSelectionModel().select(tacheTraitementSelectionne);
    	afficherTraitementsAssocies();
    	
    }
    
    public void rafraichirAffichage(){
    	
    	nom_traitement_label.setText(tacheTraitementSelectionne.getNom());
    	complement_textField.setText(tacheTraitementSelectionne.getComplement());
    	remarques_traitement_textArea.setText(tacheTraitementSelectionne.getRemarques());
    	
    	editer.setVisible(true);
        mise_a_jour_traitement.setVisible(false);
		annuler.setVisible(false);
    	
    }
    
    @FXML
    public void onEditerTraitementButton(){
    	

    	annuler.setVisible(true);
    	editer.setVisible(false);
    	mise_a_jour_traitement.setVisible(true);
    	//nom_traitement_textField.setEditable(true);
		remarques_traitement_textArea.setEditable(true);
		
		edit = true;

	
    }
    
    @FXML
    public void onAnnulerEditButton(){
    	
    	annuler.setVisible(false);
    	editer.setVisible(true);
    	mise_a_jour_traitement.setVisible(false);
		remarques_traitement_textArea.setEditable(false);
		rafraichirAffichage();
		traitements_associes_tableView.getSelectionModel().select(tacheTraitementSelectionne);
		afficherTraitementsAssocies();
    	
    	edit = false;
    	
    }
    
    @FXML
    public void onMiseAJourTraitementButton(){

    	if (tacheTraitementSelectionne == null) {
    		tacheTraitementSelectionne = new TacheTraitement();
    	}
    	
    	tacheTraitementSelectionne.setComplement(complement_textField.getText());
    	tacheTraitementSelectionne.setRemarques(remarques_traitement_textArea.getText());
		
		if (edit) {
			TacheTraitement.update(tacheTraitementSelectionne);
			afficherTraitementsAssocies();
			onAnnulerEditButton();
		}
		else {
			
		   TacheTraitement.save(tacheTraitementSelectionne);
		   afficherTraitementsAssocies();
		   onAnnulerEditButton();
		}
    	
    }

    
    @FXML
    public void onVersOeuvreButton(){
    	Scene fiche_oeuvre_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_oeuvre.fxml"), 1275, 722);
		fiche_oeuvre_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_oeuvre_scene);
    }
    
    @FXML
    public void onAjoutProduit(){
    	
    	//Messages.setTacheTraitementEdited(listView_traitements.getSelectionModel().getSelectedItem());
    	
    	Scene fiche_produit_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_produit.fxml"), 1275, 722);
		fiche_produit_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_produit_scene);
    }
    @FXML
    public void onVersClientButton(){
    	Scene fiche_client_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_client.fxml"), 1275, 722);
		fiche_client_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_client_scene);
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
    	
    	observable_liste_tachestraitements_lies.clear();

//		for (ObjectId tt_id : Messages.getOeuvreTraitee().getTraitementsAttendus_id()){
//			
//			observable_liste_tachestraitements_lies.add(MongoAccess.request("tacheTraitement", tt_id).as(TacheTraitement.class).next());
//		}
//		Messages.setObservableTacheTraitementsLiees(observable_liste_tachestraitements_lies);


		traitements_associes_tableColumn.setCellValueFactory(new PropertyValueFactory<TacheTraitement, String>("nom"));
		traitements_associes_faits_tableColumn.setCellValueFactory(new PropertyValueFactory<TacheTraitement, ImageView>("icone_progression"));
		
		traitements_associes_tableView.setItems(observable_liste_tachestraitements_lies);

		traitements_associes_tableView.getSelectionModel().clearAndSelect(selectedIndex);

		afficherProgression();
    	afficherTraitement();
    	afficherProduits();
    }
    
    public void onTraitementAssocieSelect(){
    	
    	tacheTraitementSelectionne = traitements_associes_tableView.getSelectionModel().getSelectedItem();
    	selectedIndex = traitements_associes_tableView.getSelectionModel().getSelectedIndex();
    	
    	Messages.setTacheTraitement(tacheTraitementSelectionne);
    	
    	afficherTraitementsAssocies();	
    }
    
    public void afficherTraitement(){
    	
    	remarques_traitement_textArea.setEditable(false);
        editer.setVisible(true);
        mise_a_jour_traitement.setVisible(false);
		annuler.setVisible(false);
		fiche_traitement_label.setText("FICHE TRAITEMENT :");
		//nom_traitement_textField.setDisable(true);
		remarques_traitement_textArea.setDisable(true);
		nom_traitement_label.setText(tacheTraitementSelectionne.getNom());

		t_label.setText(tacheTraitementSelectionne.getTraitement().getNom_complet());
		
		produitsLiesHbox.getChildren().clear();
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
    	listView_produits.setItems(liste_produits);
    }
    
    public void onTous_les_traitementsSelect(String traitement){
    	
    	TacheTraitement tt = new TacheTraitement();
    	
    	tt.setFait_(Progression.TODO_);
    	tt.setTraitement_id(tousLesTraitements_id.get(traitement));
    	tt.setNom(traitement);
    	
    	//MongoAccess.save("tacheTraitement", tt);
    	
    	observable_liste_tachestraitements_lies.add(tt);
    	
    	ot.addTraitementAttendu(tt.getNom(), tt.get_id());
    	
    	//MongoAccess.update("oeuvreTraitee", ot);
    	
    	afficherTraitementsAssocies();
    }


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		coche_fait.setImage(new Image(Progression.FAIT_.getUsedImage()));
		coche_todo.setImage(new Image(Progression.TODO_.getUsedImage()));
		coche_so.setImage(new Image(Progression.NULL_.getUsedImage()));
		
		tacheTraitementSelectionne = Messages.getTacheTraitement();

        editer.setVisible(true);
        mise_a_jour_traitement.setVisible(false);
		annuler.setVisible(false);
		
		if(Messages.getCommande() != null){
			versCommandeButton.setVisible(true);
		}
		else {
			versCommandeButton.setVisible(false);
		}
		
		if(Messages.getOeuvreTraitee() != null){
			versOeuvreButton.setVisible(true);
		}
		else {
			versOeuvreButton.setVisible(false);
		}
		versRapportButton.setVisible(false);
		
		versTraitementsButton.setVisible(true);
				
		liste_tachesTraitements = new ArrayList<>();
		
		complement_textField.textProperty().addListener((observable, oldValue, newValue) -> {
			onEditerTraitementButton();
		});
		
		ot = Messages.getOeuvreTraitee();
		//commande = Messages.getCommande();
		traitementSource = tacheTraitementSelectionne.getTraitement();
		
		ot_label.setText(ot.getNom());
		t_label.setText(traitementSource.getNom());
		commande_label.setText(commande);
		
		liste_traitements = FXCollections.observableArrayList();
		liste_produits  = FXCollections.observableArrayList();
		observable_liste_tachestraitements_lies = FXCollections.observableArrayList();
	
		currentStage = Messages.getStage();

//		if (Messages.getTraitements_id() == null){
//			
//			traitementCursor = MongoAccess.request("tacheTraitement").as(TacheTraitement.class);
//			tousLesTraitementsCursor = MongoAccess.request("traitement").as(Traitement.class);
//			
//			tousLesTraitements_id = new TreeMap<>();
//			
//			while (tousLesTraitementsCursor.hasNext()){
//				
//				Traitement t = tousLesTraitementsCursor.next();
//				
//				liste_traitements.add(t.getNom());
//				tousLesTraitements_id.put(t.getNom(), t.get_id());
//			}
//			
//			Messages.setTraitements_id(tousLesTraitements_id);
//		}
//		else {
//			tousLesTraitements_id = Messages.getTraitements_id();
//			liste_traitements.addAll(tousLesTraitements_id.keySet());
//		}
        
		
		tous_les_traitements_listView.setItems(liste_traitements);
		
		tous_les_traitements_listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			onTous_les_traitementsSelect((String) newValue);
		});

		afficherTraitementsAssocies();
		rafraichirAffichage();

	}

}

package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import org.bson.types.ObjectId;
import org.jongo.MongoCursor;

import com.fasterxml.jackson.databind.ObjectMapper;

import utils.RestAccess;
import models.Auteur;
import models.Client;
import models.Commande;
import models.Messages;
import models.Traitement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Fiche_client_controller  implements Initializable{
	
	@FXML
	private ObservableList<String> liste_clients;
	@FXML
	private ObservableList<String> liste_commandes;
	
	@FXML
	private ListView<String> listView_client;
	@FXML
	private ListView<String> listView_commandes;
	@FXML
	private TextField nom_client_textField;
	@FXML
	private TextField nom_complet_client_textField;
	@FXML
	private TextField adresse_voie_textField;
	@FXML
	private TextField adresse_cp_textField;
	@FXML
	private TextField adresse_ville_textField;
	@FXML
	private TextArea remarques_client_textArea;
	@FXML
	private Button nouveau_client;
	@FXML
	private Button nouvelle_commande;
	@FXML
	private Button mise_a_jour_client;
	@FXML
	private Button annuler;
	@FXML
	private Button editer;
	@FXML
	private Button versClientButton;
	@FXML
	private Button versCommandeButton;
	@FXML
	private Button versOeuvreButton;
	@FXML
	private Button versRapportButton;
	@FXML
	private Button versTraitementsButton;
	@FXML
	private Button versModelesButton;
	
	Client[] clientCursor;
	MongoCursor<Commande> commandeCursor ;
	String clientSelectionne;
	String commandeSelectionne;
	
	Map <String, String> clients_id;
	Map <String, String> commandes_id;
	
	Stage currentStage;
	
	Commande commande;
	String client;
	Client c;
	
	ObjectMapper mapper;
	
	private boolean edit = false;
	
	@FXML
	public void onVersProduitsButton(){
		
		Scene fiche_produit_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_produit.fxml"), 1275, 722);
		fiche_produit_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_produit_scene);	
	}
	@FXML
    public void onVersFichiersButton(){}
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
	public void onAjoutCommande(){
		
		Messages.setCommande(null);

		Scene fiche_commande_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_commande.fxml"), 1275, 722);
		fiche_commande_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_commande_scene);
		

	}

	@FXML
	public void onClientSelect(){

		clientSelectionne = listView_client.getSelectionModel().getSelectedItem();
		
        c = Client.retrouveClient(clientSelectionne);

		Messages.setClient(c);
		Messages.setCommande(null);
		Messages.setOeuvre(null);
		Messages.setOeuvreTraitee(null);
		
		affichageInfos();
		
	}
	
	@FXML
	public void onCommandeSelect(){
		
		commandeSelectionne = listView_commandes.getSelectionModel().getSelectedItem();
		
		Commande co = Commande.retrouveCommande(new ObjectId(c.getCommandes_id().get(commandeSelectionne)));
		
		Messages.setCommande(co);
		Messages.setOeuvre(null);
		Messages.setOeuvreTraitee(null);
		
		Scene fiche_commande_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_commande.fxml"), 1275, 722);
		fiche_commande_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		currentStage.setScene(fiche_commande_scene);
		
		
	}
	
    private void affichageInfos(){
    	
    	liste_commandes.clear();
    	
    	if (c != null){
    		
    		nom_client_textField.setText(clientSelectionne);
    		nom_complet_client_textField.setText(c.getNom_complet());
        	adresse_voie_textField.setText(c.getAdresse_rue());
        	adresse_cp_textField.setText(c.getAdresse_cp());
        	adresse_ville_textField.setText(c.getAdresse_ville());
        	remarques_client_textArea.setText(c.getRemarques());
    		
        	commandes_id = c.getCommandes_id(); 
    		liste_commandes.addAll(commandes_id.keySet());
    		
    		listView_commandes.setItems(liste_commandes);
    		
    	}
    	
    	
    }
    
    public void onNouveauClientButton() {
    	
    	mise_a_jour_client.setText("Enregistrer");
    	nom_client_textField.setText("");
    	nom_complet_client_textField.setText("");
    	adresse_voie_textField.setText("");
    	adresse_cp_textField.setText("");
    	adresse_ville_textField.setText("");
    	remarques_client_textArea.setText("");
    	
    	nom_client_textField.setPromptText("saisir le nom affiché dans les menus de l'interface");
    	nom_complet_client_textField.setPromptText("saisir le nom complet pour l'export");
    	adresse_voie_textField.setPromptText("n° dans le voie et nom de la voie");
    	adresse_cp_textField.setPromptText("code postal");
    	adresse_ville_textField.setPromptText("ville");
    	remarques_client_textArea.setPromptText("éventuelles remarques");
    	nouveau_client.setVisible(false);
    	
    	edit = false;
    	annuler.setVisible(true);
    	editer.setVisible(false);
    	mise_a_jour_client.setVisible(true);
    	nom_client_textField.setEditable(true);
    	nom_complet_client_textField.setEditable(true);
    	adresse_voie_textField.setEditable(true);
    	adresse_cp_textField.setEditable(true);
    	adresse_ville_textField.setEditable(true);
		remarques_client_textArea.setEditable(true);
    }
    
    public void onAnnulerButton() {
    	
    	mise_a_jour_client.setText("Mise à jour");
    	nom_client_textField.setText("");
    	remarques_client_textArea.setText("");
    	nom_client_textField.setPromptText("");
    	remarques_client_textArea.setPromptText("");
    	nouveau_client.setText("Nouveau client");
    	rafraichirAffichage();
    	listView_client.getSelectionModel().select(clientSelectionne);
    	affichageInfos();
    }
    
    public void rafraichirAffichage(){
    	
    	liste_clients = FXCollections.observableArrayList();
		liste_commandes  = FXCollections.observableArrayList();
		clients_id.clear();

		clientCursor = Client.retrouveClients();
		
		for (Client client : clientCursor){
			
			liste_clients.add(client.getNom());
			clients_id.put(client.getNom(), client.get_id().toString());
		}
		
		listView_client.setItems(liste_clients);
    	
    }
    
    @FXML
    public void onEditerClientButton(){
    	

    	annuler.setVisible(true);
    	editer.setVisible(false);
    	mise_a_jour_client.setVisible(true);
    	nom_client_textField.setEditable(true);
    	nom_complet_client_textField.setEditable(true);
    	adresse_voie_textField.setEditable(true);
    	adresse_cp_textField.setEditable(true);
    	adresse_ville_textField.setEditable(true);
		remarques_client_textArea.setEditable(true);
		
		
		edit = true;

	
    }
    
    @FXML
    public void onAnnulerEditButton(){
    	
    	annuler.setVisible(false);
    	editer.setVisible(true);
    	mise_a_jour_client.setVisible(false);
    	nom_client_textField.setEditable(false);
    	nom_complet_client_textField.setEditable(false);
    	adresse_voie_textField.setEditable(false);
    	adresse_cp_textField.setEditable(false);
    	adresse_ville_textField.setEditable(false);
		remarques_client_textArea.setEditable(false);
		nouveau_client.setVisible(true);
		rafraichirAffichage();
		listView_client.getSelectionModel().select(clientSelectionne);
    	affichageInfos();
    	
    	edit = false;
    	
    }
    
    @FXML
    public void onMiseAJourClientButton(){
    	
    	if (! edit){
    		c = new Client();
    	}
    	else {
    		c = Client.retrouveClient(clientSelectionne);
    	}
    	
    	c.setNom(nom_client_textField.getText());
    	c.setRemarques(remarques_client_textArea.getText());
    	c.setNom_complet(nom_complet_client_textField.getText());
    	c.setAdresse_rue(adresse_voie_textField.getText());
    	c.setAdresse_cp(adresse_cp_textField.getText());
    	c.setAdresse_ville(adresse_ville_textField.getText());
    	
    	annuler.setVisible(false);
    	editer.setVisible(true);
    	mise_a_jour_client.setVisible(false);
    	nom_client_textField.setEditable(false);
    	nom_complet_client_textField.setEditable(false);
    	adresse_voie_textField.setEditable(false);
    	adresse_cp_textField.setEditable(false);
    	adresse_ville_textField.setEditable(false);
		remarques_client_textArea.setEditable(false);
		
		if (edit) {
			
			Client.update(c);
			rafraichirAffichage();
			onAnnulerEditButton();
		}
		else {
			
		   Client.save(c);
		   onAnnulerEditButton();
		}
    	
    }
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		
		currentStage = Messages.getStage();
		if (Messages.getClient() != null){
			clientSelectionne = Messages.getClient().getNom();
		}
		
		Messages.setCommande(null);
		
		liste_clients = FXCollections.observableArrayList();
		liste_commandes  = FXCollections.observableArrayList();
		
		liste_clients.addAll(Client.retrouveClientsStr());
		
		listView_client.setItems(liste_clients);
		
		if (client != null) {
			listView_client.getSelectionModel().select(liste_clients.indexOf(client));
		}
		else if (liste_clients.size() > 0){
			listView_client.getSelectionModel().select(0);
            clientSelectionne = listView_client.getSelectionModel().getSelectedItem();
			
			Messages.setClient(Client.retrouveClient(clientSelectionne));
		}


		nom_client_textField.setEditable(false);
		nom_complet_client_textField.setEditable(false);
    	adresse_voie_textField.setEditable(false);
    	adresse_cp_textField.setEditable(false);
    	adresse_ville_textField.setEditable(false);
		remarques_client_textArea.setEditable(false);
        editer.setVisible(true);
        mise_a_jour_client.setVisible(false);
		annuler.setVisible(false);
		
		versClientButton.setVisible(false);
		versCommandeButton.setVisible(false);
		versOeuvreButton.setVisible(false);
		versRapportButton.setVisible(false);

		affichageInfos();
	}
}

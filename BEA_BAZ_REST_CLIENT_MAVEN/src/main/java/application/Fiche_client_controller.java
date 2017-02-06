package application;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import org.bson.types.ObjectId;
import org.jongo.MongoCursor;

import com.fasterxml.jackson.databind.ObjectMapper;

import models.Client;
import models.Commande;
import models.Contexte;
import models.Messages;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Fiche_client_controller extends Fiche_controller implements Initializable{
	
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
	private Button nouvelle_commande;
	
	Client[] clientCursor;
	MongoCursor<Commande> commandeCursor ;
	String clientSelectionne;
	String commandeSelectionne;
	
	Map <String, String> clients_id;
	Map <String, String> commandes_id;
	
	Commande commande;
	Client client;
	
	ObjectMapper mapper;	

	@FXML
	public void onAjoutCommande(){
		
		Messages.setCommande(null);

		Scene fiche_commande_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_commande.fxml"), Contexte.largeurFenetre, Contexte.hauteurFenetre);
		fiche_commande_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		currentStage.setScene(fiche_commande_scene);
	}

	@FXML
	public void onClientSelect(){

		clientSelectionne = listView_client.getSelectionModel().getSelectedItem();
		
        client = Client.retrouveClient(clientSelectionne);

		Messages.setClient(client);
		Messages.setCommande(null);
		Messages.setOeuvreTraitee(null);
		
		affichageInfos();
		
	}
	
	@FXML
	public void onCommandeSelect(){
		
		commandeSelectionne = listView_commandes.getSelectionModel().getSelectedItem();
		
		Commande co = Commande.retrouveCommande(new ObjectId(client.getCommandes_id().get(commandeSelectionne)));
		
		Messages.setCommande(co);
		Messages.setOeuvreTraitee(null);
		
		Scene fiche_commande_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_commande.fxml"), Contexte.largeurFenetre, Contexte.hauteurFenetre);
		fiche_commande_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		currentStage.setScene(fiche_commande_scene);
		
		
	}
	
    private void affichageInfos(){
    	
    	liste_commandes.clear();
    	
    	if (client != null){
    		
    		nom_client_textField.setText(clientSelectionne);
    		nom_complet_client_textField.setText(client.getNom_complet());
        	adresse_voie_textField.setText(client.getAdresse_rue());
        	adresse_cp_textField.setText(client.getAdresse_cp());
        	adresse_ville_textField.setText(client.getAdresse_ville());
        	remarques_client_textArea.setText(client.getRemarques());
    		
        	commandes_id = client.getCommandes_id(); 
    		liste_commandes.addAll(commandes_id.keySet());
    		
    		listView_commandes.setItems(liste_commandes);
   		
    	}  	
    }
    
    public void onNouveauClientButton() {	
    	super.onNouveauButton();   	
    	editability(true);
    	raz();
	
    }
    
    public void onAnnulerButton() {
    	super.etatInitial();
    	editability(false);
    	raz();

    	nouveau.setText("Nouveau client");
    	rafraichirAffichage();
    	listView_client.getSelectionModel().select(clientSelectionne);
    	affichageInfos();
    }
    
    public void rafraichirAffichage(){
    	
    	liste_clients = FXCollections.observableArrayList();
		liste_commandes  = FXCollections.observableArrayList();

		clientCursor = Client.retrouveClients();
		
		for (Client client : clientCursor){
			
			liste_clients.add(client.getNom());
		}
		
		listView_client.setItems(liste_clients);
    	
    }
    
    @FXML
    public void onEditerClientButton(){
    	super.onEditerButton();
    	editability(true);
    }
    
    @FXML
    public void onAnnulerEditButton(){
    	super.onAnnulerEditButton();
    	editability(false);
		
		rafraichirAffichage();
		listView_client.getSelectionModel().select(clientSelectionne);
    	affichageInfos();	
    }
    
    @FXML
    public void onMiseAJourClientButton(){
    	super.onMiseAJourButton();
    	editability(false);
    	
    	if (! edit){
    		client = new Client();
    	}
    	else {
    		client = Client.retrouveClient(clientSelectionne);
    	}
    	
    	client.setNom(nom_client_textField.getText());
    	client.setRemarques(remarques_client_textArea.getText());
    	client.setNom_complet(nom_complet_client_textField.getText());
    	client.setAdresse_rue(adresse_voie_textField.getText());
    	client.setAdresse_cp(adresse_cp_textField.getText());
    	client.setAdresse_ville(adresse_ville_textField.getText());

		if (edit) {
			
			Client.update(client);
			rafraichirAffichage();
		}
		else {
			
		   Client.save(client);
		   
		}
		onAnnulerEditButton();
    	
    }
    
    public void editability(boolean bool){
    	nom_client_textField.setEditable(bool);
		nom_complet_client_textField.setEditable(bool);
    	adresse_voie_textField.setEditable(bool);
    	adresse_cp_textField.setEditable(bool);
    	adresse_ville_textField.setEditable(bool);
		remarques_client_textArea.setEditable(bool);
		
		prompt(bool);
    }
    
    public void raz(){
    	nom_client_textField.setText("");
    	nom_complet_client_textField.setText("");
    	adresse_voie_textField.setText("");
    	adresse_cp_textField.setText("");
    	adresse_ville_textField.setText("");
    	remarques_client_textArea.setText("");
    }
    
    public void prompt(boolean bool){
    	
    	if (bool){
    		nom_client_textField.setPromptText("saisir le nom affiché dans les menus de l'interface");
        	nom_complet_client_textField.setPromptText("saisir le nom complet pour l'export");
        	adresse_voie_textField.setPromptText("n° dans le voie et nom de la voie");
        	adresse_cp_textField.setPromptText("code postal");
        	adresse_ville_textField.setPromptText("ville");
        	remarques_client_textArea.setPromptText("éventuelles remarques");
    	}
    	else {
    		nom_client_textField.setPromptText(null);
        	nom_complet_client_textField.setPromptText(null);
        	adresse_voie_textField.setPromptText(null);
        	adresse_cp_textField.setPromptText(null);
        	adresse_ville_textField.setPromptText(null);
        	remarques_client_textArea.setPromptText(null);
    	}
    }	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
        super.init();
        editability(false);
        
        versClientButton.setVisible(false);
		versCommandeButton.setVisible(false);
		versOeuvreButton.setVisible(false);
		
		Messages.setCommande(null);
		
		liste_clients = FXCollections.observableArrayList();
		liste_commandes  = FXCollections.observableArrayList();
		
		liste_clients.addAll(Client.retrouveClientsStr());
		
		listView_client.setItems(liste_clients);
		
        client = Messages.getClient();
        
        if (client != null){       	
			clientSelectionne = client.getNom();
			listView_client.getSelectionModel().select(liste_clients.indexOf(clientSelectionne));
		}
		else if (!liste_clients.isEmpty()){
			listView_client.getSelectionModel().select(0);
            clientSelectionne = listView_client.getSelectionModel().getSelectedItem();
            onClientSelect();

			Messages.setClient(Client.retrouveClient(clientSelectionne));
		}

		rafraichirAffichage();
		affichageInfos();
	}
}

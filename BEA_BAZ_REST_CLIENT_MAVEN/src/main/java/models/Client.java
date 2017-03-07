package models;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.bson.types.ObjectId;
import utils.RestAccess;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Client extends Commun{
    
	
	private String nom_complet;
	private String adresse_rue;
	private String adresse_cp;
	private String adresse_ville;
	private String remarques;

	private Map<String, String> commandes_id;
	
	public Client(){
		commandes_id = new HashMap<>();
	}

	public void update(){
        makeStringResult();
		RestAccess.update("client", stringResult);
	}
	
    public Client save(){
    	makeStringResult();
		String reponse = RestAccess.save("client", stringResult);
		
		try {
			return Commun.getMapper().readValue(reponse, Client.class);		
		}
		catch(IOException e){
			return null;
		}	
	}
    
    public Client get(){
		return this;	
	}
    
    public String toString(){	
    	return this.getNom();
    }

	public String getRemarques() {
		return remarques;
	}

	public void setRemarques(String remarques) {
		this.remarques = remarques;
	}

	public Set<String> getCommandes_names() {
		return commandes_id.keySet();
	}

	public void add_commande_id(Commande commande) {
		this.commandes_id.put(commande.getNom_affichage(), commande.get_id().toString());
	}

	public String getAdresse_rue() {
		return adresse_rue;
	}

	public void setAdresse_rue(String adresse_rue) {
		this.adresse_rue = adresse_rue;
	}

	public String getAdresse_cp() {
		return adresse_cp;
	}

	public void setAdresse_cp(String adresse_cp) {
		this.adresse_cp = adresse_cp;
	}

	public String getAdresse_ville() {
		return adresse_ville;
	}

	public void setAdresse_ville(String adresse_ville) {
		this.adresse_ville = adresse_ville;
	}

	public String getNom_complet() {
		return nom_complet;
	}

	public void setNom_complet(String nom_complet) {
		this.nom_complet = nom_complet;
	}

	public Map<String, String> getCommandes_id() {
		return commandes_id;
	}

	public void setCommandes_id(Map<String, String> commandes_id) {
		this.commandes_id = commandes_id;
	}
	
	public static ObjectId retrouveId(String clientSelectionne){

		return new ObjectId(retrouveClient(clientSelectionne).get_id());
	}
	
	public static Client retrouveClient(String clientSelectionne){

        String client_str= RestAccess.request("client", "nom", clientSelectionne);
        Client c = null;
        ObjectMapper mapper = new ObjectMapper();
        
        if (client_str != null){
          try {			 
  			  c = mapper.readValue(client_str, Client.class);
  		  }
  		  catch (IOException e) {
  	      }
        }
		return c;
	}
	
	public static Client retrouveClient(ObjectId id){

        String client_str= RestAccess.request("client", id);
        Client c = null;
		
		try {
			  c = Commun.getMapper().readValue(client_str, Client.class);
		  }
		  catch (IOException e) {
			  
	    }
		
		return c;
	}
	
	public static Client[] retrouveClients(){
		
		String client_str= RestAccess.request("client");

        Client[] c = null ;
		
		try {
			c = Commun.getMapper().readValue(client_str, Client[].class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}
	
    public static ObservableList<String> retrouveClientsStr(){
    	
    	ObservableList<String> liste_str = FXCollections.observableArrayList();
		
		for (Client c : retrouveClients()){
			liste_str.add(c.getNom());
		}
		return liste_str;
	}
    
    public static Client fromJson(String client_str){

        Client c = null;
        ObjectMapper mapper = new ObjectMapper();
        
        if (client_str != null){
          try {			 
  			  c = mapper.readValue(client_str, Client.class);
  		  }
  		  catch (IOException e) {
  	      }
        }
		return c;
	}
	
}

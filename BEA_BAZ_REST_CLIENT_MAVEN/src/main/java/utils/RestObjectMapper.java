package utils;

import java.io.IOException;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.databind.ObjectMapper;

import enums.Progression;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Commande;
import models.Commun;
import models.OeuvreTraitee;
import models.TacheTraitement;

public class RestObjectMapper {
	
	public static <T> T retrouveObjet(String table, String flux, Class<T> classe_t){
		
		T objet = null;
		
		try {
			objet = Commun.getMapper().readValue(RestAccess.request(table, "nom", flux), classe_t);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return objet;
	}
	
	public static <T> T retrouveObjet(String table, ObjectId id, Class<T> classe_t){
		
		T objet = null;
		
		try {
			objet = Commun.getMapper().readValue(RestAccess.request(table, id), classe_t);
		  }
		  catch (IOException e) {
			  
	    }
		
		return objet;
	}
	
	public static <T> T[] retrouveObjets(String table, Class<T[]> classe_t){
		
        T[] objets = null ;
		
		try {
			objets = Commun.getMapper().readValue(RestAccess.request(table), classe_t);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return objets;
	}
	
    public static <T> ObservableList<String> retrouveTacheTraitementsStr(String table, Class<T[]> classe_t){
    	
    	ObservableList<String> liste_str = FXCollections.observableArrayList();
		
		for (T t : RestObjectMapper.retrouveObjets(table, classe_t)){
			liste_str.add(((Commun)t).getNom());
		}
		return liste_str;
	}
}

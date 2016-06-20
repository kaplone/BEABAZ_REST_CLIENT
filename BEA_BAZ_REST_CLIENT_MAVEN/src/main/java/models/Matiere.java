package models;

import java.io.IOException;

import org.bson.types.ObjectId;

import utils.RestAccess;

public class Matiere extends Commun {
	
	private String nom_complet;
	
	public static void update(Matiere t){

		RestAccess.update("matiere", t);
	}
	
    public static void save(Matiere t){
		
		RestAccess.save("matiere", t);
		
	}
    
    public static void insert(Matiere t){
		
		RestAccess.insert("matiere", t);
		
	}
	
	public String getNom_complet() {
		return nom_complet;
	}

	public void setNom_complet(String nom_complet) {
		this.nom_complet = nom_complet;
	}

	public static ObjectId retrouveId(String matiereSelectionne){

		return retrouveMatiere(matiereSelectionne).get_id();
	}
	
	public static Matiere retrouveMatiere(String matiereSelectionne){

        String matiere_str= RestAccess.request("matiere", "nom", matiereSelectionne);
        Matiere c = null;
		
		try {
			  c = Commun.getMapper().readValue(matiere_str, Matiere.class);
		  }
		  catch (IOException e) {
	    }
		
		return c;
	}
	
	public static Matiere retrouveMatiere(ObjectId id){

        String matiere_str= RestAccess.request("matiere", id);
        Matiere c = null;
		
		try {
			  c = Commun.getMapper().readValue(matiere_str, Matiere.class);
		  }
		  catch (IOException e) {
			  
	    }
		
		return c;
	}
	
	public static Matiere[] retrouveMatieres(){
		
		String matiere_str= RestAccess.request("matiere");
        Matiere[] c = null ;
		
		try {
			c = Commun.getMapper().readValue(matiere_str, Matiere[].class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}
}

package models;

import java.io.IOException;

import org.bson.types.ObjectId;

import utils.RestAccess;

public class Matiere extends Commun {
	
	private String nom_complet;
	
	public String getNom_complet() {
		return nom_complet;
	}

	public void setNom_complet(String nom_complet) {
		this.nom_complet = nom_complet;
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

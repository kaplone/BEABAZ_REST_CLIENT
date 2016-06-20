package models;

import java.io.IOException;
import java.nio.file.Paths;

import org.bson.types.ObjectId;

import utils.RestAccess;

public class Fichier extends Commun{
	
	private String fichierLie;
	
	private String legende;
	
	private ObjectId oeuvre_id;
	
	public static void update(Fichier c){

		RestAccess.update("fichier", c);
	}
	
    public static void save(Fichier c){
		
		RestAccess.save("fichier", c);
		
	}
    
    @Override
    public String toString(){
    	
    	System.out.println("this.fichierLie : " + this.fichierLie);
    	System.out.println("Paths.get(this.fichierLie).getFileName() : " + Paths.get(this.fichierLie).getFileName());
    	
    	return Paths.get(this.fichierLie).getFileName().toString();
    	
    }

	public String getFichierLie() {
		return fichierLie;
	}

	public void setFichierLie(String fichierLie) {
		this.fichierLie = fichierLie;
	}

	public String getLegende() {
		return legende;
	}

	public void setLegende(String legende) {
		this.legende = legende;
	}
	
	@Override
	public String getNom(){
		return Paths.get(this.fichierLie).getFileName().toString();
	}

	public ObjectId getOeuvre_id() {
		return oeuvre_id;
	}

	public void setOeuvre_id(ObjectId oeuvre_id) {
		this.oeuvre_id = oeuvre_id;
	}
	
	public static ObjectId retrouveId(String FichierSelectionne){

		return retrouveFichier(FichierSelectionne).get_id();
	}
	
	public static Fichier retrouveFichier(String FichierSelectionne){

        String fichier_str= RestAccess.request("Fichier", "nom", FichierSelectionne);
        Fichier a = null;
		
		try {
			  a = Commun.getMapper().readValue(fichier_str, Fichier.class);
		  }
		  catch (IOException e) {
	    }
		
		return a;
	}
	
	public static Fichier retrouveFichier(ObjectId id){

        String fichier_str= RestAccess.request("Fichier", id);
        Fichier a = null;
		
		try {
			  a = Commun.getMapper().readValue(fichier_str, Fichier.class);
		  }
		  catch (IOException e) {
	    }
		
		return a;
	}
	
    public static Fichier[] retrouveFichiers(){
		
		String fichier_str= RestAccess.request("Fichier");
        Fichier[] c = null ;
		
		try {
			c = Commun.getMapper().readValue(fichier_str, Fichier[].class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}

	

}

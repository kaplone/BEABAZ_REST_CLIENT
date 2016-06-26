package models;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.bson.types.ObjectId;

import utils.RestAccess;

public class Model extends Commun{
	
    private String cheminVersModelSTR;
    
    public static void update(Model c){

		RestAccess.update("model", c);
	}
	
    public static void save(Model c){
		
		RestAccess.save("model", c);
		
	}

	public Path getCheminVersModel() {
		return Paths.get(cheminVersModelSTR);
	}
	public String getCheminVersModelSTR() {
		return cheminVersModelSTR;
	}

	public void setCheminVersModelSTR(String cheminVersModelSTR) {
		this.cheminVersModelSTR = cheminVersModelSTR;
	}

	public Path getModeleVertical() {
		
		Path base = Paths.get(cheminVersModelSTR).getParent();
		String name = Paths.get(cheminVersModelSTR).getFileName().toString();
		
		String nameVertical = name.split("\\.")[0] + "_vertical." + name.split("\\.")[1];
		
		
		return base.resolve(nameVertical);
	}
	
	public static ObjectId retrouveId(String ModelSelectionne){

		return retrouveModel(ModelSelectionne).get_id();
	}
	
	public static Model retrouveModel(String ModelSelectionne){

        String model_str= RestAccess.request("model", "nom", ModelSelectionne);
        Model a = null;
		
		try {
			  a = Commun.getMapper().readValue(model_str, Model.class);
		  }
		  catch (IOException e) {
	    }
		
		return a;
	}
	
	public static Model retrouveModel(ObjectId id){

        String model_str= RestAccess.request("model", id);
        Model a = null;
		
		try {
			  a = Commun.getMapper().readValue(model_str, Model.class);
		  }
		  catch (IOException e) {
	    }
		
		return a;
	}
	
    public static Model[] retrouveModels(){
		
		String model_str= RestAccess.request("model");
		
		System.out.println("model_str : " + model_str);
		
        Model[] c = null ;
		
		try {
			c = Commun.getMapper().readValue(model_str, Model[].class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}


}

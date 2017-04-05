package models;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonIgnore;

import utils.RestAccess;

public class Model extends Commun{
	
    private String cheminVersModelSTR;
    private String signature;
    
	public String getCheminVersModelSTR() {
		return cheminVersModelSTR;
	}

	public void setCheminVersModelSTR(String cheminVersModelSTR) {
		this.cheminVersModelSTR = cheminVersModelSTR;
	}
    
	@JsonIgnore
	public String getModeleVertical() {	
		return cheminVersModelSTR.split("\\.odt")[0] + "_vertical.odt";	
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
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
    
    

}

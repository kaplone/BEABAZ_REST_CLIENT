package utils;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Settings;

public class LoadConfig {
	
	static String nom;
	
    public static Settings loadSettings(){

		String home =  System.getProperty("user.home");
		File settings_file = new File(home, "bea_baz_rest.conf");
		
        FileReader fr = null;
    	
		try {
			fr = new FileReader(settings_file);

	    	BufferedReader br = new BufferedReader(fr);
	    	
	    	String s = br.readLine();
			
	    	while(s != null){
	    		
	    		if (s.startsWith("#") || s.trim().equals("")){
	    		}
	
	    		else {
	    		
		    		String key = s.split("=")[0].trim();
		    		String value = s.split("=")[1].trim();
		    		
		    		switch (key){
		    		
		    		case "adresse" : Settings.setAdresse(value);
		    		                break;
		    		case "version"    : Settings.setVersion(value);
	                                break;
		    		case "login"   : Settings.setLogin(value);
		    		                break;
		    		case "pass"    : Settings.setPass(value);
		    			            break;
		    		case "base"    : Settings.setBase(value);
		                            break;
		    		case "key"     : Settings.setKey(value);
                                    break;
	
		    		}
	    		}

				s = br.readLine();
	    	}
			fr.close();
			
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}
		
		return null;
		
	}

}
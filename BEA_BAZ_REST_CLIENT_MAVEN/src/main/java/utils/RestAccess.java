package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.Settings;
import models.Auteur;
import models.Commande;
import models.Commun;

import enums.Progression;

public class RestAccess {
	
	private static HttpClient client ;
	private static HttpGet request;
	private static HttpPost requestPost;
	private static String encryptedText;
	
	private static String adresse;
	
	public static void connect(){	
		LoadConfig.loadSettings();
		
		adresse = String.format("%s/%s", Settings.getAdresse(), Settings.getVersion());
		String key = Settings.getKey(); // 128 bit key
		
		client = HttpClientBuilder.create().build();

	    Key aesKey = new SecretKeySpec(key.getBytes(), "AES");

	    Cipher cipher;
	    
		try {
			cipher = Cipher.getInstance("AES");
		    cipher.init(Cipher.ENCRYPT_MODE, aesKey);
		    byte[] encrypted = cipher.doFinal(String.format("%s %s %s", Settings.getLogin(), Settings.getPass(), Settings.getBase()).getBytes());
		    Base64.Encoder encoder = Base64.getEncoder();
		    encryptedText = encoder.encodeToString(encrypted);
		  
		    System.out.println("encryptedText : " + encryptedText);
 
		    
	    
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean checkIfExists (String table, String field, String valeur) {

        return false;
	}
	
	public static String traitementReponse(){
		
        request.addHeader("monToken", new String(encryptedText));
        
        System.out.println("request.getURI() :" + request.getURI());
    	
    	HttpResponse response = null;
    	
    	String reponse = null;
    	
		try {
			response = client.execute(request);
			BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
		    reponse = rd.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return reponse;
		
	}
	

	
	public static String traitementReponsePost(){
		
        requestPost.addHeader("monToken", new String(encryptedText));
        requestPost.setHeader("Accept", "application/json");
        requestPost.setHeader("Content-type", "application/json");
        
        System.out.println("request.getURI() :" + requestPost.getURI());
    	
    	HttpResponse response = null;
    	
    	String reponse = null;
    	
		try {
			response = client.execute(requestPost);
			BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
		    reponse = rd.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return reponse;
		
	}
	
    public static String requestAll(String table) {	
    	
    	request = new HttpGet(String.format("%s/%s", adresse, table));
    	return traitementReponse();
    	
	}
	
    public static String request(String table) {	
    	
    	request = new HttpGet(String.format("%s/%s", adresse, table));
    	return traitementReponse();
    	
	}
    
    public static String request(String table, ObjectId id) {	
		
    	request = new HttpGet(String.format("%s/%s/%s", adresse, table, id.toString()));
    	return traitementReponse();
	}
    
    public static String request(String table, Commande commande) {	
		
    	request = new HttpGet(String.format("%s/%s/commande_id/%s/all", adresse, table, commande.get_id().toString()));
    	return traitementReponse();
	}

    
    public static String request(String table, String field, ObjectId objectId) {	
		
    	request = new HttpGet(String.format("%s/%s/%s/%s", adresse, table, field, objectId.toString()));
    	return traitementReponse();
	}
    
    public static String request(String table, String field, String value) {	
		
    	request = new HttpGet(String.format("%s/%s/%s/%s", adresse, table, field, value.replace(" ", "%20")));
    	return traitementReponse();
	}

    public static String request(String table, String field1, String value1, String field2, String value2) {	
		
    	request = new HttpGet(String.format("%s/%s/%s/%s/%s/%s", adresse, table, field1, value1, field2, value2));
    	return traitementReponse();
	}

    public static String request(String table, Progression progres, Commande commande) {	
    	request = new HttpGet(String.format("%s/%s/%s/%s", adresse, table, progres.toString(), commande.get_id().toString()));
    	return traitementReponse();
	}
    
    public static String request(String table, String field, String valeur, boolean regex) {	
    	request = new HttpGet(String.format("%s/%s/%s/%s/%s", adresse, table, field, valeur, regex));
    	return traitementReponse();
	}
	
	public static String save (String table, String c) {
		
		requestPost = new HttpPost(String.format("%s/ajout/%s", adresse, table));

		try {
			StringEntity se = new StringEntity(c);
			requestPost.setEntity(se);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return traitementReponsePost();
	}

	public static void drop() {	
	}

	public static String update(String table, String c) {
		
		requestPost = new HttpPost(String.format("%s/update/%s", adresse, table));

		try {
			StringEntity se = new StringEntity(c);
			requestPost.setEntity(se);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return traitementReponsePost();
	}
}

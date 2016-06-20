package application;
	
import models.Auteur;
import models.Client;
import models.Commande;
import models.Fichier;
import models.Messages;
import models.Model;
import models.Oeuvre;
import models.OeuvreTraitee;
import models.Produit;
import models.TacheTraitement;
import models.Traitement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import application.JfxUtils;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class Main_BEA_BAZ extends Application {
	
	private static String OS = System.getProperty("os.name").toLowerCase();
	
	String cheminMongod = "C:\\Program Files\\MongoDB\\Server\\3.0\\bin\\mongod.exe";
	String cheminMongo = "C:\\Program Files\\MongoDB\\Server\\3.0\\bin\\mongo.exe";
	String[] cmdArray = new String[]{"mongo"};
	String[] cmdArrayd = new String[]{"mongod"};

	@Override
	public void start(Stage primaryStage) {
	
		System.out.println(isMongodRunning());
		
//		if(! isMongodRunning()){
//			Process p;
//			try {
//				p = Runtime.getRuntime().exec(cmdArrayd);
//				System.out.println("lancement du serveur");
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		utils.RestAccess.connect();
		
		Messages.setExportStage(primaryStage);
		
		try {
			Pane root = new Pane();
			
			Scene fiche_client_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_client.fxml"), 1275, 722);
			fiche_client_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(fiche_client_scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}

	public static boolean isMac() {

		return (OS.indexOf("mac") >= 0);

	}

	public static boolean isUnix() {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
		
	}
	
	public boolean isMongodRunning(){

		boolean bool = false;
		Process p = null;
		
		try {
	        String line;
	        
	        if (isWindows()) {
				System.out.println("This is Windows");

				cmdArray = new String[]{cheminMongo};
				cmdArrayd = new String[]{cheminMongod};
				
				p = Runtime.getRuntime().exec
		                (System.getenv("windir") +"\\system32\\"+"tasklist.exe");
			} else if (isMac()) {
				System.out.println("This is Mac");
			} else if (isUnix()) {
				System.out.println("This is Unix or Linux");
				p = Runtime.getRuntime().exec("ps -e");
			}

	        BufferedReader input =
	                new BufferedReader(new InputStreamReader(p.getInputStream()));
	        while ((line = input.readLine()) != null) {
	        	if (line.contains("mongod")){
	        		bool = true;
	        	}
	        }
	        input.close();
	    } catch (Exception err) {
	        err.printStackTrace();
	    }
		
		return bool;

	    
	}

	public static void main(String[] args) {
		launch(args);

	}
}

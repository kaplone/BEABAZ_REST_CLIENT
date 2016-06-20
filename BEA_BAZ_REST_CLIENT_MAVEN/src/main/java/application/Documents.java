package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Auteur;
import models.Client;
import models.Commande;
import models.Matiere;
import models.Messages;
import models.Model;
import models.Oeuvre;
import models.OeuvreTraitee;
import models.Produit;
import models.TacheTraitement;
import models.Technique;
import models.Traitement;
import enums.Classes;
import enums.Progression;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.types.ObjectId;
import org.jongo.MongoCursor;
import org.jongo.marshall.MarshallingException;
import org.jongo.marshall.jackson.JacksonMapper;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import utils.MongoAccess;
import utils.Normalize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.util.JSON;

public class Documents {
	
	private static Commande commande;
	private static ObjectId commande_id;
	private static ArrayList<String> alterations ;
	
	private static MongoCursor<TacheTraitement> traitementCursor;
	private static MongoCursor<Traitement> tousLesTraitementsCursor;
	private static Map<String, ObjectId> tousLesTraitements_id;
	private static ObservableList<String> liste_traitements;
	
	private static Map<String, ObjectId> listeOeuvresTraitees_id;
	
	private static StringProperty bindedLabel = new SimpleStringProperty(); 

	public static void read(File file_, String table_) throws IOException {
		
        FileInputStream file = new FileInputStream(file_);
		
		int index = 0;
		
		boolean titres = true;
		boolean update = false;
		
		ArrayList<String> string_produit_liste = new ArrayList<>();
		ArrayList<String> string_traitement_liste = new ArrayList<>();
		ArrayList<String> string_technique_liste = new ArrayList<>();
		ArrayList<String> string_matiere_liste = new ArrayList<>();
		
		ObjectMapper mapper = new ObjectMapper();
		 
        //Create Workbook instance holding reference to .xlsx file
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        //Get first/desired sheet from the workbook
        XSSFSheet sheet = workbook.getSheetAt(0);

        //Iterate through each rows one by one
        Iterator<Row> rowIterator = sheet.iterator();
        
        String string_produit = "";
        String string_traitement = "";
        String string_matiere = "";
        String string_technique = "";
        
        Produit p;
        Traitement t;
        Technique te;
        Matiere m;
        
        while (rowIterator.hasNext())
        {

        	Row row = rowIterator.next();
        	
        	String [] champs = new String[2];
        	
        	switch (table_){
        	
	        	case "produit" :
	
		        	try {
		                if (utils.MongoAccess.request(table_, "nom_complet", row.getCell(0).getStringCellValue()).as(Classes.valueOf(table_).getUsedClass()) != null){
		                	
		                	p = (Produit) utils.MongoAccess.request(table_, "nom_complet", row.getCell(0).getStringCellValue()).as(Classes.valueOf(table_).getUsedClass());
		                	update = true;
		                }
		                else {
		                	
		                	p = new Produit();
		                    string_produit = "";
		                    string_produit_liste.clear();
		                    
		                    update = false;
		                }
		        	}
		
		        	catch (NullPointerException mpe){
		        		
		                p = new Produit();
		                
		                string_produit = "";
		                string_produit_liste.clear();
		                update = false;
		        		
		        	}
		        	
		        	if (update) {
		        		p.setUpdated_at(new Date());
		        	}
		        	else{
		        		
		        		p.setCreated_at(new Date());
			            
			            //For each row, iterate through all the columns
			            Iterator<Cell> cellIterator = row.cellIterator();
			            
			            
			
			            while (cellIterator.hasNext())
			            {
			                Cell cell = cellIterator.next();
			                
			                // le premier passage est 'à vide'
			                // c'est la liste des champs
			                if (titres){
		
			            	}
			                // les valeurs suivantes servent à construire 
			                // une json string
			            	else {
			
			            		champs [index] = (String) Normalize.normalizeField(cell.getStringCellValue()); 	
		
				                }
				                index ++; // on  avance dans la liste des champs
			            	}
			            }
			            
		        	    
		        	
			            titres = false; // après le premier passage ce ne sera plus un titre
			            
			            index = 0; // on initialise pour la prochaine ligne
			            
			            if (champs[0] != null){
			            	
			            	string_produit = String.format("{\"nom_complet\" : \"%s\", \"nom\" : \"%s\"}", champs[0], champs[1]);

				            p = mapper.readValue(string_produit, Produit.class);
				            
				            utils.MongoAccess.save("produit", p);
			            	
			            }
			            
			            
	            break;
	        
	        case "traitement" :
	        	
	        	try {
	                if (utils.MongoAccess.request(table_, "nom_complet", row.getCell(0).getStringCellValue()).as(Classes.valueOf(table_).getUsedClass()) != null){
	                	
	                	t = (Traitement) utils.MongoAccess.request(table_, "nom_complet", row.getCell(0).getStringCellValue()).as(Classes.valueOf(table_).getUsedClass());
	                	update = true;
	                }
	                else {
	                	
	                	t = new Traitement();
	                    string_traitement = "";
	                    string_traitement_liste.clear();
	                    
	                    update = false;
	                }
	        	}
	
	        	catch (NullPointerException mpe){
	        		
	                t = new Traitement();
	                string_traitement = "";
	                string_traitement_liste.clear();
	                update = false;
	        		
	        	}
	        	
	        	if (update) {
	        		t.setUpdated_at(new Date());
	        	}
	        	else{
	        		
	        		t.setCreated_at(new Date());
		            
		            //For each row, iterate through all the columns
		            Iterator<Cell> cellIterator = row.cellIterator();
		            
		            
		
		            while (cellIterator.hasNext())
		            {
		                Cell cell = cellIterator.next();
		                
		                // le premier passage est 'à vide'
		                // c'est la liste des champs
		                if (titres){
	
		            	}
		                // les valeurs suivantes servent à construire 
		                // une json string
		            	else {
		
		            		champs [index] = (String) Normalize.normalizeField(cell.getStringCellValue()); 	
	
			                }
			                index ++; // on  avance dans la liste des champs
		            	}
		            }
		            
	        	    
	        	
		            titres = false; // après le premier passage ce ne sera plus un titre
		            
		            index = 0; // on initialise pour la prochaine ligne
		            
		            if (champs[0] != null){
		            	
		            	string_traitement = String.format("{\"nom_complet\" : \"%s\", \"nom\" : \"%s\"}", champs[0], champs[1]);

			            t = (Traitement) mapper.readValue(string_traitement, Classes.valueOf(table_).getUsedClass());
			            
			            utils.MongoAccess.save(table_, t);
		            	
		            }
		            
		            
	        	
	            break;
	            
            case "technique" :
	        	
	        	try {
	                if (utils.MongoAccess.request(table_, "nom_complet", row.getCell(0).getStringCellValue()).as(Classes.valueOf(table_).getUsedClass()) != null){
	                	
	                	te = (Technique) utils.MongoAccess.request(table_, "nom_complet", row.getCell(0).getStringCellValue()).as(Classes.valueOf(table_).getUsedClass());
	                	update = true;
	                }
	                else {
	                	
	                	te = new Technique();
	                    string_technique = "";
	                    string_technique_liste.clear();
	                    
	                    update = false;
	                }
	        	}
	
	        	catch (NullPointerException mpe){
	        		
	                te = new Technique();
	                string_technique = "";
	                string_technique_liste.clear();
	                update = false;
	        		
	        	}
	        	
	        	if (update) {
	        		te.setUpdated_at(new Date());
	        	}
	        	else{
	        		
	        		te.setCreated_at(new Date());
		            
		            //For each row, iterate through all the columns
		            Iterator<Cell> cellIterator = row.cellIterator();
		            
		            
		
		            while (cellIterator.hasNext())
		            {
		                Cell cell = cellIterator.next();
		                
		                // le premier passage est 'à vide'
		                // c'est la liste des champs
		                if (titres){
	
		            	}
		                // les valeurs suivantes servent à construire 
		                // une json string
		            	else {
		
		            		champs [index] = (String) Normalize.normalizeField(cell.getStringCellValue()); 	
	
			                }
			                index ++; // on  avance dans la liste des champs
		            	}
		            }
		            
	        	    
	        	
		            titres = false; // après le premier passage ce ne sera plus un titre
		            
		            index = 0; // on initialise pour la prochaine ligne
		            
		            if (champs[0] != null){
		            	
		            	string_technique = String.format("{\"nom_complet\" : \"%s\", \"nom\" : \"%s\"}", champs[0], champs[1]);

			            te = (Technique) mapper.readValue(string_technique, Classes.valueOf(table_).getUsedClass());
			            
			            utils.MongoAccess.save(table_, te);
		            	
		            }
		            
		            
	        	
	            break;
	        
             case "matiere" :
	        	
	        	try {
	                if (utils.MongoAccess.request(table_, "nom_complet", row.getCell(0).getStringCellValue()).as(Classes.valueOf(table_).getUsedClass()) != null){
	                	
	                	m = (Matiere) utils.MongoAccess.request(table_, "nom_complet", row.getCell(0).getStringCellValue()).as(Classes.valueOf(table_).getUsedClass());
	                	update = true;
	                }
	                else {
	                	
	                	m = new Matiere();
	                    string_matiere = "";
	                    string_matiere_liste.clear();
	                    
	                    update = false;
	                }
	        	}
	
	        	catch (NullPointerException mpe){
	        		
	                m = new Matiere();
	                string_matiere = "";
	                string_matiere_liste.clear();
	                update = false;
	        		
	        	}
	        	
	        	if (update) {
	        		m.setUpdated_at(new Date());
	        	}
	        	else{
	        		
	        		m.setCreated_at(new Date());
		            
		            //For each row, iterate through all the columns
		            Iterator<Cell> cellIterator = row.cellIterator();
		            
		            
		
		            while (cellIterator.hasNext())
		            {
		                Cell cell = cellIterator.next();
		                
		                // le premier passage est 'à vide'
		                // c'est la liste des champs
		                if (titres){
	
		            	}
		                // les valeurs suivantes servent à construire 
		                // une json string
		            	else {
		
		            		champs [index] = (String) Normalize.normalizeField(cell.getStringCellValue()); 	
	
			                }
			                index ++; // on  avance dans la liste des champs
		            	}
		            }
		            
	        	    
	        	
		            titres = false; // après le premier passage ce ne sera plus un titre
		            
		            index = 0; // on initialise pour la prochaine ligne
		            
		            if (champs[0] != null){
		            	
		            	string_matiere = String.format("{\"nom_complet\" : \"%s\", \"nom\" : \"%s\"}", champs[0], champs[1]);

			            m = (Matiere) mapper.readValue(string_matiere, Classes.valueOf(table_).getUsedClass());
			            
			            utils.MongoAccess.save(table_, m);
		            	
		            }
		            
		            
	        	
	            break;    
	        
	        default : break;
	        	
	    }
	        	
        
        workbook.close();
        file.close();

	    }
		
	}
	
	public static boolean isRowEmpty(Row row) {
	    for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
	        Cell cell = row.getCell(c);
	        if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
	            return false;
	    }
	    return true;
	}
	
	
	

	public static void read(File file_) throws IOException {
		
		liste_traitements = FXCollections.observableArrayList();
		listeOeuvresTraitees_id = new TreeMap<>();

		traitementCursor = MongoAccess.request("tacheTraitement").as(TacheTraitement.class);
		tousLesTraitementsCursor = MongoAccess.request("traitement").as(Traitement.class);
		
		tousLesTraitements_id = new TreeMap<>();
		
		while (tousLesTraitementsCursor.hasNext()){
			
			Traitement t = tousLesTraitementsCursor.next();
			
			liste_traitements.add(t.getNom());
			tousLesTraitements_id.put(t.getNom(), t.get_id());
		}


		
		commande_id = Messages.getCommande().get_id();
		
		System.out.println(commande_id);
		commande = MongoAccess.request("commande", commande_id).as(Commande.class).next();
		
		boolean titres = true;
		boolean update = false;
		
		ArrayList<String> noms_titres = new ArrayList<>();
		ArrayList<String> noms_titres_bruts = new ArrayList<>();
		
		FileInputStream file = new FileInputStream(file_);
		
		int nb_colonnes = 0;
		
		int index = 0;
		
		String string_oeuvre = "";
		ArrayList<String> string_oeuvre_liste = new ArrayList<>();
		Oeuvre o = new Oeuvre();
		OeuvreTraitee ot = new OeuvreTraitee();

		ObjectMapper mapper = new ObjectMapper();
		 
        //Create Workbook instance holding reference to .xlsx file
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        //Get first/desired sheet from the workbook
        XSSFSheet sheet0 = workbook.getSheetAt(0);
        XSSFSheet sheet1 = workbook.getSheetAt(1);

        //Iterate through each rows one by one
        Iterator<Row> rowIterator0 = sheet0.iterator();
        while (rowIterator0.hasNext()){
        	
        	ArrayList<String> alterations = new ArrayList<>();

        	Row row = rowIterator0.next();
        	
        	if (isRowEmpty(row)){
        		continue;
        	}
        	
        	boolean st;
        	
        	if (! titres){

	        	try {
	        		// l'oeuvre existe dans la base
	                if (utils.MongoAccess.request("oeuvre", "cote_archives_6s", row.getCell(1).getStringCellValue()).as(Oeuvre.class) != null){
	                	
	                	System.out.print("cas 1");
	                	System.out.println(row.getCell(1).getStringCellValue());
	                	
	                	o = utils.MongoAccess.request("oeuvre", "cote_archives_6s", row.getCell(1).getStringCellValue()).as(Oeuvre.class);
	                	update = true;
	                }
	             // l'oeuvre n'existe pas encore dans la base
	                else {
	                	
	                	System.out.println("cas 2");
	                	
	                	o = new Oeuvre();
	                    string_oeuvre = "";
	                    string_oeuvre_liste.clear();
	                    string_oeuvre_liste.add(String.format("\"%s\" : \"%s\"", "auteur", commande.getAuteur().get_id()));
	                    string_oeuvre_liste.add(String.format("\"%s\" : \"%s\"", "created_at", LocalDate.now()));
	                    string_oeuvre_liste.add(String.format("\"%s\" : \"%s\"", "updated_at", null));
	                    string_oeuvre_liste.add(String.format("\"%s\" : \"%s\"", "deleted_at", null));
	                    string_oeuvre_liste.add(String.format("\"%s\" : \"%s\"", "key1", Normalize.normalizeStringField(row.getCell(4).getStringCellValue())));
	                    string_oeuvre_liste.add(String.format("\"%s\" : \"%s\"", "key2", Normalize.normalizeStringField(row.getCell(6).getStringCellValue())));
	                    
	                    update = false;
	                }
	    
	                
	        	}
	        	catch (IllegalStateException ise){
	        		// l'oeuvre existe dans la base
	        		if (utils.MongoAccess.request("oeuvre", "cote_archives_6s", (int)Math.round(row.getCell(1).getNumericCellValue()) + "").as(Oeuvre.class) != null){
	        			
	        			System.out.println("cas 3");
	        			
	        			o = utils.MongoAccess.request("oeuvre", "cote_archives_6s", (int)Math.round(row.getCell(1).getNumericCellValue()) + "").as(Oeuvre.class); 
	        			update = true;
	        		}
	        		// l'oeuvre n'existe pas encore dans la base
	        		else {
	        			
	        			System.out.print("cas 4");
	        			System.out.println(row.getCell(1).getNumericCellValue() + "");
	        			
	        			 o = new Oeuvre();
	                     string_oeuvre = "";
	                     string_oeuvre_liste.clear();
	                     string_oeuvre_liste.add(String.format("\"%s\" : \"%s\"", "commande", commande_id));
	                     string_oeuvre_liste.add(String.format("\"%s\" : \"%s\"", "auteur", commande.getAuteur().get_id()));
	                     string_oeuvre_liste.add(String.format("\"%s\" : \"%s\"", "created_at", LocalDate.now()));
	                     string_oeuvre_liste.add(String.format("\"%s\" : \"%s\"", "updated_at", null));
	                     string_oeuvre_liste.add(String.format("\"%s\" : \"%s\"", "deleted_at", null));
	                     string_oeuvre_liste.add(String.format("\"%s\" : \"%s\"", "key1", Normalize.normalizeStringField(row.getCell(4).getStringCellValue())));
	                     string_oeuvre_liste.add(String.format("\"%s\" : \"%s\"", "key2", Normalize.normalizeStringField(row.getCell(6).getStringCellValue())));
	                     
	                     update = false;
	        		}
	            }
	        	catch (NullPointerException mpe){
	        		
	        		System.out.println("cas 5");
	        		
	        		// l'oeuvre n'existe pas encore dans la base
	                o = new Oeuvre();
	                string_oeuvre = "";
	                string_oeuvre_liste.clear();
	                string_oeuvre_liste.add(String.format("\"%s\" : \"%s\"", "commande", commande_id));
	                string_oeuvre_liste.add(String.format("\"%s\" : \"%s\"", "created_at", LocalDate.now()));
	                string_oeuvre_liste.add(String.format("\"%s\" : \"%s\"", "updated_at", null));
	                string_oeuvre_liste.add(String.format("\"%s\" : \"%s\"", "deleted_at", null));
	                string_oeuvre_liste.add(String.format("\"%s\" : \"%s\"", "etat_current", "TODO_"));
	                
	                update = false;
	        		
	        	}
        	}
        	
        	if (update) {
        		
        	}
        	else{
	            
	            //For each row, iterate through all the columns
	            Iterator<Cell> cellIterator = row.cellIterator();
	
	            while (cellIterator.hasNext()){
	            	
	                Cell cell = cellIterator.next();
	                
	                // le premier passage est 'à vide'
	                // c'est la liste des champs
	                if (titres){
	            		noms_titres.add(cell.getStringCellValue().equals("") ? String.format("field_%02d", nb_colonnes + 1) : Normalize.normalize(cell.getStringCellValue()));
	            		noms_titres_bruts.add(cell.getStringCellValue().equals("") ? String.format("field_%02d", nb_colonnes + 1) : Normalize.normalizeLight(cell.getStringCellValue()));
	            		nb_colonnes = noms_titres.size();
	            	}
	                // les valeurs suivantes servent à construire 
	                // une json string
	            	else {
	            		
		                //Check the cell type and format accordingly
		                switch (cell.getCellType())
		                {
		                    case Cell.CELL_TYPE_NUMERIC:
	
	                            string_oeuvre_liste.add(String.format("\"%s\" : \"%s\"", noms_titres.get(index),
	                            		index == 1 ? (int)Math.round(cell.getNumericCellValue()) + "" : cell.getNumericCellValue() + ""));
	                         
		                        break;
		                        
		                    case Cell.CELL_TYPE_STRING:
		                        
		                        string_oeuvre_liste.add(String.format("\"%s\" : \"%s\"", noms_titres.get(index), Normalize.normalizeStringField(cell.getStringCellValue())));
		      
		                        break;
		                }
		                index ++; // on  avance dans la liste des champs
	            	}
	            }
	            
	            index = 0; // on initialise pour la prochaine ligne

	            if (! titres){
	            	
	            	string_oeuvre = string_oeuvre_liste.stream().collect(Collectors.joining(", ", "{", "}"));
	            	
		            o = mapper.readValue(string_oeuvre, Oeuvre.class);

		            utils.MongoAccess.save("oeuvre", o);
	            }

        	}
        	
        	if (! titres){
        	
	        	ot = new OeuvreTraitee();
	            ot.setOeuvre_id(o.get_id());
	            ot.setProgressionOeuvreTraitee(Progression.TODO_);
	            ot.setAlterations(alterations);
	            ot.setKey1(o.getKey1());
	            ot.setKey2(o.getKey2());
	            ot.setCote(o.getCote_archives_6s());
	            ot.setNom(o.getNom());
	            ot.setCommande_id(commande_id);

	            utils.MongoAccess.save("oeuvreTraitee", ot);
	            
	            listeOeuvresTraitees_id.put(ot.getNom(), ot.get_id());
   
	            ArrayList<ObjectId> traitementsEnCours = new ArrayList<>();
	            
	            for (String t : commande.getTraitements_attendus_id().keySet()) {
	            	
	            	TacheTraitement tt = new TacheTraitement();
	            	tt.setFait_(Progression.TODO_);
	            	tt.setTraitement_id(tousLesTraitements_id.get(t));
	            	tt.setNom(t);
	            	

	            	utils.MongoAccess.save("tacheTraitement", tt);
	            	ot.addTraitementAttendu(tt.getNom(), tt.get_id());
  	
	    		}
    
	            utils.MongoAccess.update("oeuvreTraitee", ot);
	            
        	}
            
            titres = false; // après le premier passage ce ne sera plus un titre
	     }
        
        System.out.println(listeOeuvresTraitees_id);

        commande.setOeuvresTraitees_id(listeOeuvresTraitees_id);
        utils.MongoAccess.update("commande", commande);
        
        //////////////////////////////////////////////////////////////////////////////////////////////////:
        	
    	//Iterate through each rows one by one
        Iterator<Row> rowIterator1 = sheet1.iterator();
        
        titres = true;
        index = 0;
        noms_titres.clear();
        noms_titres_bruts.clear();
    	
    	string_oeuvre_liste.clear();
    	
    	OeuvreTraitee oeuvreACompleter = new OeuvreTraitee();
        
        while (rowIterator1.hasNext()){
	
            Row row = rowIterator1.next();
        	
        	if (isRowEmpty(row)){
        		continue;
        	}
        	
        	if (! titres){
        		
        		//System.out.println("**" + row.getCell(6).getStringCellValue() +  "**");
        		
        		if ("".equals(row.getCell(6).getStringCellValue())){
        			oeuvreACompleter = MongoAccess.request("oeuvreTraitee", "key1", Normalize.normalizeStringField(row.getCell(4).getStringCellValue())).as(OeuvreTraitee.class);
        		}
        		else {
        			oeuvreACompleter = MongoAccess.request("oeuvreTraitee", "key1", Normalize.normalizeStringField(row.getCell(4).getStringCellValue()), "key2", row.getCell(6).getStringCellValue()).as(OeuvreTraitee.class);
        		}
        		
        		
            	if (oeuvreACompleter == null){
            		System.out.println(Normalize.normalizeStringField(row.getCell(4).getStringCellValue()) + " : non trouvée");
            		continue;
            	} 
            	
            	
        	}
        	
        	alterations = new ArrayList<>();
	            
            //For each row, iterate through all the columns
            Iterator<Cell> cellIterator = row.cellIterator();
            
            

            while (cellIterator.hasNext()){
            	
                Cell cell = cellIterator.next();
                
                // le premier passage est 'à vide'
                // c'est la liste des champs
 
                if (titres){
            		noms_titres.add(cell.getStringCellValue().equals("") ? String.format("field_%02d", nb_colonnes + 1) : Normalize.normalize(cell.getStringCellValue()));
            		noms_titres_bruts.add(cell.getStringCellValue().equals("") ? String.format("field_%02d", nb_colonnes + 1) : Normalize.normalizeLight(cell.getStringCellValue()));
            		nb_colonnes = noms_titres.size();
            	}
                // les valeurs suivantes servent à construire 
                // une json string
            	else {
            		
	                //Check the cell type and format accordingly
	                switch (cell.getCellType())
	                {
	                    case Cell.CELL_TYPE_NUMERIC:

                           // string_oeuvre_liste.add(String.format("\"%s\" : \"%s\"", noms_titres.get(index), cell.getNumericCellValue() + ""));
	                        break;
	                        
	                    case Cell.CELL_TYPE_STRING:
	                        
	                        if (index >=7 && index <= 22){
	                        	
	                        	if ("x".equals(cell.getStringCellValue())){
	                        		alterations.add(String.format("\"%s\"", noms_titres_bruts.get(index)));
	                        	}
	                        	
	                        }
	                        else if (index == 23) {
	                        	string_oeuvre_liste.add(String.format("\"%s\" : \"%s\"", noms_titres.get(index), Normalize.normalizeField(cell.getStringCellValue())));
	                        }
	                        
	                        break;
	                }
	                index ++; // on  avance dans la liste des champs

	            }
	            
            } 
       
            if (! titres){
            	
            	if (alterations.size() > 0){
                	string_oeuvre_liste.add(String.format("\"%s\" : %s", "alterations", alterations));
                    alterations.clear();
                }
                
            	string_oeuvre = string_oeuvre_liste.stream().collect(Collectors.joining(", ", "{", "}"));
                utils.MongoAccess.update("oeuvreTraitee",oeuvreACompleter.get_id(), string_oeuvre);	
                string_oeuvre_liste.clear();
            }

            index = 0; // on initialise pour la prochaine ligne

            titres = false; // après le premier passage ce ne sera plus un titre
 
	    }
            
        
        workbook.close();
        file.close();

	}
	
	public static void listeDesTachesTraitement(Oeuvre oeuvre){
		
		//commande = MongoAccess.request("commande", Messages.getCommande_id()).as(Commande.class).next();
		
		
		System.out.println("nom : " +oeuvre.getNom());
		//Fiche_commande_import_controller.getBindLabel().set("Import en cours : " + oeuvre.getNom());
		
		//ArrayList<TacheTraitement> listeDesTaches = new ArrayList<>();
		
		for (String t : commande.getTraitements_attendus_names()){
			
			TacheTraitement tt = new TacheTraitement();
			tt.setCreated_at(Date.from(Instant.now()));
			tt.setFait_(Progression.TODO_);
			
			MongoAccess.save("tacheTraitement", tt);
			
			//listeDesTaches.add(tt);
		}
		
		//return listeDesTaches.stream().map(t -> t.get_id().toString()).collect(Collectors.joining(",", "[", "]"));
		
		
		
	}
	
	public static void write(){
		//Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();
         
        //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet("Employee Data");
          
        //This data needs to be written (Object[])
        Map<String, Object[]> data = new TreeMap<String, Object[]>();
        data.put("1", new Object[] {"ID", "NAME", "LASTNAME"});
        data.put("2", new Object[] {1, "Amit", "Shukla"});
        data.put("3", new Object[] {2, "Lokesh", "Gupta"});
        data.put("4", new Object[] {3, "John", "Adwards"});
        data.put("5", new Object[] {4, "Brian", "Schultz"});
          
        //Iterate over data and write to sheet
        Set<String> keyset = data.keySet();
        int rownum = 0;
        for (String key : keyset)
        {
            Row row = sheet.createRow(rownum++);
            Object [] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr)
            {
               Cell cell = row.createCell(cellnum++);
               if(obj instanceof String)
                    cell.setCellValue((String)obj);
                else if(obj instanceof Integer)
                    cell.setCellValue((Integer)obj);
            }
        }
        try
        {
            //Write the workbook in file system
            FileOutputStream out = new FileOutputStream(new File("/home/kaplone/Desktop/howtodoinjava_demo.xlsx"));
            workbook.write(out);
            out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}
	
	

}

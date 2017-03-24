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
import java.util.LinkedList;
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
import models.Commun;
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
import org.json.JSONObject;

import utils.RestAccess;
import utils.Normalize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.util.JSON;

public class Documents {
	
	private static Commande commande;
	private static String commande_id;
	private static ArrayList<String> alterations ;
	
	private static MongoCursor<TacheTraitement> traitementCursor;
	private static MongoCursor<Traitement> tousLesTraitementsCursor;
	private static Map<String, String> tousLesTraitements_id;
	private static ObservableList<String> liste_traitements;
	
	private static Map<String, String> listeOeuvresTraitees_id;
	
	private static StringProperty bindedLabel = new SimpleStringProperty(); 
	
	private static Iterator<Row> rowIterator;
	private static Row row;
	private static Iterator<Cell> cellIterator;
	private static Cell cell;	
	
	private static JSONObject jsonObjectFile;
	private static JSONObject jsonObjectOeuvre;	
	private static JSONObject jsonObjectOeuvreTraitee;	
	private static JSONObject jsonObjectRow;	
	private static List<JSONObject> jsonObjectRows;
	private static Map<String, List<JSONObject>> documentJson;
	
	private static Map<String, JSONObject> contenuRow;
	private static Map<String, String> contenuRowOeuvre;
	private static Map<String, String> contenuRowOeuvreTraitee;
    
    

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
//		                if (utils.MongoAccess.request(table_, "nom_complet", row.getCell(0).getStringCellValue()).as(Classes.valueOf(table_).getUsedClass()) != null){
//		                	
//		                	p = (Produit) utils.MongoAccess.request(table_, "nom_complet", row.getCell(0).getStringCellValue()).as(Classes.valueOf(table_).getUsedClass());
//		                	update = true;
//		                }
//		                else {
//		                	
//		                	p = new Produit();
//		                    string_produit = "";
//		                    string_produit_liste.clear();
//		                    
//		                    update = false;
//		                }
		        	}
		
		        	catch (NullPointerException mpe){
		        		
		                p = new Produit();
		                
		                string_produit = "";
		                string_produit_liste.clear();
		                update = false;
		        		
		        	}
		        	
		        	if (update) {
//		        		p.setUpdated_at(new Date());
		        	}
		        	else{
		        		
//		        		p.setCreated_at(new Date());
			            
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
				            
//				            utils.MongoAccess.save("produit", p);
			            	
			            }
			            
			            
	            break;
	        
	        case "traitement" :
	        	
	        	try {
//	                if (utils.MongoAccess.request(table_, "nom_complet", row.getCell(0).getStringCellValue()).as(Classes.valueOf(table_).getUsedClass()) != null){
//	                	
//	                	t = (Traitement) utils.MongoAccess.request(table_, "nom_complet", row.getCell(0).getStringCellValue()).as(Classes.valueOf(table_).getUsedClass());
//	                	update = true;
//	                }
//	                else {
//	                	
//	                	t = new Traitement();
//	                    string_traitement = "";
//	                    string_traitement_liste.clear();
//	                    
//	                    update = false;
//	                }
	        	}
	
	        	catch (NullPointerException mpe){
	        		
	                t = new Traitement();
	                string_traitement = "";
	                string_traitement_liste.clear();
	                update = false;
	        		
	        	}
	        	
	        	if (update) {
//	        		t.setUpdated_at(new Date());
	        	}
	        	else{
	        		
//	        		t.setCreated_at(new Date());
		            
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
			            
//			            utils.MongoAccess.save(table_, t);
		            	
		            }
		            
		            
	        	
	            break;
	            
            case "technique" :
	        	
	        	try {
//	                if (utils.MongoAccess.request(table_, "nom_complet", row.getCell(0).getStringCellValue()).as(Classes.valueOf(table_).getUsedClass()) != null){
//	                	
//	                	te = (Technique) utils.MongoAccess.request(table_, "nom_complet", row.getCell(0).getStringCellValue()).as(Classes.valueOf(table_).getUsedClass());
//	                	update = true;
//	                }
//	                else {
//	                	
//	                	te = new Technique();
//	                    string_technique = "";
//	                    string_technique_liste.clear();
//	                    
//	                    update = false;
//	                }
	        	}
	
	        	catch (NullPointerException mpe){
	        		
	                te = new Technique();
	                string_technique = "";
	                string_technique_liste.clear();
	                update = false;
	        		
	        	}
	        	
	        	if (update) {
//	        		te.setUpdated_at(new Date());
	        	}
	        	else{
	        		
//	        		te.setCreated_at(new Date());
		            
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
			            
//			            utils.MongoAccess.save(table_, te);
		            	
		            }
		            
		            
	        	
	            break;
	        
             case "matiere" :
	        	
	        	try {
//	                if (utils.MongoAccess.request(table_, "nom_complet", row.getCell(0).getStringCellValue()).as(Classes.valueOf(table_).getUsedClass()) != null){
//	                	
//	                	m = (Matiere) utils.MongoAccess.request(table_, "nom_complet", row.getCell(0).getStringCellValue()).as(Classes.valueOf(table_).getUsedClass());
//	                	update = true;
//	                }
//	                else {
//	                	
//	                	m = new Matiere();
//	                    string_matiere = "";
//	                    string_matiere_liste.clear();
//	                    
//	                    update = false;
//	                }
	        	}
	
	        	catch (NullPointerException mpe){
	        		
	                m = new Matiere();
	                string_matiere = "";
	                string_matiere_liste.clear();
	                update = false;
	        		
	        	}
	        	
	        	if (update) {
//	        		m.setUpdated_at(new Date());
	        	}
	        	else{
	        		
//	        		m.setCreated_at(new Date());
		            
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
			            
//			            utils.MongoAccess.save(table_, m);
		            	
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
		
		commande_id = Messages.getCommande().get_id();

		jsonObjectRows = new ArrayList<>();
		
		FileInputStream file = new FileInputStream(file_);
		ObjectMapper mapper = new ObjectMapper();
 
        //Create Workbook instance holding reference to .xlsx file
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        //Get first/desired sheet from the workbook
        XSSFSheet sheet0 = workbook.getSheetAt(0);

        //Iterate through each rows one by one
        rowIterator = sheet0.iterator();
        while (rowIterator.hasNext()){

        	row = rowIterator.next();
        	
        	if (isRowEmpty(row) || row.getRowNum() < 2){
        		continue;
        	}

        	else {
        		//For each row, iterate through all the columns
	            cellIterator = row.cellIterator();
                
	            contenuRow = new HashMap<>();
	            contenuRowOeuvre = new HashMap<>();
	            contenuRowOeuvreTraitee = new HashMap<>();
	            contenuRowOeuvreTraitee.put("commande_id", commande_id);
	            
	            while (cellIterator.hasNext()){
	            	cell = cellIterator.next();	
	            	switch (cell.getColumnIndex()) {
	            	
	                case 0: //cote
	                	switch (cell.getCellType()) {
	                    case Cell.CELL_TYPE_NUMERIC:
	                    	contenuRowOeuvre.put("cote_archives_6s", "" + Math.round(cell.getNumericCellValue()));
	                        break;
	                    case Cell.CELL_TYPE_STRING:
	                    	contenuRowOeuvre.put("cote_archives_6s", cell.getStringCellValue());
	                        break;
	                    }
	                    break;
	                case 1: // titre
	                	contenuRowOeuvre.put("titre_de_l_oeuvre", cell.getStringCellValue());
	                    break;
	                case 2: // auteur
	                	contenuRowOeuvre.put("auteur", cell.getStringCellValue());
	                    break;
	                case 3: // date
	                	switch (cell.getCellType()) {
	                    case Cell.CELL_TYPE_NUMERIC:
	                    	contenuRowOeuvre.put("date", "" + Math.round(cell.getNumericCellValue()));
	                        break;
	                    case Cell.CELL_TYPE_STRING:
	                    	contenuRowOeuvre.put("date", cell.getStringCellValue());
	                        break;
	                    }
	                    break;
		            case 4: // dimensions
	                	switch (cell.getCellType()) {
	                    case Cell.CELL_TYPE_NUMERIC:
	                    	contenuRowOeuvre.put("dimensions", "" + Math.round(cell.getNumericCellValue()));
	                        break;
	                    case Cell.CELL_TYPE_STRING:
	                    	contenuRowOeuvre.put("dimensions", cell.getStringCellValue());
	                        break;
	                    }
	                    break;
		            case 5: // matieres
	                	contenuRowOeuvre.put("matieres", cell.getStringCellValue());
	                    break;
		            case 6: // techniques
	                	contenuRowOeuvre.put("techniques", cell.getStringCellValue());
	                    break;
		            case 7: // inscriptions
	                	contenuRowOeuvre.put("inscriptions", cell.getStringCellValue());
	                    break;
	                    
	                //// oeuvre traitee     
	                    
		            case 8: // observations
	                	contenuRowOeuvreTraitee.put("observations", cell.getStringCellValue());
	                    break;
		            case 9: // alterations depot
	                	contenuRowOeuvreTraitee.put("alterations depot", cell.getStringCellValue());
	                    break;
		            case 10: // alterations physiques
	                	contenuRowOeuvreTraitee.put("alterations physiques", cell.getStringCellValue());
	                    break;
		            case 11: // alterations chimiques
	                	contenuRowOeuvreTraitee.put("alterations chimiques", cell.getStringCellValue());
	                    break;
		            case 12: // alterations techniques
	                	contenuRowOeuvreTraitee.put("alterations techniques", cell.getStringCellValue());
	                    break;
		            case 13: // traitement depoussierage
	                	contenuRowOeuvreTraitee.put("traitement depoussierage", cell.getStringCellValue());
	                    break;
		            case 14: // traitement suppression elements
	                	contenuRowOeuvreTraitee.put("traitement suppression elements", cell.getStringCellValue());
	                    break;
		            case 15: // traitements aqueux
	                	contenuRowOeuvreTraitee.put("traitements aqueux", cell.getStringCellValue());
	                    break;
		            case 16: // traitement consolidation
	                	contenuRowOeuvreTraitee.put("traitement consolidation", cell.getStringCellValue());
	                    break;
		            case 17: // traitement mise a plat
	                	contenuRowOeuvreTraitee.put("traitement mise a plat", cell.getStringCellValue());
	                    break;
		            case 18: // traitement retouche
	                	contenuRowOeuvreTraitee.put("traitement retouche", cell.getStringCellValue());
	                    break;
		            case 19: // produits utilisés
	                	contenuRowOeuvreTraitee.put("produits utilisés", cell.getStringCellValue());
	                    break;
	            	}
	            }
	            
	            
	            
	            jsonObjectOeuvre = new JSONObject(contenuRowOeuvre);
	            jsonObjectOeuvreTraitee = new JSONObject(contenuRowOeuvreTraitee);
	            
	            contenuRow.put("oeuvre", jsonObjectOeuvre);
	            contenuRow.put("oeuvreTraitee", jsonObjectOeuvreTraitee);
	            
	            jsonObjectRow = new JSONObject(contenuRow);
	            
	            jsonObjectRows.add(jsonObjectRow);
	         
        	}
        }
        
        System.out.println("fin de read()");
        documentJson = new HashMap<>();
        documentJson.put("document", jsonObjectRows);
        jsonObjectFile = new JSONObject(documentJson);
        System.out.println(jsonObjectFile);
        
        RestAccess.importFromDocument(jsonObjectFile.toString());
	}
}
package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.scene.image.Image;

import org.bson.types.ObjectId;

import models.Messages;
import models.Commande;
import models.Fichier;
import models.Model;
import models.Oeuvre;
import models.OeuvreTraitee;
import models.Produit;
import models.TacheTraitement;
import models.Traitement;
import enums.Progression;
import fr.opensagres.xdocreport.converter.ConverterRegistry;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.IConverter;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.ClassPathImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.annotations.FieldMetadata;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.annotations.ImageMetadata;

public class FreeMarkerMaker {
	
	@FieldMetadata( images = { @ImageMetadata( name = "image_oeuvre" ) } )

	public static void odt2pdf(Oeuvre o, OeuvreTraitee ot) {

		try {		  
              List<Fichier> listeFichiers = ot.getFichiers_obj();
              listeFichiers.sort(new Comparator<Fichier>() {

					@Override
					public int compare(Fichier o1, Fichier o2) {
						return o1.getNom().compareTo(o2.getNom());
					}
				});
            		
              Image img = null;
              File file = new File(ot.getFichierAffiche().getFichierLie());
              if (file.exists()){
            	  img = new Image("file:" + ot.getFichierAffiche().getFichierLie().toString());
              }
              else {
            	  file = new File("/home/kaplone/Desktop/20230811.jpg");
            	  System.out.println(file.exists());
            	  img = new Image("/images/20230811.jpg");
              }
			  
			  
 
			  InputStream in;
			  
			  Commande commande = Messages.getCommande();
			  Model modele = commande.getModeleObj();
			  
			  System.out.println("modele : " + modele.getCheminVersModelSTR());
			  System.out.println("img : " + img);
			  System.out.println("img.getHeight() : " + img.getHeight());
			  System.out.println("file : " + file);
			  
			  if (img.getHeight() > img.getWidth()){
				  in = new FileInputStream(new File(modele.getModeleVertical()));
			  }
			  else{
				  in = new FileInputStream(new File(modele.getCheminVersModelSTR()));
			  }

		      IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in,TemplateEngineKind.Freemarker);

		      
		      FieldsMetadata metadata = report.createFieldsMetadata();
		      metadata.addFieldAsImage( "image_oeuvre", "image_oeuvre");
		      
		    //  OLD PAI
		 
		      metadata.addFieldAsList("fichiers.nom");
              metadata.addFieldAsList("fichiers.legende");
		      
		   // NEW API
	         // metadata.load( "fichiers", Fichier.class, true );
		      
		      report.setFieldsMetadata(metadata);
		      
		      System.out.println("__01");

		      IContext context = report.createContext();
		      context.put("inventaire", o.getCote_archives_6s());
		      context.put("titre", o.getTitre_de_l_oeuvre() != null ? o.getTitre_de_l_oeuvre() : "");
		      context.put("dimensions", o.getDimensions() != null ? o.getDimensions() : "");
		      //context.put("technique", o.getTechniquesUtilisees_noms_complets());
		      //context.put("matiere", o.getMatieresUtilisees_noms_complets());
		      
		      context.put("technique", "");
		      context.put("matiere", "");

		      context.put("inscriptions", o.getInscriptions_au_verso() != null ? o.getInscriptions_au_verso() : "");
		      
		      context.put("etat_final", ot.getEtat() != null ? ot.getEtat().toString() : "");
		      context.put("complement_etat", ot.getComplement_etat() != null ? ot.getComplement_etat().toString() : "");
		      
		      
		      ArrayList<String> traitementsEffectues = new ArrayList<>();
		      ArrayList<String> produitsAppliques = new ArrayList<>();
		      
		      for (TacheTraitement tt : ot.accesseurTraitementsAttendus_obj()){
		    	  
		    	  if(tt.getFait_() == Progression.FAIT_){
		    		  
		    		  traitementsEffectues.add(tt.getTraitement().getNom_complet() + (tt.getComplement() == null ? "" : " " + tt.getComplement()));  
		    		  
		    		  if (tt.getProduitsLies() != null){
		    			  //produitsAppliques.addAll(tt.getProduitsLies_names());
		    			  
		    			  for (String p_ : tt.getProduitsLies().keySet()){
		    				  
		    				  produitsAppliques.add(p_);
		    				  //produitsAppliques.add(p_.getNom_complet());
		    			  }
		    		  }

		    		  
		    		  
		    	  }
		      }
		      
		      System.out.println(produitsAppliques);
		      
		      context.put("produits", produitsAppliques);
		      
		      context.put("traitements", traitementsEffectues);
		      
		      context.put("alterations", ot.getAlterations());
		      
		      context.put("fichiers", listeFichiers);
		      
		      System.out.println("__02");
		      
		      context.put("bea", "Béatrice Alcade\n" +
                                 "Restauratrice du patrimoine\n" +
                                 "28 place Jean Jaurès\n" +
                                 "34400 Lunel - 06 21 21 15 40");
		      
		      context.put("client", "ARCHIVES MUNICIPALES DE LA SEYNE-SUR-MER\n" +
		    		                "Traverse Marius AUTRAN\n" +
		    		                "83 500 La Seyne-sur-Mer");
		      
		      context.put("commande", "RESTAURATION DE DOCUMENTS D'ARCHIVES FONDS CHARLY");
		      
		      System.out.println("__03");

		      //context.put("image_oeuvre", getLogo(ot));	
		      if (file.exists()){
		    	  context.put("image_oeuvre", file);
		      }
		      
		      // 3) Generate report by merging Java model with the Docx

		      OutputStream out = new FileOutputStream(Paths.get(modele.getCheminVersModelSTR()).getParent().resolve(String.format("%s.odt", o.getCote_archives_6s())).toFile());
		      
		      System.out.println("__04");
		      report.process(context, out);
		      
		      System.out.println("__05");

		      //OutputStream out2 = new FileOutputStream(new File(String.format("/home/kaplone/Desktop/BEABASE/tests/%s_freemarker.pdf", o.getCote_archives_6s())));
		      OutputStream out2 = new FileOutputStream(Paths.get(modele.getCheminVersModelSTR()).getParent().resolve(String.format("%s.pdf", o.getCote_archives_6s())).toFile());
              // 1) Create options ODT 2 PDF to select well converter form the registry
              Options options = Options.getFrom(DocumentKind.ODT).to(ConverterTypeTo.PDF);

              // 2) Get the converter from the registry
              IConverter converter = ConverterRegistry.getRegistry().getConverter(options);
   
              report.convert(context, options, out2);

		    } catch (IOException e) {
		      e.printStackTrace();
		    } catch (XDocReportException e) {
		      e.printStackTrace();
		    } 

	}

}

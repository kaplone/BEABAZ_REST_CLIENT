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
import java.util.stream.Collectors;

import javafx.scene.image.Image;

import org.bson.types.ObjectId;

import com.sun.javafx.iio.ImageStorage;

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
    //public static File  getLogo(OeuvreTraitee ot)
//    public static File  getLogo(ArrayList<Fichier> af)
//    {
//		//return MongoAccess.request("fichier", ot.getFichiers().get(ot.getFichiers().size() -2)).as(Fichier.class).next().getFichierLie();
//        return new File(af.get(af.size() -2).getFichierLie());
//    }

	public static void odt2pdf(Oeuvre o, OeuvreTraitee ot) {

		try {
		      // 1) Load Docx file by filling Velocity template engine and cache it to the registry
		      //InputStream in = new FileInputStream(new File("/home/kaplone/Desktop/BEABASE/Béa base/modele_rapport_v2_freemarker.odt"));
		      //InputStream in = new FileInputStream(new File("C:\\Users\\USER\\Desktop\\BEABAZ\\modele_rapport_v2_freemarker.odt"));
			  
              ArrayList<Fichier> listeFichiers = ot.getFichiers();
              listeFichiers.sort(new Comparator<Fichier>() {

					@Override
					public int compare(Fichier o1, Fichier o2) {
						
						String s1 = Paths.get(o1.getFichierLie()).getFileName().toString();
						String s2 = Paths.get(o2.getFichierLie()).getFileName().toString();
					
//						return String.format("%s_%02d", s1.split("\\.")[1], Integer.parseInt(s1.split("\\.")[2]))
//					.compareTo(String.format("%s_%02d", s2.split("\\.")[1], Integer.parseInt(s2.split("\\.")[2])));
						
						return s1.compareTo(s2);
					}
				});
            		  
		      
			  Image img = new Image("file:" + ot.getFichierAffiche().getFichierLie().toString());
			  File file = new File(ot.getFichierAffiche().getFichierLie());
 
			  InputStream in;
			  
			  Commande commande = Messages.getCommande();
			  Model modele = commande.getModel();
			  
			  if (img.getHeight() > img.getWidth()){
				  in = new FileInputStream(modele.getModeleVertical().toFile());
			  }
			  else{
				  in = new FileInputStream(modele.getCheminVersModel().toFile());
			  }

		      IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in,TemplateEngineKind.Freemarker);

		      
		      FieldsMetadata metadata = report.createFieldsMetadata();
		      metadata.addFieldAsImage( "image_oeuvre", "image_oeuvre");
		      
		    //  OLD PAI
		 
		      metadata.addFieldAsList("fichiers.nom");
              metadata.addFieldAsList("fichiers.legende");
		      
		   // NEW API
	        //  metadata.load( "fichiers", Fichier.class, true );
		      
		      report.setFieldsMetadata(metadata);
		      
		      System.out.println("__01");

		      IContext context = report.createContext();
		      context.put("inventaire", o.getCote_archives_6s());
		      context.put("titre", o.getTitre_de_l_oeuvre() != null ? o.getTitre_de_l_oeuvre() : "");
		      context.put("dimensions", o.getDimensions() != null ? o.getDimensions() : "");
		      context.put("technique", o.getTechniquesUtilisees_noms_complets());
		      context.put("matiere", o.getMatieresUtilisees_noms_complets());

		      context.put("inscriptions", o.getInscriptions_au_verso() != null ? o.getInscriptions_au_verso() : "");
		      
		      context.put("etat_final", ot.getEtat() != null ? ot.getEtat().toString() : "");
		      context.put("complement_etat", ot.getComplement_etat() != null ? ot.getComplement_etat().toString() : "");
		      
		      
		      ArrayList<String> traitementsEffectues = new ArrayList<>();
		      ArrayList<String> produitsAppliques = new ArrayList<>();
		      
		      for (ObjectId tt_id : ot.getTraitementsAttendus_id()){
		    	  
		    	  TacheTraitement tt = MongoAccess.request("tacheTraitement", tt_id).as(TacheTraitement.class).next();
		    	  
		    	  if(tt.getFait_() == Progression.FAIT_){
		    		  
		    		  traitementsEffectues.add(tt.getTraitement().getNom_complet() + (tt.getComplement() == null ? "" : " " + tt.getComplement()));  
		    		  
		    		  if (tt.getProduitsLies() != null){
		    			  for (ObjectId p : tt.getProduitsLies_id()){
		    				  
		    				  Produit p_ = MongoAccess.request("produit", p).as(Produit.class).next();
		    				  
		    				  produitsAppliques.add(p_.getNom_complet());
		    			  }
		    		  }

		    		  
		    		  
		    	  }
		      }
		      
		      context.put("produits", produitsAppliques.stream().collect(Collectors.joining(", ")));
		      
		      context.put("traitements", traitementsEffectues);
		      
		      context.put("alterations", ot.getAlterations());
		      
		      
		      
//		      ArrayList<String> listeFiles = new ArrayList<>();
//		      ArrayList<String> listeLegendes = new ArrayList<>();
//		      
//		      for (Fichier f : listeFichiers){
//		    	  
//		    	  listeFiles.add(f.getNom());
//		    	  listeLegendes.add(f.getLegende());
//		    	  
//		      }		      
//		      context.put("fichiers", listeFiles);
//		      context.put("legendes", listeLegendes);
		      
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
		      context.put("image_oeuvre", file);

		      // 3) Generate report by merging Java model with the Docx

		      OutputStream out = new FileOutputStream(modele.getCheminVersModel().getParent().resolve(String.format("%s.odt", o.getCote_archives_6s())).toFile());
		      
		      System.out.println("__04");
		      report.process(context, out);
		      
		      System.out.println("__05");

		      //OutputStream out2 = new FileOutputStream(new File(String.format("/home/kaplone/Desktop/BEABASE/tests/%s_freemarker.pdf", o.getCote_archives_6s())));
		      OutputStream out2 = new FileOutputStream(modele.getCheminVersModel().getParent().resolve(String.format("%s.pdf", o.getCote_archives_6s())).toFile());
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

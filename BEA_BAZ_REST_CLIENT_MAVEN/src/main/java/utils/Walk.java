package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.CopyOption;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.TreeMap;

import org.bson.types.ObjectId;

import application.Fiche_commande_import_controller;
import application.Main_BEA_BAZ;
import models.Fichier;
import models.OeuvreTraitee;

public class Walk {
	
	public static void walking(Path p) {
		
		System.out.println(p.toString());

		FileVisitor<Path> fileVisitor = new FileSizeVisitor(new Long(50));
		try {
			Files.walkFileTree(p, fileVisitor);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static class FileSizeVisitor implements FileVisitor<Path> {

		private Long size;

		public FileSizeVisitor(Long size) {
			this.size = size;
		}

		/**
		 * This is triggered before visiting a directory.
		 */
		@Override
		public FileVisitResult preVisitDirectory(Path path,
				BasicFileAttributes attrs) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		/**
		 * This is triggered when we visit a file.
		 */
		@Override
		public FileVisitResult visitFile(Path path, BasicFileAttributes attrs)
				throws IOException {

			if(path.toString().toLowerCase().endsWith(".jpg")){
			    distribuer(path);
			}
			

			return FileVisitResult.CONTINUE;
		}

		/**
		 * This is triggered if we cannot visit a Path We log the fact we cannot
		 * visit a specified path .
		 */
		@Override
		public FileVisitResult visitFileFailed(Path path, IOException exc)
				throws IOException {
			// We print the error
			System.err.println("ERROR: Cannot visit path: " + path);
			// We continue the folder walk
			return FileVisitResult.CONTINUE;
		}

		/**
		 * This is triggered after we finish visiting a specified folder.
		 */
		@Override
		public FileVisitResult postVisitDirectory(Path path, IOException exc)
				throws IOException {
			// We continue the folder walk
			return FileVisitResult.CONTINUE;
		}
		
		String base = System.getProperty("user.dir"); //retourne le chemin du package : /home/autor/workspace-scala/WALKING
		
		public void distribuer(Path path){

			String cote = path.getFileName().toString().split("\\.")[0];
			String temporalite = path.getFileName().toString().split("\\.")[1];
			String rang = path.getFileName().toString().split("\\.")[2];
			
			Fichier fichier = new Fichier();
			fichier.setFichierLie(path.toString());
			fichier.setNom(path.getFileName().toString());
			
			
			String legende = "";
			
            switch (temporalite){
            
            case "AR" : legende += "Avant traitement ";
            
                        switch (rang){
            
			            case "1" : legende += "recto";
			                        break;
			            case "2" : legende += "verso ";
			                        break;
			            default  : legende += "";
			                        break;
			            }
                        break;
            case "IR" : legende += "Durant traitement ";
                        break;
            case "PR" : legende += "Après traitement ";
            
			            switch (rang){
			            
			            case "1" : legende += "recto";
			                        break;
			            case "2" : legende += "verso ";
			                        break;
			            default  : legende += "";
			                        break;
			            }
                        break;
            }
            
			
			fichier.setLegende(legende);
			
			MongoAccess.save("fichier", fichier);

			OeuvreTraitee oeuvreConcernee = MongoAccess.request("oeuvreTraitee", "cote", cote).as(OeuvreTraitee.class);

			
			//Fiche_commande_import_controller.getBindLabel().set("Images en cours : " + path.getFileName().toString());
			
			if (oeuvreConcernee.getFichiers_id() == null){
				
				oeuvreConcernee.setFichiers_id(new TreeMap<String, ObjectId>());
			}
			
			oeuvreConcernee.getFichiers_id().put(Normalize.normalizeNormStringField(fichier.getNom()), fichier.get_id());
			
			OeuvreTraitee.update(oeuvreConcernee);
		}

	}

}

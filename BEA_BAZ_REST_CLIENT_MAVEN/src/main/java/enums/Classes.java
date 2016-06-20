package enums;

import models.Auteur;
import models.Client;
import models.Commande;
import models.Fichier;
import models.Matiere;
import models.Model;
import models.Oeuvre;
import models.OeuvreTraitee;
import models.Produit;
import models.TacheTraitement;
import models.Technique;
import models.Traitement;


public enum Classes {
	
	client (Client.class),
	auteur (Auteur.class),
	commande (Commande.class),
	traitement (Traitement.class),
	produit (Produit.class),
	technique (Technique.class),
	matiere (Matiere.class),
	fichier (Fichier.class),
	modele (Model.class),
	oeuvreTraitee (OeuvreTraitee.class),
	oeuvre (Oeuvre.class),
	tacheTraitement (TacheTraitement.class);
	
	private Class usedClass;
	
	Classes(Class c){
		this.usedClass = c;	
	}

	public Class getUsedClass() {
		return usedClass;
	}

}

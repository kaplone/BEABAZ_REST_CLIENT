package controleurs;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.type.TypeReference;

import application.JfxUtils;
import enums.EtatFinal;
import enums.Progression;
import utils.FreeMarkerMaker;
import utils.JsonUtils;
import utils.RestAccess;
import utils.RestObjectMapper;
import models.Auteur;
import models.Commande;
import models.Contexte;
import models.Fichier;
import models.Matiere;
import models.Messages;
import models.Model;
import models.Oeuvre;
import models.OeuvreTraitee;
import models.Produit;
import models.TacheTraitement;
import models.Technique;
import models.Traitement;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class Fiche_oeuvre_controller extends Fiche_controller implements Initializable {

	@FXML
	private ListView<Traitement> traitements_all_listView;
	@FXML
	private TableColumn<TacheTraitement, String> traitements_attendus_tableColumn;
	@FXML
	private TableColumn<TacheTraitement, ImageView> faits_attendus_tableColumn;
	@FXML
	private TableView<TacheTraitement> traitements_attendus_tableView;

	@FXML
	private ListView<String> fichiers_listView;

	@FXML
	private Polygon precedent_fleche;
	@FXML
	private Polygon suivant_fleche;

	@FXML
	private TextField numero_archive_6s_textField;
	@FXML
	private TextField titre_textField;
	@FXML
	private TextField date_oeuvre_textField;
	@FXML
	private TextField dimensions_textField;
	@FXML
	private TextArea inscriptions_textArea;
	@FXML
	private TextArea degradations_textArea;
	@FXML
	private TextArea remarques_textArea;
	@FXML
	private TextArea observations_textArea;

	@FXML
	private ListView<String> matieres_listView;
	@FXML
	private ListView<String> matieres_all_listView;

	@FXML
	private ListView<String> techniques_all_listView;
	@FXML
	private ListView<String> techniques_listView;

	@FXML
	private ImageView preview_imageView;

	@FXML
	private GridPane grid;
	@FXML
	private ChoiceBox<EtatFinal> etat_final_choiceBox;
	@FXML
	private TextArea complement_etat_textArea;

	@FXML
	private Button exporter_rapport_button;

	@FXML
	private Button add_m;
	@FXML
	private Button remove_m;

	@FXML
	private Button add_t;
	@FXML
	private Button remove_t;

	@FXML
	private Button add_tr;
	@FXML
	private Button remove_tr;

	@FXML
	private CheckBox defaut_checkBox;
	@FXML
	private VBox import_texte_vbox;
	@FXML
	private TextArea traitements_importes_textArea;

	private Oeuvre oeuvreSelectionneObj;
	private OeuvreTraitee oeuvreTraiteeSelectionneObj;

	private Auteur auteur;

	boolean directSelect = false;

	private ObservableList<String> matieres_all;
	private ObservableList<String> techniques_all;
	private ObservableList<String> fichiers;
	private ObservableList<Traitement> traitements_all;

	private ObservableList<String> matieresUtilisees_obs;
	private ObservableList<String> techniquesUtilisees_obs;
	private ObservableList<TacheTraitement> traitementsUtilisees_obs;
	private ObservableList<TacheTraitement> traitementsAttendus;

	private List<String> matieresUtilisees;
	private List<String> techniquesUtilisees;
	private List<String> traitementsUtilisees;
	
	private static List<Auteur> auteurs;

	private Map<String, Traitement> map_traitements;
	private Map<String, TacheTraitement> map_tacheTraitements;

	private ObservableList<EtatFinal> etatsFinaux;

	public void reloadOeuvre() {

		if (directSelect) {
			tableOeuvre.scrollTo(tableOeuvre.getSelectionModel().getSelectedIndex() - 9);
			oeuvreTraiteeSelectionneObj = OeuvreTraitee.retrouveOeuvreTraitee(new ObjectId(tableOeuvre.getSelectionModel().getSelectedItem().get("oeuvresTraitee_id")));
			Messages.setOeuvreTraiteeObj(oeuvreTraiteeSelectionneObj);
			oeuvreSelectionneObj = oeuvreTraiteeSelectionneObj.getOeuvre();
			matieresUtilisees = oeuvreSelectionneObj.getMatieresUtilisees_names();
			techniquesUtilisees = oeuvreSelectionneObj.getTechniquesUtilisees_names();
			traitementsUtilisees = oeuvreTraiteeSelectionneObj.getTraitementsAttendus_names();
			traitementsUtilisees_obs.setAll(oeuvreTraiteeSelectionneObj.getTraitementsAttendus_obj());
			traitementsUtilisees_obs.forEach(a -> map_tacheTraitements.put(a.getNom(), a));
		} else {
			tableOeuvre.scrollTo(tableOeuvre.getSelectionModel().getSelectedIndex());
		}

		numero_archive_6s_textField.setText(oeuvreSelectionneObj.getCote_archives_6s());
		titre_textField.setText(oeuvreSelectionneObj.getTitre_de_l_oeuvre());

		date_oeuvre_textField.setText(oeuvreSelectionneObj.getDate());
		dimensions_textField.setText(oeuvreSelectionneObj.getDimensions());
		inscriptions_textArea.setText(oeuvreSelectionneObj.getInscriptions_au_verso());
		degradations_textArea.setText(oeuvreTraiteeSelectionneObj.getAlterations().stream()
				.map(o -> o.replace("oui/non", "")).collect(Collectors.joining("\n")));
		observations_textArea.setText(oeuvreTraiteeSelectionneObj.getObservations());
		remarques_textArea.setText(oeuvreTraiteeSelectionneObj.getRemarques());

		etat_final_choiceBox.getSelectionModel().select(oeuvreTraiteeSelectionneObj.getEtat());
		complement_etat_textArea.setText(oeuvreTraiteeSelectionneObj.getComplement_etat());
		nom_label.setText(oeuvreSelectionneObj.getNom());

		// bouchon
		if (Integer.parseInt(oeuvreSelectionneObj.getCote_archives_6s()) % 2 == 0) {
			import_texte_vbox.setVisible(false);
			import_texte_vbox.setPrefHeight(0);
		} else {
			import_texte_vbox.setVisible(true);
			import_texte_vbox.setPrefHeight(44);
			traitements_importes_textArea
					.setText("Le numéro d'inventaire de la fiche est impair :\nTout ça vient de l'import ....");
		}

		afficherMatieres();
		afficherTechniques();
		afficherTraitements();
		afficherFichiers();
		afficherAuteurs();
	}

	public void afficherTraitements() {

		traitements_all.setAll(Messages.getTous_les_traitements());
		traitements_all.forEach(a -> map_traitements.put(a.getNom(), a));
		
		miseAJourAffichageTraitements();
	}

	public void miseAJourAffichageTraitements() {

		traitements_all_listView.setCellFactory(cell -> {

			return new ListCell<Traitement>() {

				@Override
				protected void updateItem(Traitement item, boolean empty) {
					super.updateItem(item, empty);

					if (item == null || empty) {
					}

					else {

						setText(item.getNom());

						if (traitementsUtilisees.contains(item.getNom())) {
							setDisable(true);
							setOpacity(0.5);
						}
						else {
							setDisable(false);
							setOpacity(1);
						}
					}
				}
			};
		});

		traitements_attendus_tableColumn.setCellValueFactory(new PropertyValueFactory<TacheTraitement, String>("nom"));
		faits_attendus_tableColumn.setCellValueFactory(new PropertyValueFactory<TacheTraitement, ImageView>("icone_progression"));
		traitements_attendus_tableView.setItems(traitementsUtilisees_obs);

		traitements_attendus_tableView.setOnMouseClicked(a -> {
			Messages.setTraitementSelectionne(traitements_attendus_tableView.getSelectionModel().getSelectedItem());
			Scene fiche_tache_traitement_scene = new Scene(
					(Parent) JfxUtils.loadFxml("/views/fiche_tache_traitement.fxml"),
					Contexte.largeurFenetre,
					Contexte.hauteurFenetre);
			fiche_tache_traitement_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			currentStage.setScene(fiche_tache_traitement_scene);
		});
	}

	public void ajouter_traitement(String m) {
		if (m != null) {
			TacheTraitement tt = new TacheTraitement(map_traitements.get(m));
			traitementsUtilisees_obs.add(tt);
			traitementsUtilisees.add(m);
			traitements_all_listView.getSelectionModel().select(null);
			traitements_all_listView.getSelectionModel().clearSelection();
			miseAJourAffichageTraitements();
			onEditerButton();
		}
	}

	public void retirer_traitement(String m) {
		if (m != null) {
			traitementsUtilisees_obs.remove(map_tacheTraitements.get(m));
			traitementsUtilisees.remove(m);
			miseAJourAffichageTraitements();
			onEditerButton();
		}
	}

	public void afficherAuteurs() {

		if (auteurs == null){
	    	observableAuteurs = FXCollections.observableArrayList();
	    	auteurs = Arrays.asList(Auteur.retrouveAuteurs());
	    	observableAuteurs.add(null);
	    	for (Auteur auteur : auteurs){
				observableAuteurs.add(auteur.getNom());
			}
	    }

	    
	    auteursChoiceBox.setItems(observableAuteurs);

		if (oeuvreSelectionneObj.getAuteur() != null){
			auteursChoiceBox.getSelectionModel().select(oeuvreSelectionneObj.getAuteur());
		}
	}

	@FXML
	public void onEditerOeuvreButton() {
		super.onEditerButton();
		editability(true);
	}

	@FXML
	public void onAnnulerEditButton() {
		super.onAnnulerEditButton();
		editability(false);
        
		directSelect = false;
		reloadOeuvre();
	}

	@FXML
	public void onMiseAJourOeuvreButton() {
		
		oeuvreSelectionneObj.setCote_archives_6s(numero_archive_6s_textField.getText());
		oeuvreSelectionneObj.setTitre_de_l_oeuvre(titre_textField.getText());
		oeuvreSelectionneObj.setDate(date_oeuvre_textField.getText());
		oeuvreSelectionneObj.setDimensions(dimensions_textField.getText());
		oeuvreSelectionneObj.setInscriptions_au_verso(inscriptions_textArea.getText());
		oeuvreSelectionneObj.setAuteur(auteursChoiceBox.getSelectionModel().getSelectedItem());

		if (degradations_textArea.getText() != null){
			String[] listeAlterations = degradations_textArea.getText().split(System.getProperty("line.separator"));
			oeuvreTraiteeSelectionneObj.setAlterations(new ArrayList<>(Arrays.asList(listeAlterations)));
		}
				
		oeuvreTraiteeSelectionneObj.setObservations(observations_textArea.getText());
		oeuvreTraiteeSelectionneObj.setRemarques(remarques_textArea.getText());
		oeuvreTraiteeSelectionneObj.setEtat(etat_final_choiceBox.getSelectionModel().getSelectedItem());
		oeuvreTraiteeSelectionneObj.setComplement_etat(complement_etat_textArea.getText());
		
		if (edit) {
			oeuvreTraiteeSelectionneObj.update("oeuvreTraitee");
			oeuvreSelectionneObj.update("oeuvre");
		}
		else {
			oeuvreTraiteeSelectionneObj.save("oeuvreTraitee", OeuvreTraitee.class);
			oeuvreSelectionneObj.save("oeuvre", Oeuvre.class);
		    //tableOeuvre.getSelectionModel().select(oeuvreTraiteeSelectionneObj.toString());
			//rafraichirAffichage();
		}
        
		super.onMiseAJourButton();
		editability(false);
	}

	@FXML
	public void onExporter_rapport_button() {

		FreeMarkerMaker.odt2pdf(oeuvreSelectionneObj, oeuvreTraiteeSelectionneObj);
	};

	public Comparator<OeuvreTraitee> otTrieeParNom = (OeuvreTraitee o1, OeuvreTraitee o2) -> o1.getNom()
			.compareTo(o2.getNom());

	public void afficherOeuvres() {

		obs_oeuvres = Contexte.getFiche_commande_controller().afficherOeuvres();
		oeuvres_nom_colonne.setCellValueFactory(
				data -> new SimpleStringProperty(data.getValue().get("oeuvresTraitee_string").toString()));
		oeuvres_fait_colonne.setCellValueFactory(data -> new SimpleObjectProperty<ImageView>(getImageView(data)));
		tableOeuvre.setItems(obs_oeuvres);
		
		tableOeuvre.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			directSelect = true;
			reloadOeuvre();
		});

	}

	public ImageView getImageView(CellDataFeatures<Map<String, String>, ImageView> data) {

		ImageView imv = new ImageView();
		imv.setFitWidth(20);
		imv.setFitHeight(20);
		imv.setImage(
				new Image(Progression.valueOf(data.getValue().get("oeuvresTraitee_etat").toString()).getUsedImage()));

		return imv;
	}

	public void afficherTechniques() {

		if (techniques_all.isEmpty()) {
			techniques_all.addAll(Arrays.asList(Technique.retrouveTechniques()).stream().map(a -> a.getNom())
					.collect(Collectors.toList()));
		}

		techniquesUtilisees_obs.clear();
		techniquesUtilisees_obs.addAll(techniquesUtilisees);
		techniques_listView.setItems(techniquesUtilisees_obs);

		miseAJourAffichageTechniques();

	}

	public void miseAJourAffichageTechniques() {

		techniques_all_listView.setCellFactory(cell -> {

			return new ListCell<String>() {

				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (item == null || empty) {
					}

					else {

						setText(item);

						if (techniquesUtilisees_obs.contains(item)) {
							setDisable(true);
							setOpacity(0.5);
						}
						else {
							setDisable(false);
							setOpacity(1);
						}
					}
				}
			};
		});
	}

	public void ajouter_technique(String m) {
		if (m != null) {
			techniquesUtilisees_obs.add(m);
			techniques_all_listView.getSelectionModel().select(null);
			techniques_all_listView.getSelectionModel().clearSelection();
			miseAJourAffichageTechniques();
			onEditerButton();
		}
	}

	public void retirer_technique(String m) {
		if (m != null) {
			techniquesUtilisees_obs.remove(m);
			miseAJourAffichageTechniques();
			onEditerButton();
		}
	}

	public void afficherMatieres() {

		if (matieres_all.isEmpty()) {
			matieres_all.addAll(Arrays.asList(Matiere.retrouveMatieres()).stream().map(a -> a.getNom())
					.collect(Collectors.toList()));
		}
	    
		matieresUtilisees_obs.setAll(matieresUtilisees);
		miseAJourAffichageMatieres();
	}

	public void miseAJourAffichageMatieres() {

		matieres_all_listView.setCellFactory(cell -> {

			return new ListCell<String>() {

				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (item == null || empty) {
					}

					else {

						setText(item);

						if (matieresUtilisees_obs.contains(item)) {
							setDisable(true);
							setOpacity(0.5);
						}
						else {
							setDisable(false);
							setOpacity(1);
						}
					}
				}
			};
		});
	}

	public void ajouter_matiere(String m) {
		if (m != null) {
			matieresUtilisees_obs.add(m);
			matieres_all_listView.getSelectionModel().select(null);
			matieres_all_listView.getSelectionModel().clearSelection();
			miseAJourAffichageMatieres();
			onEditerButton();
		}
	}

	public void retirer_matiere(String m) {
		if (m != null) {
			matieresUtilisees_obs.remove(m);
			miseAJourAffichageMatieres();
			onEditerButton();
		}
	}

	public void afficherFichiers() {

		List<String> contenu_fichiers = oeuvreTraiteeSelectionneObj.getFichiers().stream()
				.map(a -> a.get("fichier_string").replaceAll("_JPG", ".jpg")).collect(Collectors.toList());

		Collections.sort(contenu_fichiers);

		if (contenu_fichiers.stream().anyMatch(a -> a.contains("AR"))) {
			contenu_fichiers.add(0, "AR!");
		}
		if (contenu_fichiers.stream().anyMatch(a -> a.contains("IR"))) {
			contenu_fichiers.add((int) contenu_fichiers.stream().filter(b -> b.contains("AR")).count(), "IR!");
		}
		if (contenu_fichiers.stream().anyMatch(a -> a.contains("PR"))) {
			contenu_fichiers.add(
					(int) contenu_fichiers.stream().filter(b -> b.contains("AR") || b.contains("IR")).count(), "PR!");
		}

		fichiers.clear();
		fichiers_listView.setItems(fichiers);

		fichiers_listView.setCellFactory(cell -> {

			return new ListCell<String>() {

				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (item == null || empty) {
						setText(null);
					} else {
						switch (item) {
						case "AR!":
							setText("Avant restauration");
							setOpacity(0.5);
							setDisable(true);
							break;
						case "IR!":
							setText("Pendant restauration");
							setOpacity(0.5);
							setDisable(true);
							break;
						case "PR!":
							setText("Après restauration");
							setOpacity(0.5);
							setDisable(true);
							break;
						default:
							setText("   " + item);
							setOpacity(1);
						}
					}
				}
			};

		});

		fichiers.addAll(contenu_fichiers);

		String fichierSelectionne_str = RestAccess.request("fichier", "nom", oeuvreSelectionneObj.getCote_archives_6s(),
				true);
		Fichier fichierSelectionne = Fichier.fromJson(fichierSelectionne_str);
		File file = new File(String.format("file:%s", fichierSelectionne.getFichierLie().toString()));

		if (file.exists()) {
			preview_imageView
					.setImage(new Image(String.format("file:%s", fichierSelectionne.getFichierLie().toString())));
		} else {
			preview_imageView.setImage(new Image("images/scott-1.jpg"));
		}
	}

	// @FXML
	// public void onTraitementAttenduSelect(){
	//
	// TacheTraitement ttAtt =
	// traitements_attendus_all_tableView.getSelectionModel().getSelectedItem();
	// Messages.setTacheTraitement(ttAtt);
	//
	// Scene fiche_tache_traitement_scene = new Scene((Parent)
	// JfxUtils.loadFxml("/views/fiche_tache_traitement.fxml"),
	// Contexte.largeurFenetre, Contexte.hauteurFenetre);
	// fiche_tache_traitement_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	//
	// currentStage.setScene(fiche_tache_traitement_scene);
	//
	// }

	@FXML
	public void onFichierSelect() {

		String f = fichiers_listView.getSelectionModel().getSelectedItem();

		if (f != null) {
			Fichier fichier = Fichier.retrouveFichier(f);

			Messages.setFichier(fichier);

			Scene fiche_fichier_scene = new Scene((Parent) JfxUtils.loadFxml("/views/fiche_fichier.fxml"),
					Contexte.largeurFenetre, Contexte.hauteurFenetre);
			fiche_fichier_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			currentStage.setScene(fiche_fichier_scene);
		}
	}

	public void editability(boolean bool) {
		numero_archive_6s_textField.setEditable(bool);
		titre_textField.setEditable(bool);
		date_oeuvre_textField.setEditable(bool);
		dimensions_textField.setEditable(bool);
		inscriptions_textArea.setEditable(bool);
		degradations_textArea.setEditable(bool);
		observations_textArea.setEditable(bool);
		remarques_textArea.setEditable(bool);

		prompt(bool);
	}

	public void raz() {
		numero_archive_6s_textField.setText("");
		titre_textField.setText("");
		date_oeuvre_textField.setText("");
		dimensions_textField.setText("");
		inscriptions_textArea.setText("");
		degradations_textArea.setText("");
		observations_textArea.setText("");
		remarques_textArea.setText("");
	}

	public void prompt(boolean bool) {

		if (bool) {
			numero_archive_6s_textField.setPromptText("numéro d'inventaire (devrait être présent après l'import)");
			titre_textField.setPromptText("titre de l'oeuvre (devrait être présent après l'import)");
			date_oeuvre_textField.setPromptText("Date de l'oeuvre");
			dimensions_textField.setPromptText("Dimensions de l'oeuvre");
			inscriptions_textArea.setPromptText("Inscriptions");
			degradations_textArea.setPromptText("Dégradations");
			observations_textArea.setPromptText("Observations");
			remarques_textArea.setPromptText("éventuelles remarques");
		} else {
			numero_archive_6s_textField.setPromptText(null);
			titre_textField.setPromptText(null);
			date_oeuvre_textField.setPromptText(null);
			dimensions_textField.setPromptText(null);
			inscriptions_textArea.setPromptText(null);
			degradations_textArea.setPromptText(null);
			observations_textArea.setPromptText(null);
			remarques_textArea.setPromptText(null);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.init();
		editability(false);

		versOeuvreButton.setVisible(false);
		nouveau.setVisible(false);
		editer.setVisible(true);

		oeuvreTraiteeSelectionneObj = Messages.getOeuvreTraiteeObj();

		oeuvreSelectionneObj = oeuvreTraiteeSelectionneObj.getOeuvre();

		auteur = oeuvreSelectionneObj.getAuteur_obj();

		nom_label.setText(oeuvreSelectionneObj.getNom());

		numero_archive_6s_textField.setText(oeuvreSelectionneObj.getCote_archives_6s());
		titre_textField.setText(oeuvreSelectionneObj.getTitre_de_l_oeuvre());
		date_oeuvre_textField.setText(oeuvreSelectionneObj.getDate());
		dimensions_textField.setText(oeuvreSelectionneObj.getDimensions());
		inscriptions_textArea.setText(oeuvreSelectionneObj.getInscriptions_au_verso());
		degradations_textArea.setText(oeuvreSelectionneObj.get_observations());

		matieres_all = FXCollections.observableArrayList();
		matieres_all_listView.setItems(matieres_all);
		matieresUtilisees_obs = FXCollections.observableArrayList();
		matieres_listView.setItems(matieresUtilisees_obs);

		matieres_all_listView.onDragDetectedProperty().set(dd -> {

			Dragboard dragBoard = matieres_all_listView.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			content.putString(matieres_all_listView.getSelectionModel().getSelectedItem());
			dragBoard.setContent(content);

			matieres_listView.setOnDragOver(dd_ -> dd_.acceptTransferModes(TransferMode.MOVE));

			matieres_listView.setOnDragDropped(dd_ -> {
				ajouter_matiere(dd_.getDragboard().getString());
				dd_.setDropCompleted(true);
			});

		});

		matieres_all_listView.setOnDragDropped(dd_ -> dd_.setDropCompleted(true));

		matieres_listView.onDragDetectedProperty().set(dd -> {

			Dragboard dragBoard = matieres_listView.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			content.putString(matieres_listView.getSelectionModel().getSelectedItem());
			dragBoard.setContent(content);

			matieres_all_listView.setOnDragOver(dd_ -> dd_.acceptTransferModes(TransferMode.MOVE));

			matieres_all_listView.setOnDragDropped(dd_ -> {
				retirer_matiere(dd_.getDragboard().getString());
				dd_.setDropCompleted(true);
			});
		});

		matieresUtilisees = oeuvreSelectionneObj.getMatieresUtilisees_names();
		add_m.setOnAction(a -> ajouter_matiere(matieres_all_listView.getSelectionModel().getSelectedItem()));
		remove_m.setOnAction(a -> retirer_matiere(matieres_listView.getSelectionModel().getSelectedItem()));

		techniques_all = FXCollections.observableArrayList();
		techniques_all_listView.setItems(techniques_all);
		techniquesUtilisees_obs = FXCollections.observableArrayList();

		techniques_all_listView.onDragDetectedProperty().set(dd -> {

			Dragboard dragBoard = techniques_all_listView.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			content.putString(techniques_all_listView.getSelectionModel().getSelectedItem());
			dragBoard.setContent(content);

			techniques_listView.setOnDragOver(dd_ -> dd_.acceptTransferModes(TransferMode.MOVE));

			techniques_listView.setOnDragDropped(dd_ -> {
				ajouter_technique(dd_.getDragboard().getString());
				dd_.setDropCompleted(true);
			});

		});

		techniques_all_listView.setOnDragDropped(dd_ -> dd_.setDropCompleted(true));

		techniques_listView.onDragDetectedProperty().set(dd -> {

			Dragboard dragBoard = techniques_listView.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			content.putString(techniques_listView.getSelectionModel().getSelectedItem());
			dragBoard.setContent(content);

			techniques_all_listView.setOnDragOver(dd_ -> dd_.acceptTransferModes(TransferMode.MOVE));

			techniques_all_listView.setOnDragDropped(dd_ -> {
				retirer_technique(dd_.getDragboard().getString());
				dd_.setDropCompleted(true);
			});
		});

		techniquesUtilisees = oeuvreSelectionneObj.getTechniquesUtilisees_names();
		add_t.setOnAction(a -> ajouter_technique(techniques_all_listView.getSelectionModel().getSelectedItem()));
		remove_t.setOnAction(a -> retirer_technique(techniques_listView.getSelectionModel().getSelectedItem()));

		traitements_all = FXCollections.observableArrayList();
		traitements_all_listView.setItems(traitements_all);
		traitementsUtilisees_obs = FXCollections.observableArrayList();
		map_traitements = new HashMap<>();
		map_tacheTraitements = new HashMap<>();
		
		System.out.println("oeuvreTraiteeSelectionneObj : " + oeuvreTraiteeSelectionneObj);
		System.out.println("oeuvreTraiteeSelectionneObj.getTraitementsAttendus_obj() : " + oeuvreTraiteeSelectionneObj.getTraitementsAttendus_obj());
		
		traitementsUtilisees_obs.setAll(oeuvreTraiteeSelectionneObj.getTraitementsAttendus_obj());
		traitementsUtilisees_obs.forEach(a -> map_tacheTraitements.put(a.getNom(), a));

		traitements_all_listView.onDragDetectedProperty().set(dd -> {

			Dragboard dragBoard = traitements_all_listView.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			content.putString(traitements_all_listView.getSelectionModel().getSelectedItem().getNom());
			dragBoard.setContent(content);

			traitements_attendus_tableView.setOnDragOver(dd_ -> dd_.acceptTransferModes(TransferMode.MOVE));

			traitements_attendus_tableView.setOnDragDropped(dd_ -> {
				ajouter_traitement(dd_.getDragboard().getString());
				dd_.setDropCompleted(true);
			});

		});

		traitements_all_listView.setOnDragDropped(dd_ -> dd_.setDropCompleted(true));

		traitements_attendus_tableView.onDragDetectedProperty().set(dd -> {

			Dragboard dragBoard = traitements_attendus_tableView.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			content.putString(traitements_attendus_tableView.getSelectionModel().getSelectedItem().getNom());
			dragBoard.setContent(content);

			traitements_all_listView.setOnDragOver(dd_ -> dd_.acceptTransferModes(TransferMode.MOVE));

			traitements_all_listView.setOnDragDropped(dd_ -> {
				retirer_traitement(dd_.getDragboard().getString());
				dd_.setDropCompleted(true);
			});
		});

		traitementsUtilisees = oeuvreTraiteeSelectionneObj.getTraitementsAttendus_names();
		add_tr.setOnAction(
				a -> ajouter_traitement(traitements_all_listView.getSelectionModel().getSelectedItem().getNom()));
		remove_tr.setOnAction(
				a -> retirer_traitement(traitements_attendus_tableView.getSelectionModel().getSelectedItem().getNom()));

		etatsFinaux = FXCollections.observableArrayList(EtatFinal.values());
		etat_final_choiceBox.setItems(etatsFinaux);

		fichiers = FXCollections.observableArrayList();

		// complement_etat_textArea.textProperty().addListener((observable,
		// oldValue, newValue) -> {
		// onEditerOeuvreButton();
		// });

		// auteursChoiceBox.valueProperty().addListener((observable, oldValue,
		// newValue) -> {
		//
		// if(newValue != null && ! newValue.equals(auteur.getNom())){
		// auteur = Auteur.retrouveAuteur(newValue);
		// onEditerOeuvreButton();
		// }
		// });

		// etat_final_choiceBox.valueProperty().addListener((observable,
		// oldValue, newValue) -> {
		// onEditerOeuvreButton();
		// });

		afficherOeuvres();
		afficherMatieres();
		afficherTechniques();
		afficherTraitements();
		afficherAuteurs();
		afficherFichiers();
	}
}

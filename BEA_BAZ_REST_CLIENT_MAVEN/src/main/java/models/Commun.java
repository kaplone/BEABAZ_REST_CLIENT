package models;

import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Commun {
	
	@MongoId // auto
    @MongoObjectId
    private String _id;
	private String created_at;
	private String upStringd_at;
	private String deleted_at;
	
	private String nom;
	private String remarques;
	
	protected String stringResult;
	
	public void makeStringResult(){
		try {
			stringResult = getMapper().writeValueAsString(this);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
	}
	
	public abstract Commun save();
	public abstract void update();

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUpdated_at() {
		return upStringd_at;
	}

	public void setUpdated_at(String upStringd_at) {
		this.upStringd_at = upStringd_at;
	}

	public String getDeleted_at() {
		return deleted_at;
	}

	public void setDeleted_at(String deleted_at) {
		this.deleted_at = deleted_at;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getRemarques() {
		return remarques;
	}

	public void setRemarques(String remarques) {
		this.remarques = remarques;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}
	
	public String toString(){
		return this.nom;
	}
	
	public static ObjectMapper  getMapper(){
		return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

}

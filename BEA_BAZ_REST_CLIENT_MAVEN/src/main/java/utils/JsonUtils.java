package utils;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.collections.ObservableList;


public class JsonUtils {
	
	public static <T> T JsonToObj(String jsonString, TypeReference<T> typeReference_T){
		
		ObjectMapper om = new ObjectMapper();		
		T obj = null;

		try {
			obj = om.readValue(jsonString, new TypeReference<T>() {});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
    public static <T> ObservableList<T> JsonToListObj(String jsonString, ObservableList<T> liste_T, TypeReference<List<T>> typeReference_T){
    	
    	System.out.println(jsonString);

		ObjectMapper om = new ObjectMapper();
		List<T> obj;

		try {
			obj = om.readValue(jsonString, typeReference_T);
			liste_T.addAll(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return liste_T;
	}
}

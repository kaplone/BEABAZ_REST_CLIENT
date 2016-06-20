package utils;

import java.util.ArrayList;
import java.util.Arrays;

public class Normalize {

	public static String normalize (String s ){
		
		return s.replace('\'', '_')
		        .replace('\n', '_')
		        .replace(' ', '_')
		        .replace("°", "")
		        .replace("é", "e")
		        .replace("à", "a")
		        .replace("(", "")
		        .replace(")", "")
		        .replace("/", "_")
		        .replace("=", "_")
		        .toLowerCase();
	}
	
    public static String normalizeLight (String s ){
		
		return s.replace("\nOui/Non", "")
				.replace('\n', ' ')
				.toLowerCase();
	}

	public static Object normalizeField(String s) {
		return s.replace('"', ' ')
				.replace('.', '_');
	}
	
	public static String normalizeStringField(String s) {
		return s.replace('"', '\'')
				.replace('.', '_');
	}
	
	public static String normalizeNormStringField(String s) {
		return s.replace('.', '_');
	}
	
	public static String normalizeDenormStringField(String s) {
		return s.replace('_', '.');
	}
}

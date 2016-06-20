package models;

public class Settings {
	
	private static String adresse;
	private static String login;
	private static String pass;
	private static String version;
	private static String base;
	private static String key;
	
	public Settings(){
		this(null, null, null, null, null, null);
	}
		
	public Settings(String adresse, String login, String pass, String version, String base, String key) {
		Settings.adresse = adresse;
		Settings.login = login;
		Settings.pass = pass;
		Settings.version = version;
		Settings.base = base;
		Settings.key = key;
	}

	public static String getAdresse() {
		return adresse;
	}

	public static void setAdresse(String adresse) {
		Settings.adresse = adresse;
	}

	public static String getLogin() {
		return login;
	}

	public static void setLogin(String login) {
		Settings.login = login;
	}

	public static String getPass() {
		return pass;
	}

	public static void setPass(String pass) {
		Settings.pass = pass;
	}

	public static String getBase() {
		return base;
	}

	public static void setBase(String base) {
		Settings.base = base;
	}

	public static String getVersion() {
		return version;
	}

	public static void setVersion(String version) {
		Settings.version = version;
	}

	public static String getKey() {
		return key;
	}

	public static void setKey(String key) {
		Settings.key = key;
	}
	
}

package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class JfxUtils {
	
	public static Node loadFxml(String fxml) {
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(JfxUtils.class.getResource(fxml));
            Node root = (Node) loader.load(Main_BEA_BAZ.class.getResource(fxml).openStream());
            return root;
        } catch (IOException e) {
            throw new IllegalStateException("cannot load FXML screen", e);
        }
    }

}

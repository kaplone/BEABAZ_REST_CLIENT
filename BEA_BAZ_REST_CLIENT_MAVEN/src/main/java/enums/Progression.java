package enums;

import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public enum Progression {
	
	FAIT_ ("images/coche_ok.png"),
	TODO_ ("images/coche_todo.png"),
	NULL_ ("images/coche_null.png");
	
    private ImageView usedImage;
    private String f;
	
	Progression(String i) {
		
		f = i;
//        Image image = new Image(f.toURI().toString());
//        
//        usedImage = new ImageView();
//        usedImage.setFitHeight(15);
//        usedImage.setPreserveRatio(true);
//        usedImage.setImage(image);
	}
	
	public String getUsedImage() {
		return f;
	}

}

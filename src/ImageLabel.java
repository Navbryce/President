import java.awt.*;
import java.awt.event.MouseEvent.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.InputEvent.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class ImageLabel extends JLabel{
	private String fileName;
	ImageLabel(ImageIcon pictureAdd, String name){
		super(pictureAdd);
		fileName=name;
	}
	
	public String getFileName(){
		return fileName;
	}
	
}

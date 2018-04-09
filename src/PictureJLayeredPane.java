import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;
import java.util.ArrayList;

public class PictureJLayeredPane extends JLayeredPane {
	private JComponent primaryObject;
	private String rootPath;
	PictureJLayeredPane(int x, int y, JComponent mainObject, String rootPathString){
		primaryObject=mainObject;
		rootPath = rootPathString;
		setBounds(x, y, mainObject.getWidth(), mainObject.getHeight());
		add(mainObject, new Integer(10));
		mainObject.setVisible(true);
	}
	public void draw(String fileName, int xLocation, int yLocation, int layer){
		String path = rootPath + fileName;
		//String path="F:\\Computer Science 3-AP\\Unit2Part3\\Pictures\\" + fileName;
		System.out.println(fileName);
		ImageIcon picture=new ImageIcon(Toolkit.getDefaultToolkit().createImage(path));
		JLabel pictureAdd=new ImageLabel(picture, fileName);
		pictureAdd.setSize(new Dimension(picture.getIconWidth(), picture.getIconHeight()));
		add(pictureAdd, new Integer(layer));
		pictureAdd.setVisible(true);
		repaint();
		pictureAdd.setLocation(xLocation, yLocation);
	}
	public void updateSize(){
		setSize(primaryObject.getWidth(), primaryObject.getHeight());
	}
}

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.*;
import java.io.File;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.event.*;

public class Mosaique {

	static ArrayList<BufferedImage> imgs = new ArrayList<BufferedImage>();
	static BufferedImage[][] gallery;
	static Picture cool;
	static int h,v;
	static RGB[][] arr;
	static HashMap<BufferedImage, RGB> map;
	static BufferedImage save;
	static JFrame window;
	static Canvas canvas;
	static int sx;
	static int sy;
	static ArrayList<BufferedImage> pix = null;

	static BufferedImage original = null;
	static File saveLocation = null;
	static boolean preview = false;
	static Dimension screenSize;
	static int size = 5;

	public static void main(String args[]){
		try {
        	UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
    	} catch (Exception e) {}
    	screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		window = new JFrame("Mosaique");
		screenSize.height = screenSize.height - 100;
		screenSize.width = screenSize.width - 100;
		window.setBounds(0,0,screenSize.width, screenSize.height);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Menu
		JMenuBar menuBar = new JMenuBar();
		JSlider zoom = new JSlider();
		//File saving and getting
		JMenu menu = new JMenu("Setup");
		menu.getAccessibleContext().setAccessibleDescription("Get images");
		JMenuItem chooseOriginal = new JMenuItem("Choose original image");
		chooseOriginal.addActionListener(new ChooseOriginal());
		menu.add(chooseOriginal);
		JMenuItem choosePix = new JMenuItem("Choose image library");
		choosePix.addActionListener(new ChoosePix());
		menu.add(choosePix);
		JMenuItem chooseSaveLocation = new JMenuItem("Save as");
		chooseSaveLocation.addActionListener(new ChooseSave());
		menu.add(chooseSaveLocation);
		menuBar.add(menu);
		//Generation methods
		menu = new JMenu("Generate");
		menu.getAccessibleContext().setAccessibleDescription("Generate");
		JMenuItem generateNoPreview = new JMenuItem("Generate without preview");
		generateNoPreview.addActionListener(new GenerateNoPreview());
		menu.add(generateNoPreview);
		JMenuItem generateWithPreview = new JMenuItem("Generate with preview");
		generateWithPreview.addActionListener(new GenerateWithPreview());
		menu.add(generateWithPreview);
		JMenuItem generateNoSave = new JMenuItem("Generate without saving");
		generateNoSave.addActionListener(new GenerateNoSave());
		menu.add(generateNoSave);
		menuBar.add(menu);

		menu = new JMenu("Help");
		JMenuItem setup = new JMenuItem("Setup help");
		setup.addActionListener(new SetUp());
		menu.add(setup);
		JMenuItem generation = new JMenuItem("Generation help");
		generation.addActionListener(new Generation());
		menu.add(generation);
		JMenuItem sliderhelp = new JMenuItem("Slider help");
		sliderhelp.addActionListener(new SliderHelp());
		menu.add(sliderhelp);
		JMenuItem about = new JMenuItem("About");
		about.addActionListener(new About());
		menu.add(about);
		menuBar.add(menu);

		window.setJMenuBar(menuBar);
		JSlider slider = new JSlider(2, 15, 5);
		slider.addChangeListener(new SliderListener());
		canvas = new Canvas();
		canvas.setPreferredSize(screenSize);
		JScrollPane scrollPane = new JScrollPane(canvas, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setWheelScrollingEnabled(true);
		scrollPane.getVerticalScrollBar().setUnitIncrement(30);
		window.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_I) {
                    sx += 10;
                    sy += 10;
                    canvas.repaint();
                    canvas.revalidate();
                } else if (e.getKeyCode() == KeyEvent.VK_O) {
	                sx = Math.max(sx-10, size);
	                sy = Math.max(sy-10, size);
                    canvas.repaint();
                    canvas.revalidate();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        window.setFocusable(true);
		window.requestFocusInWindow();
        window.getContentPane().add(slider, BorderLayout.NORTH);
		window.getContentPane().add(scrollPane, BorderLayout.CENTER);
		window.setVisible(true);
	}	
	static class SliderListener implements ChangeListener {
	    public void stateChanged(ChangeEvent e) {
	        JSlider source = (JSlider)e.getSource();
	        if (!source.getValueIsAdjusting()) {
	            size = (int)source.getValue();
	            window.requestFocus();
	        }
	    }
	}
	public static class Canvas extends JPanel{
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			if(preview){
				if(gallery[0].length*sx > screenSize.width || gallery.length*sy > screenSize.height){
					this.setPreferredSize(new Dimension(gallery[0].length*sx, gallery.length*sy));
				} else {
					this.setPreferredSize(screenSize);
				}
				Color z = new Color(map.get(gallery[0][0]).getRed(), map.get(gallery[0][0]).getGreen(), map.get(gallery[0][0]).getBlue());
				g.setColor(z);
				g.fillRect(0, 0, 50, 50);
				for(int y = 0; y < gallery.length; y++){
					for(int x = 0; x < gallery[0].length; x++){
						g.drawImage(gallery[y][x] , x * sx,  y * sy, sx, sy, null);
					}
				}
			} else {
				this.setPreferredSize(screenSize);
				g.setFont(new Font("TimesRoman", Font.PLAIN, 50)); 
				g.setColor(Color.DARK_GRAY);
				g.fillRect(0, 0, screenSize.width, screenSize.height);
				g.setColor(Color.WHITE);
				g.drawString("Mosaique",screenSize.width/2-150, screenSize.height/2-50);
			}
		}
	}
	public static void saveImage(){
		save = new BufferedImage(h, v, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = save.createGraphics();
		Color z = new Color(map.get(gallery[0][0]).getRed(), map.get(gallery[0][0]).getGreen(), map.get(gallery[0][0]).getBlue());
		g.setColor(z);
		g.fillRect(0, 0, 50, 50);
		for(int y = 0; y < gallery.length; y++){
			for(int x = 0; x < gallery[0].length; x++){
				g.drawImage(gallery[y][x] , x * cool.getScaleX(),  y * cool.getScaleY(), cool.getScaleX(), cool.getScaleY(), null);
			}
		}
		try{
			if(saveLocation.getName().endsWith(".png")){
				ImageIO.write(save, "png", saveLocation);
			} else if(saveLocation.getName().endsWith(".jpg")){
				ImageIO.write(save, "jpg", saveLocation);
			} else if(saveLocation.getName().endsWith(".jpeg")){
				ImageIO.write(save, "jpeg", saveLocation);
			} else if(saveLocation.getName().endsWith(".bmp")){
				ImageIO.write(save, "bmp", saveLocation);
			} else if(saveLocation.getName().endsWith(".gif")){
				ImageIO.write(save, "gif", saveLocation);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public static class About implements ActionListener, ItemListener{
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(window, "Mosaique is a generative art program created by Rui Li, Alex Shi, Mahir Rahman and Ekim Karabey at Fraser Hacks 2017.");
    	}
    	public void itemStateChanged(ItemEvent e) {
	    }
	}
	public static class SetUp implements ActionListener, ItemListener{
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(window, "Choose original image: This will be the image you wish to transform. \n\nChoose image library: This will be the folder that contains images you wish to draw the original image with. \n\nSave location: This is the location where you will save the generated image if that is the option selected.");
    	}
    	public void itemStateChanged(ItemEvent e) {
	    }
	}
	public static class Generation implements ActionListener, ItemListener{
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(window, "Generate without preview: This will generate the image and save it directly without preview here. A save location must be specified.\n\nGenerate with preview: This will generate and save the image with a preview. A save location must be specified. \n\nGenerate without saving: This will let you preview the image, but won't save the image.\n\nPressing I and O zooms the picture in and out.");
    	}
    	public void itemStateChanged(ItemEvent e) {
	    }
	}
	public static class SliderHelp implements ActionListener, ItemListener{
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(window, "The slider changes the individual sizes of the images used to draw. Try changing the value and generating to see what occurs!");
    	}
    	public void itemStateChanged(ItemEvent e) {
	    }
	}
	public static class ChooseOriginal implements ActionListener, ItemListener{
		public void actionPerformed(ActionEvent e) {
        	JFileChooser fc = new JFileChooser();
        	fc.setCurrentDirectory(new java.io.File("."));
			int returnVal = fc.showSaveDialog(window);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
			    File yourFile = fc.getSelectedFile();
			    if(GetImage.accept(yourFile.getName())){
			    	try{
			    		original = ImageIO.read(yourFile);
			    		JOptionPane.showMessageDialog(window, "Picture set");
			    	}catch (Exception d){
			    		d.printStackTrace();
			    	}
			    } else {
			    	JOptionPane.showMessageDialog(window, "Select a valid image");
			    }
			}
    	}
    	public void itemStateChanged(ItemEvent e) {
	    }
	}
	public static class ChoosePix implements ActionListener, ItemListener{
		public void actionPerformed(ActionEvent e) {
        	JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new java.io.File("."));
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fc.showSaveDialog(window);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
			    File yourFolder = fc.getSelectedFile();
			    GetImage.dir = yourFolder;
			    pix = GetImage.getImages();
			    JOptionPane.showMessageDialog(window, "Pictures set\n(Make sure this folder has enough images otherwise results could be disappointing!)");
			}
    	}
    	public void itemStateChanged(ItemEvent e) {
	    }
	}
	public static class ChooseSave implements ActionListener, ItemListener{
		public void actionPerformed(ActionEvent e) {
        	JFileChooser fc = new JFileChooser();
        	fc.setCurrentDirectory(new java.io.File("."));
        	fc.setDialogType(JFileChooser.SAVE_DIALOG);
			int returnVal = fc.showSaveDialog(window);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				File yourFile = fc.getSelectedFile();
	        	if(GetImage.accept(yourFile.getName())){
	        		try{
	        			saveLocation = yourFile;
	        			JOptionPane.showMessageDialog(window, "Save location set");
	        		} catch (Exception d){
	        			d.printStackTrace();
	        		}
	        	} else {
	        		JOptionPane.showMessageDialog(window, "Please specify file type");
	        	}
        	}
    	}
    	public void itemStateChanged(ItemEvent e) {
	    }
	}
	public static class GenerateNoPreview implements ActionListener, ItemListener{
		public void actionPerformed(ActionEvent d) {
        	if(pix != null && pix.size() > 0 && original != null && saveLocation != null){
        		preview = false;
        		cool = new Picture(size, size);
				try{
					arr = cool.getRGBAverages(original);
				}catch(Exception e){
					e.printStackTrace();
				}
				map = new HashMap<BufferedImage,RGB>();
				for(int i = 0; i < pix.size(); i++){
					BufferedImage img = pix.get(i);
					map.put(img,cool.RGBAverage(img));
				}
				gallery = cool.buildImage(arr,map);
				h = arr[0].length*cool.getScaleX(); v = arr.length*cool.getScaleY();
				saveImage();
				//Zooming
				sx = cool.getScaleX();
				sy = cool.getScaleY();
				canvas.repaint();
				canvas.revalidate();
        	} else {
	        	JOptionPane.showMessageDialog(window, "Please finish setup");
	        }
    	}
    	public void itemStateChanged(ItemEvent e) {
	    }
	}
	public static class GenerateWithPreview implements ActionListener, ItemListener{
		public void actionPerformed(ActionEvent d) {
        	if(pix != null && pix.size() > 0 && original != null && saveLocation != null){
        		preview = true;
        		cool = new Picture(size, size);
				try{
					arr = cool.getRGBAverages(original);
				}catch(Exception e){
					e.printStackTrace();
				}
				map = new HashMap<BufferedImage,RGB>();
				for(int i = 0; i < pix.size(); i++){
					BufferedImage img = pix.get(i);
					map.put(img,cool.RGBAverage(img));
				}
				gallery = cool.buildImage(arr,map);
				h = arr[0].length*cool.getScaleX(); v = arr.length*cool.getScaleY();
				saveImage();
				//Zooming
				sx = cool.getScaleX();
				sy = cool.getScaleY();
				canvas.repaint();
				canvas.revalidate();
        	} else {
		        JOptionPane.showMessageDialog(window, "Please finish setup");
		    }
		}
    	public void itemStateChanged(ItemEvent e) {
	    }
	}
	public static class GenerateNoSave implements ActionListener, ItemListener{
		public void actionPerformed(ActionEvent d) {
        	if(pix != null && pix.size() > 0 && original != null){
        		preview = true;
        		cool = new Picture(size, size);
				try{
					arr = cool.getRGBAverages(original);
				}catch(Exception e){
					e.printStackTrace();
				}
				map = new HashMap<BufferedImage,RGB>();
				for(int i = 0; i < pix.size(); i++){
					BufferedImage img = pix.get(i);
					map.put(img,cool.RGBAverage(img));
				}
				gallery = cool.buildImage(arr,map);
				h = arr[0].length*cool.getScaleX(); v = arr.length*cool.getScaleY();
				//Zooming
				sx = cool.getScaleX();
				sy = cool.getScaleY();
				canvas.repaint();
				canvas.revalidate();
        	} else {
	        	JOptionPane.showMessageDialog(window, "Please finish setup");
	        }
    	}
    	public void itemStateChanged(ItemEvent e) {
	    }
	}
}

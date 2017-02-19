import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.*;
import javax.swing.*;
import java.awt.*;
public class GetImage{

    static File dir = new File("C:/");
    static final String[] EXTENSIONS = new String[]{
        "gif", "png", "bmp", "jpg", "jpeg" // and other formats you need
    };

    // filter to identify images based on their extensions
    static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {
        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };

    //Set file location
    public static void setFileLocation(String cd){
        dir = new File(cd);
    }

    public static ArrayList<BufferedImage> getImages () {
    	ArrayList<BufferedImage> out = new ArrayList<BufferedImage>();
        if (dir.isDirectory()) { 
            for (final File f : dir.listFiles(IMAGE_FILTER)) {
                BufferedImage img = null;
                try {
                    img = ImageIO.read(f);
                    out.add(img);  
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return out;
    }

    public static BufferedImage getOriginal(String directory){
        BufferedImage image = null;
        try{
            image = ImageIO.read(new File(directory));
        }catch(Exception e){
            e.printStackTrace();
        }
        return image;
    }

    /*public static void main(String[] args){
    	JFrame frame = new JFrame();
    	ArrayList<BufferedImage> list = getImages();
    	BufferedImage img = list.get(1);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(new ImageIcon(img)));
		frame.pack();
		frame.setVisible(true);
		while(true){}
    }*///TODE
}

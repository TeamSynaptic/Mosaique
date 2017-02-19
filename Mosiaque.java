import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.*;
import java.io.File;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.*;

public class Mosiaque {

	static ArrayList<BufferedImage> imgs = new ArrayList<BufferedImage>();
	static BufferedImage[][] gallery;
	static Picture cool;
	static int h,v;
	static RGB[][] arr;
	static HashMap<BufferedImage, RGB> map;

	public static void main(String args[]){
		JFrame window = new JFrame();
		h = 800; v = 800;
		window.setSize(h,v);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GetImage.setFileLocation("/home/lx_user/pictures");
		ArrayList<BufferedImage> pix = GetImage.getImages();
		cool = new Picture(10,10);
		try{
			arr = cool.getRGBAverages(cool.convertTo2DUsingGetRGB(ImageIO.read(new File("/home/lx_user/pictures/zz.jpg"))));
		}catch(Exception e){
			e.printStackTrace();
		}
		map = new HashMap<BufferedImage,RGB>();
		for(int i = 0; i < pix.size(); i++){
			BufferedImage img = pix.get(i);
			map.put(img,cool.RGBAverage(cool.convertTo2DUsingGetRGB(img)));
		}
		gallery = cool.buildImage(arr,map);
		window.add(new Canvas());
		window.setVisible(true);
	}	

	public static class Canvas extends JPanel{
		public void paintComponent(Graphics g){
			Color z = new Color(map.get(gallery[0][0]).getRed(), map.get(gallery[0][0]).getGreen(), map.get(gallery[0][0]).getBlue());
			g.setColor(z);
			g.fillRect(0, 0, 50, 50);
			for(int y = 0; y < gallery.length; y++){
				for(int x = 0; x < gallery[0].length; x++){
					g.drawImage(gallery[y][x] , x * cool.getScaleX(),  y * cool.getScaleY(), cool.getScaleX(), cool.getScaleY(), null);
				}
			}
		}
	}
}

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.*;
import java.util.*;

public class Picture {
	private int scalex;
	private int scaley;
	public Picture(int sx, int sy){
		scalex = sx;
		scaley = sy;
	}
   //Change int array to RGB data
   public RGB[][] getRGBAverages(BufferedImage image){
   	RGB[][] result = new RGB[image.getHeight()/scaley][image.getWidth()/scalex];
   	int redAverage = 0;
   	int greenAverage = 0;
   	int blueAverage = 0;
   	int idx = 0; //Current index of x
   	int idy = 0; //Current index of y
   	for(int i = 0; i < image.getHeight()/scaley; i++){ //Search each section
   		for(int j = 0; j < image.getWidth()/scalex; j++){
		   	Color c = new Color(image.getRGB(idx+scalex/2, idy+scaley/2));
   			idx += scalex; //Move on to next x section
   			result[i][j] = new RGB(c.getRed(), c.getBlue(), c.getGreen());
   		}
   		idy += scaley; //Move on to next y section, reset x index
   		idx = 0;
   	}
   	return result;
   }
	//Change int array to RGB average per image
	//Same algorithm as above, but don't need to store each section. Just get overall average
	public RGB RGBAverage(BufferedImage image){
		int redAverage = 0;
		int greenAverage = 0;
		int blueAverage = 0;
		int idx = 0;
		int idy = 0;
		for(int i = 0; i < image.getHeight()/scaley; i++){
			for(int j = 0; j < image.getWidth()/scalex; j++){
				for(int y = 0; y <  scaley; y++){
					for(int x = 0; x < scalex; x++){
	   				Color c = new Color(image.getRGB(idx+x, idy+y));
	   				redAverage += c.getRed();
	   				blueAverage += c.getBlue();
	   				greenAverage += c.getGreen();
   				}
				}
				idx += scalex;
			}
			idy += scaley;
			idx = 0;
		}
		redAverage = (int)Math.round(redAverage / Double.valueOf((image.getHeight() * image.getWidth())));
		blueAverage = (int)Math.round(blueAverage / Double.valueOf((image.getHeight() * image.getWidth())));
		greenAverage = (int)Math.round(greenAverage / Double.valueOf((image.getHeight() * image.getWidth())));
		RGB result = new RGB(redAverage, blueAverage, greenAverage);
		return result;
	}
	//Compare each images RGB and get smallest difference
	public BufferedImage[][] buildImage(RGB[][] original, HashMap<BufferedImage, RGB > images){
		BufferedImage[][] result = new BufferedImage[original.length][original[0].length]; //Image array for drawing later based on scalex & scaley
		for(int i = 0; i < original.length; i++){
			for(int j = 0; j < original[i].length; j++){
				BufferedImage bi = null; //Image to add
				double diff = -1;
				for(Map.Entry<BufferedImage, RGB> entry : images.entrySet()){
					if(diff == -1){ //Set default as the first entry
                  double distance = Math.sqrt(Math.pow(entry.getValue().getRed() - original[i][j].getRed(),2) + Math.pow(entry.getValue().getGreen() - original[i][j].getGreen(),2) + Math.pow(entry.getValue().getBlue() - original[i][j].getBlue(),2));
                  diff = distance;
						bi = entry.getKey();
					} else {
						double distance = Math.sqrt(Math.pow(entry.getValue().getRed() - original[i][j].getRed(),2) + Math.pow(entry.getValue().getGreen() - original[i][j].getGreen(),2) + Math.pow(entry.getValue().getBlue() - original[i][j].getBlue(),2));
						if(distance <= diff){ //If the difference is smaller, set image as this one
							diff = distance;
							bi = entry.getKey();
						}
					}
				}
				result[i][j] = bi;//This is image
			}
		}
		return result;
	}
	public int getScaleX(){
		return scalex;
	}
	public int getScaleY(){
		return scaley;
	}
	public void setScaleX(int sx){
		scalex = sx;
	}
	public void setScaleY(int sy){
		scaley = sy;
	}
}
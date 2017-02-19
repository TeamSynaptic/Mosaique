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
	//Get the integer array of RGB values
	public int[][] convertTo2DUsingGetRGB(BufferedImage image) {
      int width = image.getWidth();
      int height = image.getHeight();
      int[][] result = new int[height][width];
      for (int row = 0; row < height; row++) {
         for (int col = 0; col < width; col++) {
            result[row][col] = image.getRGB(col, row);
         }
      }
      return result;
   	}
   	//Change int array to RGB data
   	public RGB[][] getRGBAverages(int[][] rgb){
   		RGB[][] result = new RGB[rgb.length/scaley][rgb[0].length/scalex];
   		int redAverage = 0;
   		int greenAverage = 0;
   		int blueAverage = 0;
   		int idx = 0; //Current index of x
   		int idy = 0; //Current index of y
   		for(int i = 0; i < rgb.length/scaley; i++){ //Search each section
   			for(int j = 0; j < rgb[i].length/scalex; j++){
		   		Color c = new Color(rgb[idy+scaley/2][idx+scalex/2]);
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
   	public RGB RGBAverage(int[][] rgb){
   		int redAverage = 0;
   		int greenAverage = 0;
   		int blueAverage = 0;
   		int idx = 0;
   		int idy = 0;
   		for(int i = 0; i < rgb.length/scaley; i++){
   			for(int j = 0; j < rgb[i].length/scalex; j++){
   				for(int y = 0; y <  scaley; y++){
   					for(int x = 0; x < scalex; x++){
		   				Color c = new Color(rgb[idy+y][idx+x]);
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
   		redAverage = (int)Math.round(redAverage / Double.valueOf((rgb.length * rgb[0].length)));
   		blueAverage = (int)Math.round(blueAverage / Double.valueOf((rgb.length * rgb[0].length)));
   		greenAverage = (int)Math.round(greenAverage / Double.valueOf((rgb.length * rgb[0].length)));
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
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.*;
import java.util.*;

public class Picture {
	static int scalex;
	static int scaley;
	public Picture(int sx, int sy){
		scalex = sx;
		scaley = sy;
	}
	//Get the integer array of RGB values
	public static int[][] convertTo2DUsingGetRGB(BufferedImage image) {
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
   	public static RGB[][] getRGBAverages(int[][] rgb){
   		RGB[][] result = new RGB[rgb.length/scaley][rgb[0].length/scalex];
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
   				result[i][j] = new RGB(redAverage, blueAverage, greenAverage);
   			}
   			idy += scaley;
   			idx = 0;
   		}
   		for(int i = 0; i < result.length; i++){
   			for(int j = 0; j < result[i].length; j++){
   				System.out.println(result[i][j].red + " " + result[i][j].blue + " " + result[i][j].green);
   				result[i][j].red = result[i][j].red / (scalex * scaley);
   				result[i][j].blue = result[i][j].blue / (scalex * scaley);
   				result[i][j].green = result[i][j].green / (scalex * scaley);
   			}
   		}
   		return result;
   	}
   	//Change int array to RGB average per image
   	public static RGB RGBAverage(int[][] rgb){
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
   		redAverage = redAverage / (rgb.length * rgb[0].length);
   		blueAverage = blueAverage / (rgb.length * rgb[0].length);
   		greenAverage = greenAverage / (rgb.length * rgb[0].length);
   		RGB result = new RGB(redAverage, blueAverage, greenAverage);
   		return result;
   	}
   	//Compare each images RGB and get smallest difference
   	public static BufferedImage[][] buildImage(RGB[][] original, HashMap<BufferedImage, RGB> images){
   		BufferedImage[][] result = new BufferedImage[original.length][original[0].length];
   		for(int i = 0; i < original.length; i++){
   			for(int j = 0; j < original[i].length; j++){
   				BufferedImage bi = null;
   				int diff = -1;
   				for(Map.Entry<BufferedImage, RGB> entry : images.entrySet()){
   					if(diff == -1){
   						diff = (Math.abs(entry.getValue().red - original[i][j].red)) 
   						+ (Math.abs(entry.getValue().green - original[i][j].green)) 
   						+ (Math.abs(entry.getValue().blue - original[i][j].blue));
   						bi = entry.getKey();
   					} else {
   						int curr = (Math.abs(entry.getValue().red - original[i][j].red)) 
   						+ (Math.abs(entry.getValue().green - original[i][j].green)) 
   						+ (Math.abs(entry.getValue().blue - original[i][j].blue));
   						if(curr < diff){
   							diff = curr;
   							bi = entry.getKey();
   						}
   					}
   				}
   				result[i][j] = bi;
   			}
   		}
   		return result;
   	}
}
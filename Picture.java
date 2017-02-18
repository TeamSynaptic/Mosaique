import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.*;

public class Picture {
	static int scalex;
	static int scaley;
	public Picture(int sx, int sy){
		scalex = sx;
		scaley = sy;
	}
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
}
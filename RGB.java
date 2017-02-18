public class RGB {
	private int red;
	private int blue;
	private int green;
	public RGB(int r, int b, int g){
		red = r;
		blue = b;
		green = g;
	}
	public RGB(){}
	public int getRed(){
		return red;
	}
	public int getBlue(){
		return blue;
	}
	public int getGreen(){
		return green;
	}
	public void setRed(int r){
		red = r;
	}
	public void setBlue(int b){
		blue = b;
	}
	public void setGreen(int g){
		green = g;
	}
}
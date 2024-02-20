package GeoConvertUtils;

public class MainClass {
	
	public static void main(String[] args){
		Geographic g = new Geographic(Math.toRadians(33) ,Math.toRadians(43), 0);
		Geographic gg = new Geographic(Math.toRadians(33) ,Math.toRadians(44), 0);
		System.out.println(g.distVincenty(gg));
	}

}

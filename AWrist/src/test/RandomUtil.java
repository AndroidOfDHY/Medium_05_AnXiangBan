package test;

import java.util.Random;

public class RandomUtil {
public static Random random;
	
	static{
		random = new Random();
	}
	public static float getTW() {
		int max =95 ;
		float jkmin = 36.5f;
		float jkmax = 37.5f; 
		float bjkmin = 37.6f;
		float bjkmax = 42f;
		double gl = random.nextDouble()*100;	
		Double double1 = random.nextDouble();
		if(gl<max){
			return (float) (double1*(jkmax-jkmin)+jkmin);
		}else{
			return (float) (double1*(bjkmax-bjkmin)+bjkmin);
		}
    }
	public static int getXL() {
		int max =95 ;
		float jkmin = 50f;
		float jkmax = 120f; 
		float bjkmin = 130f;
		float bjkmax = 150f;
		double gl = random.nextDouble()*100;	
		Double double1 = random.nextDouble();	
		if(gl<max){
			return (int) (double1*(jkmax-jkmin)+jkmin);
		}else{
			return (int) (double1*(bjkmax-bjkmin)+bjkmin);
		}
    }
	public static float getSZY() {
		int max =95 ;
		float jkmin = 76.5f;
		float jkmax = 85.5f; 
		float bjkmin = 85.6f;
		float bjkmax = 95.5f;
		double gl = random.nextDouble()*100;	
		Double double1 = random.nextDouble();
		if(gl<max){
			return (float) (double1*(jkmax-jkmin)+jkmin);
		}else{
			return (float) (double1*(bjkmax-bjkmin)+bjkmin);
		}
    }
	public static float getSSY() {
		int max =95 ;
		float jkmin = 115.5f;
		float jkmax = 124.5f; 
		float bjkmin = 124.6f;
		float bjkmax = 134.5f;
		double gl = random.nextDouble()*100;	
		Double double1 = random.nextDouble();
		if(gl<max){
			return (float) (double1*(jkmax-jkmin)+jkmin);
		}else{
			return (float) (double1*(bjkmax-bjkmin)+bjkmin);
		}
    }
}

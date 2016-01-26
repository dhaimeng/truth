package Analysis;
import java.util.Scanner; 
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/*
 * 从信息熵，离散程度两个方面分析数据的质量
 * @author liuwenqiang
 * @time   2014年12月
 */

public class Dataqulity {
	/*
	 * 从信息熵分析数据的质量
	 * @author liuwenqiang
	 * @time   2014年12月
	 */
	public static double Entropy(int[] entropy) { 
		   double H = 0.0; 
		   int sum = 0; 
		   int[] letter = entropy;
		   for(int i=0;i<letter.length;i++){
			   sum+=letter[i];
		   }
		   for (int i = 0; i<letter.length; i++) {
			   double p = 1.0 * letter[i] / sum; 
			   H += -(p * Math.log(p) / Math.log(2));
			 }
		   H=H/(Math.log(letter.length) / Math.log(2));
		   return H; 
	}
	/*
	 * 从离散程度方面分析数据的质量
	 * @author liuwenqiang
	 * @time   2014年12月
	 */
	public static double deviation(double[] entropy) { 
		   double H = 0.0; 
		   int sum = 0; 
		   double[] letter = entropy;
		   for (int i = 1; i<letter.length; i++) {
			   double p = 1.0 *( letter[i]-letter[0])/letter[0]; 
			   H += p*p;
			 }
		   H=H/(letter.length-1);
		   H=Math.sqrt(H);
		   return H; 
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double []s = null;
		while(true){
			System.out.print("请输入Int类型数组数据,第一个为比较值");
			java.util.Scanner input=new java.util.Scanner(System.in);
			String sstrString = input.next();  
			  String[] cchar = sstrString.split(",");
			  s=new double[cchar.length];
			  for (int i = 0; i < cchar.length; i++) {  
		             s[i]= Double.valueOf(cchar[i]);
		        }  
			  System.out.print("离散程度："+deviation(s)+"\n");
		}
	}
}

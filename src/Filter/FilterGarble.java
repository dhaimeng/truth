package Filter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class FilterGarble{
	/**
     * 
     * @param filePath 要读取的文件路径  过滤的读取所需要的文件
     * @param urlname  指向该url的行将被删除掉
     * @return 文件字符串
     */
    public static void readFile(String filePath,String dir){
    	File file = new File(filePath);   
		String[] filelist = file.list();
		FileWriter fw = null;
		try {
			for(int i=0;i<filelist.length;i++){
				System.out.println("Processing the "+filelist[i].toString());
				FileReader fr = new FileReader(filePath+filelist[i]);
				BufferedReader br = new BufferedReader(fr);
				StringBuffer sb=new StringBuffer();
				String pre = br.readLine();
				String next = br.readLine();
				while (next != null) {
					if(pre.indexOf("\\u")==-1 && pre.indexOf("$")==-1 && pre.indexOf("\\x")==-1 && pre.indexOf("\\n")==-1 && pre.indexOf("@tr")==-1&& pre.indexOf("@de")==-1&& pre.indexOf("@it")==-1&& pre.indexOf("@no")==-1&& pre.indexOf("@ko")==-1&& pre.indexOf("@ca")==-1&& pre.indexOf("@fi")==-1&& pre.indexOf("@sv")==-1&& pre.indexOf("@ja")==-1&& pre.indexOf("@pt")==-1&& pre.indexOf("@zh")==-1&& pre.indexOf("@nl")==-1&& pre.indexOf("@fr")==-1&& pre.indexOf("@uk")==-1 && pre.indexOf("@cs")==-1&& pre.indexOf("@vi")==-1&& pre.indexOf("@hu")==-1&& pre.indexOf("@ru")==-1&& pre.indexOf("@pl")==-1 )
  					  sb=sb.append(pre+"\n");
  		        else{
  			           if(pre.indexOf("@en")!=-1)
  							sb=sb.append(pre+"\n");
  		            }
					pre = next;
					next=br.readLine();
  		          }
				String s=sb.toString();
		    	try{
		    		fw=new FileWriter(filePath+filelist[i]+".nx");
		        	BufferedWriter bw=new BufferedWriter(fw);
		        	bw.write(s);
		        	bw.close();
		        	fw.close();
		    	}catch(Exception e){
		    		e.printStackTrace();
		    	}
		    	//System.out.println(s);
		    	sb.delete(0, sb.length());
		    	sb=new StringBuffer();
				}
			}catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
		}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String fileName="E:\\快盘\\当前任务\\任务1-Trust问题\\Dataset\\Location\\分析50个统计（Synetic 数据）\\分析统计10个\\11\\";  //url的存储位置路径
		readFile(fileName,fileName);
		System.out.print("ok");
	}
}

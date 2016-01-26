package Filter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
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
public class Crawler {
	/*
	 * 抽取每一个实体中的SameAs三个实体（由于New York Time中的实体只包含DBpedia，Freebase，Geonames）,并把每一个实体的数据爬取下来；
	 */
	public static ArrayList UrlsWrong=null;
	public ArrayList SameAs(String inputFileName) throws IOException{
		ArrayList UrlsName=new ArrayList();   //存放最后的结果
		File file = new File(inputFileName);    //读取文件夹
		String[] filelist = file.list();
		String[] Result=null;
		for(int i=0;i<filelist.length;i++)  //读取每一个文件
		{
			System.out.println("Processing the "+filelist[i].toString());
			FileReader fr = new FileReader(inputFileName+filelist[i]);
			BufferedReader br = new BufferedReader(fr);
			String pre = br.readLine();
			String next = br.readLine();
			String pre_prefix = null;
			while (next!= null) {
				String[] SPO =pre.split(" ");
				if(SPO[1].indexOf("sameAs")!=-1 && SPO[2].indexOf("data.nytimes.com")==-1)
				{
					UrlsName.add(SPO[2]);	
				}
				pre = next;
				next = br.readLine();
			}
		}
		return UrlsName;	
	}
	public void CrawlerData(ArrayList UrlsName,String desFiledir) throws IOException{
		ArrayList UrlsFilter=new ArrayList();   //存放最后的结果
	    for(int i=0;i<UrlsName.size();i++){  //修改URl方便下载
		 String Urls=UrlsName.get(i).toString().substring(1, UrlsName.get(i).toString().indexOf(">"));
		  if(Urls.indexOf("dbpedia.org")!=-1){
			  UrlsFilter.add(Urls.replaceAll("resource", "data"));
		  }else if(Urls.indexOf("geonames")!=-1){
			  UrlsFilter.add(Urls+"about.rdf");
		  }else{
			  
				  try {
					UrlsFilter.add("https://www.googleapis.com/freebase/v1/rdf/en/"
									+ Urls.substring(Urls.indexOf("en.") + 3));
				} catch (Exception e) {
					// TODO: handle exception
				}
			   
		  }
	 }
	 for(int i=0;i<UrlsFilter.size();i++){
		 int bytesum = 0;
	     int byteread = 0;
			//设置代理服务器
			System.getProperties().put("proxySet", "true");
			System.getProperties().put("proxyHost", "127.0.0.1");
			System.getProperties().put("proxyPort", "8580");
	     try {
			URL url = new URL(UrlsFilter.get(i).toString());
			URLConnection conn = url.openConnection();
			InputStream inStream = conn.getInputStream();
			FileOutputStream fs = new FileOutputStream(desFiledir + i + "_" + i+ ".txt");
			byte[] buffer = new byte[1024];
			int length;
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				System.out.println(bytesum);
				fs.write(buffer, 0, byteread);
			}
		} catch (Exception e) {
			// TODO: handle exception
			 //UrlsWrong.add((UrlsFilter.get(i).toString()));
		}
		System.out.print("下载第"+(i+1)+"个文件");
	 }
     System.out.print("ok");
    // new MultiThreadDown().down(fileurl.get(i).toString(), bytesum,threadNum,desFiledir+new CrawlerFalconDocu().getFilename(fileurl.get(i).toString()));
	}
	 public static void copyFile(String oldPath, String newPath) { 
	       try { 
	           int bytesum = 0; 
	           int byteread = 0; 
	           File oldfile = new File(oldPath); 
	           if (oldfile.exists()) { //文件存在时 
	               InputStream inStream = new FileInputStream(oldPath); //读入原文件 
	               FileOutputStream fs = new FileOutputStream(newPath); 
	               byte[] buffer = new byte[1444]; 
	               int length; 
	               while ((byteread = inStream.read(buffer)) != -1) { 
	                   bytesum += byteread; //字节数 文件大小 
	                   System.out.println(bytesum); 
	                   fs.write(buffer, 0, byteread); 
	               } 
	               inStream.close(); 
	           } 
	       } 
	       catch (Exception e) { 
	           System.out.println("复制单个文件操作出错"); 
	           e.printStackTrace(); 

	       } 

	   } 	
	    /** 
	     * 删除文件 
	     * @param filePathAndName String 文件路径及名称 如c:/fqf.txt 
	     * @param fileContent String 
	     * @return boolean 
	     */ 
	   public static void delFile(String filePathAndName) { 
	       try { 
	           String filePath = filePathAndName; 
	           filePath = filePath.toString(); 
	           File myDelFile = new File(filePath); 
	           myDelFile.delete(); 

	       } 
	       catch (Exception e) { 
	           System.out.println("删除文件操作出错"); 
	           e.printStackTrace(); 

	       } 

	   } 
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String fileName = "E:\\RDF\\1\\";
		File file = new File(fileName);   
		String[] filelist = file.list();
		for(int i=0;i<filelist.length;i++){
			System.out.println("Processing the "+filelist[i].toString());
		    String dir=fileName+filelist[i].toString().substring(0, filelist[i].toString().indexOf("."));
		    File filedir = new File(dir);
		    if(filedir.mkdirs());  //创建目录
		    copyFile(fileName+filelist[i],dir+"\\"+filelist[i]);
		   // delFile(fileName+filelist[i]); 
		    ArrayList UrlsFilter=new ArrayList();   //存放最后的结果
		    UrlsFilter=new Crawler().SameAs(dir+"\\"); //抽取SameAS链接
		    //UrlsFilter.add("<https://www.googleapis.com/freebase/v1/rdf/en/scarsdale>");
		    new Crawler().CrawlerData(UrlsFilter,dir+"\\");
		    /*for(int j=0;j<UrlsWrong.size();j++){
		    	try {
					System.out.println("Wrong" + UrlsWrong.get(j).toString()
							+ "\n");
				} catch (Exception e) {
					// TODO: handle exception
				}
		    	
		    }*/
		 }
	}
}

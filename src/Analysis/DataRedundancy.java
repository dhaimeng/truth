package Analysis;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
public class DataRedundancy {
	/*
	 * 该方法主要统计所有的Entity中一共分布在那些数据源中
	 */
	  public static ArrayList readFile(String filePath) throws IOException{
	        ArrayList Arrayresult=new ArrayList();
		    File file = new File(filePath);   
			String[] filelist = file.list();
			String pre=null;
			String Dataname=null;
			int index=0;
		    for(int i=0;i<filelist.length;i++){
					//System.out.println("Processing the "+filelist[i].toString());
					FileReader fr = new FileReader(filePath+filelist[i]);
					BufferedReader br = new BufferedReader(fr);
					pre = br.readLine();
					String next = br.readLine();
					while (next != null) {
						index++;
						int j=pre.indexOf('/',7);
						if(j>0){
						 Dataname=pre.substring(7,j);
						}else{
							Dataname=pre.substring(7);
						}
						if(Arrayresult.indexOf(Dataname)==-1){
							Arrayresult.add(Dataname);
						}
						pre = next;
						next=br.readLine();		
	  		          }
				}
			//System.out.println("Processing the "+Arrayresult.indexOf("bbc.co.uk"));
			return Arrayresult;
	}
	  /*
		 * 该方法对比两个文件A,B中,差异的字符。A是源文件，B是对比文件
	  */
	  public static void ComparedFile(String filePathA,String filePathB,String dirPath) throws IOException{
	        ArrayList Arrayresult=new ArrayList();
		    File file = new File(filePathA);   //源文件
			String pre=null;
			FileReader fr = new FileReader(filePathA);
			BufferedReader br = new BufferedReader(fr);
			pre = br.readLine();
			String next = br.readLine();
			FileReader frB = new FileReader(filePathB);
			BufferedReader brB = new BufferedReader(frB);
			while (next != null) {
				String preB = brB.readLine();
				String nextB = brB.readLine();
				while (nextB != null) {
					if(!pre.equals(preB)){
						Arrayresult.add(pre);
					}
					 preB = nextB;
					 nextB=brB.readLine();
				}	
				  pre = next;
				  next=br.readLine();		
	  		     }
			  String sb = null;
			  for(int i=0;i<Arrayresult.size();i++){
					sb+=Arrayresult.get(i).toString();
				}
				FileWriter fw = new FileWriter(dirPath+"Arr.txt",true);
		        BufferedWriter bw=new BufferedWriter(fw);
		        bw.write(sb.toString());
		        bw.newLine();
		        bw.flush();
		        fw.close();
}
			//System.out.println("Processing the "+Arrayresult.indexOf("bbc.co.uk"));
	  /*
		 * 该方法主要统计每一个Entity包含的实体数量以及数据源的总数
		 */
		  public static void DistributionEntity(String filePath) throws IOException{
		        ArrayList Arrayresult=new ArrayList();
			    File file = new File(filePath);   
				String[] filelist = file.list();
				String pre=null;
				String Dataname=null;
				int index=0;
			    for(int i=0;i<filelist.length;i++){
						//System.out.println("Processing the "+filelist[i].toString());
			            index=0;
						FileReader fr = new FileReader(filePath+filelist[i]);
						BufferedReader br = new BufferedReader(fr);
						pre = br.readLine();
						String next = br.readLine();
						while (next != null) {
							index++;
						    int j=pre.indexOf('/',7);
							if(j>0){
							 Dataname=pre.substring(7,j);
							}else{
								Dataname=pre.substring(7);
							}
							if(Arrayresult.indexOf(Dataname)==-1){
								Arrayresult.add(Dataname);
							}
							pre = next;
							next=br.readLine();		
		  		          }
						  FileWriter fw = new FileWriter("E:\\RDF\\1\\"+"DistributionEntity.txt",true);
					      BufferedWriter bw=new BufferedWriter(fw);
					      bw.write(Arrayresult.size()+"\t"+index+"\n");
					      bw.newLine();
					      bw.flush();
					      fw.close();
					      Arrayresult.clear();
					}
				//System.out.println("Processing the "+Arrayresult.indexOf("bbc.co.uk"));
		}
	  /*
	   * 该方法主要统计所有的数据源包含的实体情况，例如 DBpedia提供了5000个Entity
	   */
	 public static void DataName(String filePath,ArrayList DataName) throws IOException{
	        ArrayList Arrayresult=DataName;
	        int []count=new int[600];
		    File file = new File(filePath);   
			String[] filelist = file.list();
			String pre=null;
			String Dataname=null;
			int index=0;
		    for(int i=0;i<filelist.length;i++){
					//System.out.println("Processing the "+filelist[i].toString());
		    	    ArrayList sb=new  ArrayList();  //存放每个文档数据源的结果
		    	    FileReader fr = new FileReader(filePath+filelist[i]);
					BufferedReader br = new BufferedReader(fr);
					pre = br.readLine();
					String next = br.readLine();
					while (next != null) {
						int j=pre.indexOf('/',7);
						if(j>0){
						 Dataname=pre.substring(7,j);
						}else{
						 Dataname=pre.substring(7);
						}
					   if(sb.indexOf(Dataname)==-1){
						   int Dindex=Arrayresult.indexOf(Dataname);
						   count[Dindex]++;
						   sb.add(Dataname);
					   }
						pre = next;
						next=br.readLine();		
	  		          }
					sb.clear();
				}
		   
		    StringBuffer sb=new StringBuffer();
		     //统计数据源对应Entity的分布情况
		  /*int []Dis=new int[600];
		    ArrayList Distribution=new  ArrayList();  //存放每个文档数据源的结果
		    for(int i=0;i<Arrayresult.size();i++){
		    	if(Distribution.indexOf(count[i])==-1){
		    		Distribution.add(count[i]);
		    	}
		    	Dis[Distribution.indexOf(count[i])]++;	
		    }
		    for(int i1=0;i1<Distribution.size();i1++){
				sb.append(Distribution.get(i1).toString()+"\t"+Dis[i1]+"\n");
			}	
		    FileWriter fw = new FileWriter("E:\\RDF\\1\\"+"IndexDataNameCount.txt",true);
	        BufferedWriter bw=new BufferedWriter(fw);
	        bw.write(sb.toString());
	        bw.newLine();
	        bw.flush();
	        fw.close();*/
		   /* 输出每个数据源提供的Entity的个数*/
		   for(int i=0;i<Arrayresult.size();i++){
				sb.append(Arrayresult.get(i).toString()+"\t"+count[i]+"\n");
			}
			FileWriter fw = new FileWriter("E:\\RDF\\1\\"+"DataNameCount.txt",true);
	        BufferedWriter bw=new BufferedWriter(fw);
	        bw.write(sb.toString());
	        bw.newLine();
	        bw.flush();
	        fw.close();
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ArrayList Arrayresult=readFile("E:\\RDF\\1\\Location\\");
		//DataName("E:\\RDF\\1\\Location\\",Arrayresult);
		DistributionEntity("E:\\RDF\\1\\Location\\");
	}
}

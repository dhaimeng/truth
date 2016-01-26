package Analysis;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
public class AttributeRedundancy {
	/*
	 * 该方法主要统计所有的属性的总数
	 */
	  public static void readFile(String filePath) throws IOException{
	        ArrayList Arrayresult=new ArrayList();
	        //Arrayresult.ensureCapacity(1000);
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
						while (pre != null) {
							//index++;
							//String []index1=pre.split(" ");
							try {
								if(pre.indexOf("<")!=-1){
								Dataname = pre.substring(pre.indexOf("<", 3) + 1,pre.indexOf(">", pre.indexOf(">") + 2));
								}//Dataname=index1[1];
							} catch (Exception e) {
								// TODO: handle exception
								System.out.println("Processing the "+filelist[i]+"the line"+pre);
								//System.out.println(e.toString());

							}finally{
							}
							if(Arrayresult.indexOf(Dataname)==-1){
								Arrayresult.add(Dataname);
							}
							pre =br.readLine();		
				     }
				  
				}
			//System.out.println("Processing the "+Arrayresult.get(0).toString());
			//System.out.println("Processing the "+Arrayresult.indexOf("bbc.co.uk"));
		    FileWriter fw = new FileWriter(filePath+"All_Attribute.txt",true);
		    BufferedWriter bw=new BufferedWriter(fw);
		    String Attribute = null;
		    for(int m=0;m<Arrayresult.size();m++){
		    	Attribute+=Arrayresult.get(m).toString()+"\n";
		    }
		      bw.write(Attribute);
		      bw.newLine();
		      bw.flush();
		      fw.close();
		     // Arrayresult.clear();   
	}
	  /*
		 * 该方法主要是把相等属性放在一起进行试验和统计分析
		 */
	public static void FilterAttribute(String AttributePath,String filePath,String dirPath) throws IOException{
		ArrayList ArrayAttribute=new ArrayList();
		ArrayAttribute.ensureCapacity(1000);
		FileReader fr1 = new FileReader(AttributePath);
		BufferedReader br1 = new BufferedReader(fr1);
		String pre = br1.readLine();
		while (pre != null) {
			ArrayAttribute.add(pre.trim());
			pre=br1.readLine();	
		}
		ArrayList Arrayresult=new ArrayList();
		File file = new File(filePath);   
		String[] filelist = file.list();
		String Dataname=null;
		ArrayList sb=new ArrayList();
        for(int i=0;i<filelist.length;i++){
			for(int j=0;j<ArrayAttribute.size();j++){
				if(ArrayAttribute.get(j).toString().indexOf("<http://oneobject>")!=-1){
					sb.add(ArrayAttribute.get(j).toString());
				}
				else{
				FileReader fr = new FileReader(filePath+filelist[i]);
				BufferedReader br = new BufferedReader(fr);
				pre = br.readLine();
				while (pre != null) {
				try {
					Dataname = pre.substring(pre.indexOf("<", 3) + 1,pre.indexOf(">", pre.indexOf(">") + 2));
					} catch (Exception e) {
                     System.out.println("Processing the "+filelist[i].toString()+"  "+Dataname);}
					if(Dataname.equals(ArrayAttribute.get(j))){
						 sb.add(pre);
					   }
				     pre =br.readLine();		
		  		     }
			     }
			   }
			  for(int i1=0;i1<sb.size();i1++){
				  if(i1<sb.size()-1){
				      if(sb.get(i1).toString().indexOf("http://oneobject")!=-1&&sb.get(i1+1).toString().indexOf("http://oneobject")!=-1){ 
				    	  sb.remove(i1);
				    	 i1--;
				         }				 
				      }else{
					 if(sb.get(i1).toString().indexOf("<http://oneobject>")!=-1)
						 sb.remove(i1);
				 }
			  }
			  StringBuffer result=new StringBuffer();
			  for(int j=0;j<sb.size();j++){
				  result.append(sb.get(j).toString()+"\n");
				 // System.out.println(sb.get(j).toString());
			  }

			  FileWriter fw = new FileWriter(dirPath+filelist[i].substring(0,filelist[i].indexOf("."))+"Clear.txt",true);
		      BufferedWriter bw=new BufferedWriter(fw);
		      bw.write(result.toString());
		      bw.newLine();
		      bw.flush();
		      fw.close();  
		      sb.clear();
            }
			//System.out.println("Processing the "+Arrayresult.indexOf("bbc.co.uk"));
	     // Arrayresult.clear();	
	}
	  /*
	   * 该方法主要统计所有的数据源包含的实体情况，例如 DBpedia提供了5000个Entity
	   */
	public static void DataName(String filePath) throws IOException{
	        ArrayList Arrayresult=new ArrayList();
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
						int j=pre.indexOf("/", pre.indexOf("://")+3);
						if(j>0){
						Dataname=pre.substring(pre.indexOf("://")+3,pre.indexOf("/", pre.indexOf("://")+3));
						}
						else{
						  try {
							Dataname=pre.substring(pre.indexOf("://")+3);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println(pre);
							e.printStackTrace();
						}
						}
						if(Arrayresult.indexOf(Dataname)==-1){
							  Arrayresult.add(Dataname);
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
				count[i]=0;
			}
			FileWriter fw = new FileWriter("E:\\快盘\\当前任务\\任务1-Trust问题\\Dataset\\descriptors\\Data\\"+"DataNameCount.txt",true);
	        BufferedWriter bw=new BufferedWriter(fw);
	        bw.write(sb.toString());
	        bw.newLine();
	        bw.flush();
	        fw.close();
	}
	/*
	   * 该方法主要统计每个数据集中的所有实体总数
	   */
	public static void SumEntity(String filePath) throws IOException{
        int count=0;
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
					int j=pre.indexOf("/", pre.indexOf("://")+3);
					if(j>0){
					   Dataname=pre.substring(pre.indexOf("://")+3,pre.indexOf("/", pre.indexOf("://")+3));
					}
					else{
						 try {
								Dataname=pre.substring(pre.indexOf("://")+3);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								System.out.println(pre);
								e.printStackTrace();
							}
					}
					if(sb.indexOf(Dataname)==-1)
						sb.add(Dataname);
					pre = next;
					next=br.readLine();		
  		          }
				count+=sb.size();
				sb.clear();
			}
	    System.out.println("所有的实体总数为："+count);
     }
	/*
	   * 该方法主要统计每个数据集中的所有属性
	   */
	public static void SumPredicate(String filePath) throws IOException{
      int count=0;
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
					int j=pre.indexOf("/", pre.indexOf("://")+3);
					if(j>0){
					   Dataname=pre.substring(pre.indexOf("://")+3,pre.indexOf("/", pre.indexOf("://")+3));
					}
					else{
						 try {
							Dataname=pre.substring(pre.indexOf("://")+3);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								System.out.println(pre);
								e.printStackTrace();
							}
					}
					if(Dataname.equals("dbpedia.org")&& sb.indexOf(pre)==-1){
						sb.add(pre);
					}
					pre = next;
					next=br.readLine();		
		          }
				count+=sb.size();
				sb.clear();
			}
	    System.out.println("所有的实体总数为："+count);
   }
	/*
	 * 该方法对比两个文件A,B中,差异的字符。A是源文件，B是对比文件
  */
  public static void ComparedFile(String filePathA,String filePathB,String dirPath) throws IOException{
        ArrayList Arrayresult=new ArrayList();
        ArrayList Array=new ArrayList();
		String pre=null;
		FileReader frB = new FileReader(filePathB);
		BufferedReader brB = new BufferedReader(frB);
		String preB = brB.readLine();
		String nextB = brB.readLine();
		while (nextB != null) {
			 Array.add(preB);
			 preB = nextB;
			 nextB=brB.readLine();
		 }
		FileReader fr = new FileReader(filePathA);
		BufferedReader br = new BufferedReader(fr);
		pre = br.readLine();
		String next = br.readLine();
		while (next != null) {
			if(Array.indexOf(pre)==-1){
				Arrayresult.add(pre);
			}
			  pre = next;
			  next=br.readLine();		
  		     }
		  String sb = null;
		  for(int i=0;i<Arrayresult.size();i++){
				sb+=Arrayresult.get(i).toString()+"\n";
			}
			FileWriter fw = new FileWriter(dirPath+"Arr.txt",true);
	        BufferedWriter bw=new BufferedWriter(fw);
	        bw.write(sb.toString());
	        bw.newLine();
	        bw.flush();
	        fw.close();
}
  /*
	 * 该方法计算每个冲突属性的冲突个数分布
*/
	public static Map ConflictEntropy(String filePath) throws IOException{
		    Map Arrayresult=new HashMap();//存储最后统计的结果
		    File file = new File(filePath);   
			String[] filelist = file.list();
			String pre=null;
			String Dataname=null;
			int index=0;
			int count=0;
		    for(int i=0;i<filelist.length;i++){
					//System.out.println("Processing the "+filelist[i].toString());
		    	    ArrayList sb=new  ArrayList();  //存放每个文档数据源的结果
		    	    FileReader fr = new FileReader(filePath+filelist[i]);
					BufferedReader br = new BufferedReader(fr);
					pre = br.readLine();
					String next = br.readLine();
					while (next != null) {
						if(pre.indexOf("http://oneobject")!=-1){
							if(count!=0){
								if(Arrayresult.containsKey(count))
								    Arrayresult.put(count, Integer.parseInt(Arrayresult.get(count).toString())+1);
								else
									Arrayresult.put(count,1);
							}
							sb.clear();
							count=0;
						}
						else{
							String s=pre.substring(pre.indexOf(">", pre.indexOf(">") + 2)+1).trim();
							if(sb.indexOf(s)==-1){
								sb.add(s);
								count++;
							}
						}
						pre = next;
						next=br.readLine();		
			          }
				}
		   // System.out.println("所有的实体总数为："+count);
		    return Arrayresult;
	   }
	/*
	 * 该方法计算冲突属性的信息增益
   */
	public static Map ConflictObject(String filePath) throws IOException{
	    Map Arrayresult=new HashMap();//存储最后统计的结果
	    File file = new File(filePath);   
		String[] filelist = file.list();
		String pre=null;
		String Dataname=null;
		int index=0;
		int count=0;
	    for(int i=0;i<filelist.length;i++){
				//System.out.println("Processing the "+filelist[i].toString());
	    	    ArrayList sb=new  ArrayList();  //存放每个文档数据源的结果
	    	    FileReader fr = new FileReader(filePath+filelist[i]);
				BufferedReader br = new BufferedReader(fr);
				pre = br.readLine();
				String next = br.readLine();
				while (next != null) {
					if(pre.indexOf("http://oneobject")!=-1){
						if(count!=0){
							if(Arrayresult.containsKey(count))
							    Arrayresult.put(count, Integer.parseInt(Arrayresult.get(count).toString())+1);
							else
								Arrayresult.put(count,1);
						}
						sb.clear();
						count=0;
					}
					else{
						String s=pre.substring(pre.indexOf(">", pre.indexOf(">") + 2)+1).trim();
						if(sb.indexOf(s)==-1){
							sb.add(s);
							count++;
						}
					}
					pre = next;
					next=br.readLine();		
		          }
			}
	   // System.out.println("所有的实体总数为："+count);
	    return Arrayresult;
   }
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String filePath="E:\\快盘\\当前任务\\任务1-Trust问题\\Dataset\\Location\\ConflictData\\";
		//SumEntity(filePath);
		//SumPredicate(filePath);
		Map Arrayresult=ConflictObject(filePath);
		Iterator iter = Arrayresult.entrySet().iterator();
		String dir="E:\\快盘\\当前任务\\任务1-Trust问题\\Dataset\\Location\\result.txt";
		 FileWriter fw = new FileWriter(dir);//分析结果存储
		 BufferedWriter bw=new BufferedWriter(fw); 
		 while (iter.hasNext()) {
	         Map.Entry entry = (Map.Entry) iter.next();
	         bw.write(entry.getKey().toString()+"  "+entry.getValue()+"\n");//为数据源与Object之间的映射矩阵行名初始化
	         bw.flush();// Print col 1
		 }
		//String filePath="E:\\RDF\\1\\test\\";
		 //readFile(filePath); //统计出所以的实体属性，用来清理
		//ComparedFile(filePath+"All_Attribute.txt","E:\\快盘\\当前任务\\任务1-Trust问题\\Dataset\\Location\\SchemaMapping.txt",filePath);
	   // FilterAttribute("E:\\快盘\\当前任务\\任务1-Trust问题\\Dataset\\Location\\OAEI2011\\SchemaMapping.txt",filePath,"E:\\快盘\\当前任务\\任务1-Trust问题\\Dataset\\Location\\ConflictData\\");
		//DistributionEntity("E:\\RDF\\1\\Location\\");
	}
} 

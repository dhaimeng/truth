package DynamicTrust;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

public class TruthFinder {

	/**
	 * 该方法作为实验的BaseLine方法（http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.89.8378&rep=rep1&type=pdf）
	 * @param args
	 */
	private static final double ALPHA = 0.85;   
	private static final double DISTANCE = 0.0000001; //迭代阈值
	 private static final double DISTANCEString =0.8; //如果字符串之间的相似小于这个数值，认为是无边的
	 private static final double DISTANCEValue = 0.2; //如果s数值之间的距离大于这个数值，认为是无边的
	 private static final String fileName="C:\\Users\\Johnson\\Desktop\\BTC2012所有SameAs的分析结果.txt";  //当前BTC2012所有的SameAs属性存储的位置
	 static Map TrustDataset=new HashMap();//数据源的信任值对
	 static int ObjectNum,DatasetNum,index;//相同Predict的标志位;
	 static Map []TrustObject;//每个Object的数据信任值对
	 static Map []MapSP=new HashMap[1000];//Object与Subject Predict之间的映射
	 static String [][]MapDataset;//数据源与Object之间的映射矩阵
	 static ArrayList BeliefPropagation =new ArrayList();//数据相似矩阵
	 static int []Datatype=new int[1000];//数据类型标注符号，为每一个冲突的属性标注冲突的数据类型1-String，2-Number，3-categorical
	/**
	 * Dynamic Trust Model的初始化，用于计算数据源的初始信任值，
	 * 以及根据目前的文件输入初始化映射矩阵MapDataset和MapSP
	 * @param filepath冲突文件的输入
	 * @throws IOException 
	*/
	 public static void Initialization(String filepath) throws IOException{
		//通过读取BTC2012中SameAs的分析结果，读取所有数据源的信任值
		 Map TrustAllDataset=new HashMap();
		 FileReader fr = new FileReader(fileName);
		 BufferedReader br = new BufferedReader(fr);
		 String pre = br.readLine();
		 while(pre!= null){
			 String[]ns=pre.split("  ");
			 TrustAllDataset.put(ns[0], ns[1]);
			 pre = br.readLine();
		 }
		 br.close();
		 fr.close();
		 //读取文件中涉及到的数据源，并将数据源的值初始化TrustDataset；初始化Object与Subject Predict之间的映射
		 FileReader frD = new FileReader(filepath);//读取所有数据源的信任值
		 NxParser nxp = new NxParser(frD);
		 Node[] ns;
		 index=-1;//相同Predict的标志位
		 ObjectNum=0;//记录冲突属性值得总个数
		 while (nxp.hasNext()) {
		    ns = nxp.next();
	       // System.out.println(ns[2].toString()); 
		    if(ns[0].toString().indexOf("oneobject")!=-1){ //每一次读取到One Object意味着属性个数加1
		        index++;
		        MapSP[index]=new HashMap();
		        if(ns[0].toString().indexOf("String")!=-1)
		        	Datatype[index]=1;
		        else{
		        	Datatype[index]=2;
		        }
		        continue;
		    }
		    else{
		    	String DatasetName=ns[0].toString().substring(ns[0].toString().indexOf("://")+3, ns[0].toString().indexOf("/",9));
		    	//TrustDataset.put(DatasetName,TrustAllDataset.get(DatasetName));	//提取我们遇到的所有数据源的信任值对	    
		    	TrustDataset.put(DatasetName,0.5);
		    }
		    ObjectNum++;//冲突值得总个数
		    String IndexName=ns[0].toString()+" "+ns[1].toString()+" "+ns[2].toString();
		    MapSP[index].put(ns[2].toString().toLowerCase(),IndexName);
		 }
		 frD.close();
		 ObjectNum++;//多出一列标记Object
		 DatasetNum=TrustDataset.size()+1;
		 TrustObject=new HashMap[ObjectNum];//每个Object的数据信任值对
		 for(int i=0;i<=index;i++){
			 Iterator iter = MapSP[i].entrySet().iterator();
			 TrustObject[i]=new HashMap();
			 while (iter.hasNext()) {
		         Map.Entry entry = (Map.Entry) iter.next();
		         TrustObject[i].put(entry.getKey(), "");//为每一个冲突属性的Object建立Object-Trust对。
			 }
		 }
		 //建立Object与数据源的映射关系表
		 MapDataset=new String[ObjectNum][DatasetNum];  //为数据源与Object之间的映射矩阵预定义
		 for(int i=1;i<ObjectNum;i++){
			   for(int j=1;j<DatasetNum;j++){
				   MapDataset[i][j]="0";
			   }
		 }
		 Iterator iterDataset = TrustDataset.entrySet().iterator();
		 int indexDataset=0;
		 while (iterDataset.hasNext()) {
			 indexDataset++;
	         Map.Entry entry = (Map.Entry) iterDataset.next();
	         MapDataset[0][indexDataset]=entry.getKey().toString();//为数据源与Object之间的映射矩阵列名初始化，
		 }
		 int indexObject=0;
		 for(int i=0;i<=index;i++){
			 Iterator iter = MapSP[i].entrySet().iterator();
			 while (iter.hasNext()) {
				 indexObject++;
		         Map.Entry entry = (Map.Entry) iter.next();
		         MapDataset[indexObject][0]=entry.getKey().toString();//为数据源与Object之间的映射矩阵行名初始化
			 }
		 }
		FileReader frMapping = new FileReader(filepath);//读取所有数据源的信任值
	    NxParser nxpMapping = new NxParser(frMapping);
	    Node[] nsMapping;
	    while (nxpMapping.hasNext()) {
		    nsMapping = nxpMapping.next();
		    if(nsMapping[0].toString().indexOf("oneobject")==-1){ //不读取标志行
		           String DatasetName=nsMapping[0].toString().substring(nsMapping[0].toString().indexOf("://")+3, nsMapping[0].toString().indexOf("/",9));
		           String ObjectName=nsMapping[2].toString().toLowerCase();
		           int indexRow = 0,indexcolumn = 0;
		           for(int i=1;i<DatasetNum;i++){
		        	   if(MapDataset[0][i].equals(DatasetName)){
		        		   indexcolumn=i;
		        	        break;
		        	   }
		           }
		           for(int j=1;j<ObjectNum;j++){
		        	   if(MapDataset[j][0].equals(ObjectName)){
		        		   indexRow=j;
		        		    break;
		        	   }
		           }
		           MapDataset[indexRow][indexcolumn]=""+1;        	   
		    }
	    }
	    //初始化数据相似矩阵
		 for(int i=0;i<=index;i++){
			 Iterator iter = MapSP[i].entrySet().iterator();
             String [][]DataSimilarity=BRMatrix(MapSP[i],Datatype[i]);
			 BeliefPropagation.add(i, DataSimilarity);
			 
		 }
	 }
	 public static String[][] BRMatrix (Map TrustObject,int index){
			String[][]DSMatrix=new String[TrustObject.size()+1][TrustObject.size()+1];//相似矩阵的建立
			Iterator iter = TrustObject.entrySet().iterator();
			int Num=1;
			double MaxNum=0;
			while (iter.hasNext()) {
		         Map.Entry entry = (Map.Entry) iter.next();
		         //为数据源与Object之间的映射矩阵行名初始化
		         DSMatrix[0][Num]=entry.getKey().toString();  //为二维矩阵建立行，列标志
		         DSMatrix[Num][0]=entry.getKey().toString();
		         Num++;
			 }
			for(int i=1;i<=TrustObject.size();i++)  //列
				for(int j=1;j<=TrustObject.size();j++)//行
				{
					double Similarity=0;
					if(index==1){
						if(DSMatrix[0][i].toString().equals(DSMatrix[j][0].toString()))
							Similarity=0;
						else{
						   Similarity=similarity(DSMatrix[0][i].toString(),DSMatrix[j][0].toString());
						   if(Similarity<DISTANCEString)
							   Similarity=0;
						    }
					}
					else{
						//调用计算数值类型的相似函数
						if(DSMatrix[0][i].toString().equals(DSMatrix[j][0].toString()))
							Similarity=0;
						else{
							if(Math.abs(Double.parseDouble(DSMatrix[0][i].toString())-Double.parseDouble(DSMatrix[j][0].toString()))<DISTANCEValue && Math.abs(Double.parseDouble(DSMatrix[0][i].toString())-Double.parseDouble(DSMatrix[j][0].toString()))!=0){
						       Similarity=1/Math.abs(Double.parseDouble(DSMatrix[0][i].toString())-Double.parseDouble(DSMatrix[j][0].toString()));
							}
							else{
								Similarity=0;
							}
						}
						if(Similarity>MaxNum)
							MaxNum=Similarity;
					}
					DSMatrix[i][j]=Double.toString(Similarity);
				}
			if(index==2 && MaxNum!=0){
				for(int i=1;i<=TrustObject.size();i++)  //列
					for(int j=1;j<=TrustObject.size();j++)//行
					{
						DSMatrix[i][j]=Double.toString(Double.parseDouble(DSMatrix[i][j].toString())/MaxNum);
					}
			}
			return DSMatrix;
		}
	public static Map TrusDSTObject(Map TrustDataset){
		Map TrustObject=new HashMap();//每个Object的数据信任值对
		double TObject=1;
		for(int i=1;i<ObjectNum;i++){
			   for(int j=1;j<DatasetNum;j++){
				   if(MapDataset[i][j].indexOf("1")!=-1){
					   TObject=TObject*(1-Double.parseDouble(TrustDataset.get(MapDataset[0][j]).toString()));
				   }
			    }
			   TrustObject.put(MapDataset[i][0],1-TObject);
			   TObject=1;
		}
		return TrustObject;
	}
	 public static boolean compareAbs(Map pageRankpre,Map pageRank){  
         boolean flag = true;  
     	Iterator iter = pageRankpre.entrySet().iterator();
 		while (iter.hasNext()) {
 	         Map.Entry entry = (Map.Entry) iter.next();
 	         Iterator iter1 = pageRank.entrySet().iterator();
 	    		while (iter1.hasNext()) {
 	    			Map.Entry entry1 = (Map.Entry) iter1.next();
 	    			if(entry.getKey().toString().equals(entry1.getKey())){
 	    				 double Difference=Double.parseDouble(entry.getValue().toString())-Double.parseDouble(entry1.getValue().toString());
 	                     if(Math.abs(Difference)>DISTANCE){  
 	                                      flag = false;
 	                                      break;
 	                      }  
 	    			}
 	    		}
 		}
         return !flag;  
 } 
 public static int ld(String s, String t) {  
     int d[][];  
     int sLen = s.length();  
     int tLen = t.length();  
     int si;   
     int ti;   
     char ch1;  
     char ch2;  
     int cost;  
     if(sLen == 0) {  
         return tLen;  
     }  
     if(tLen == 0) {  
         return sLen;  
     }  
     d = new int[sLen+1][tLen+1];  
     for(si=0; si<=sLen; si++) {  
         d[si][0] = si;  
     }  
     for(ti=0; ti<=tLen; ti++) {  
         d[0][ti] = ti;  
     }  
     for(si=1; si<=sLen; si++) {  
         ch1 = s.charAt(si-1);  
         for(ti=1; ti<=tLen; ti++) {  
             ch2 = t.charAt(ti-1);  
             if(ch1 == ch2) {  
                 cost = 0;  
             } else {  
                 cost = 1;  
             }  
             d[si][ti] = Math.min(Math.min(d[si-1][ti]+1, d[si][ti-1]+1),d[si-1][ti-1]+cost);  
         }  
     }  
     return d[sLen][tLen];  
 }  
 /*
	 * 计算字符串的Edit Distance距离
	 * @author Wenqiang Liu
	 * @time 2015/3/31
 */
 public static double similarity(String src, String tar) {  
     int ld = ld(src, tar);  
     return 1 - (double) ld / Math.max(src.length(), tar.length());   
 } 
	/*
	 * 根据当前Object的信任值，为数据源的信任赋值
	 * @parameter TrustObject为当前Object信任值
	 * @return Map 返回每个数据源的信任值
	 * @author Wenqiang Liu
	 * @time 2015/3/31
 */
	public static Map TrusObToDS(Map TrustObject){
		Map TrustDataset=new HashMap();//每个Object的数据信任值对
		Map TrustDatasetF=new HashMap();//每个Object的数据信任值对
		double TObject=0;
		int ALLNum=0;
		double Max=0;
		for(int i=1;i<DatasetNum;i++){
			   for(int j=1;j<ObjectNum;j++){
				   if(MapDataset[j][i].indexOf("1")!=-1){
					   TObject=TObject+Double.parseDouble(TrustObject.get(MapDataset[j][0]).toString());
					   ALLNum++;
				   }
			    }
			   if(Max<TObject/ALLNum)
				   Max=TObject/ALLNum;
			   TrustDataset.put(MapDataset[0][i],TObject/ALLNum);
			   TObject=0;
			   ALLNum=0;
		}
		Iterator iter = TrustDataset.entrySet().iterator();
		while (iter.hasNext()) {
	         Map.Entry entry = (Map.Entry) iter.next();
	         TrustDatasetF.put(entry.getKey(), Double.parseDouble(entry.getValue().toString())/Max);
		}
		return TrustDataset;
	}    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
      
	}

}

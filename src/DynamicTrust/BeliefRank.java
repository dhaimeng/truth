package DynamicTrust;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Jama.Matrix;  
public class BeliefRank {
	/**
	 * @param BR是Belief Rank的缩写形式
	 */
	private static final double ALPHA = 0.51;  
    private static final double DISTANCE =  0.001; 
    private static final double Intite = 1; //每个数据源的初始值 
    static ArrayList DatasetName=new ArrayList();//数据库的名字 
    /* 

     * 读取以三元组形式存放的SameAs关系并将引用关系存放在数组中

     */  
    public static  ArrayList DataBaseSameAs(String filePath) throws IOException{
    	ArrayList DatasetName=new ArrayList();
    	 FileReader fr = new FileReader(filePath);
		 BufferedReader br = new BufferedReader(fr);
		 String pre=null;
		 pre = br.readLine();
		 FileWriter fw = new FileWriter(filePath+"DataSameAs.txt",true);
	     BufferedWriter bw=new BufferedWriter(fw);
		 while (pre!= null) {
			  String[]ns=pre.split(">");
			  String Subject=null;
			  String Predicate = null;
			try {
				Subject=ns[0].toString().substring(ns[0].toString().indexOf("://")+3, ns[0].toString().indexOf("/",9));
				Predicate = ns[2].toString().substring(ns[2].toString().indexOf("://")+3, ns[2].toString().indexOf("/",10));
				
				if(DatasetName.indexOf(Predicate)==-1){
					DatasetName.add(Predicate);
				}
				if(DatasetName.indexOf(Subject)==-1){
					DatasetName.add(Subject);
				}
				String sb=Subject+"  "+Predicate+"\n";
				bw.write(sb.toString());
			    bw.newLine();
			    bw.flush();
				pre =br.readLine();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(pre);
				pre =br.readLine();
			 }
			}
		 return DatasetName;
    }
    public static double[][] AnalyzeSameAs(String filePath) throws IOException{
		StringBuffer sb=new StringBuffer();
	        //Arrayresult.ensureCapacity(1000);
		 String pre=null;
		 int index=0;
		 int Same=-1;
		 FileReader fr = new FileReader(filePath);
		 BufferedReader br = new BufferedReader(fr);
      	 String SameAs[][]=new String[10000][2];
		 pre = br.readLine();
		 int []Count=new int[10000];
		 while (pre!= null) {	
			  String[]ns=pre.split(">");
			  String Subject=null;
			  String Predicate = null;
			try {
				Subject=ns[0].toString().substring(ns[0].toString().indexOf("://")+3, ns[0].toString().indexOf("/",9));
				Predicate = ns[2].toString().substring(ns[2].toString().indexOf("://")+3, ns[2].toString().indexOf("/",10));
				Same=1;//重复的标志位
				for(int i=0;i<SameAs.length;i++){
					if(Subject.equals(SameAs[i][0])&& Predicate.equals(SameAs[i][1])){
					   Same=-1;//发现重复的，把Index+1
					   Count[i]++;
					   break;
					}
				}
				if(Same==1){  //如果没有发现重复的，要插入到数组中
					try {
						SameAs[index][0]=Subject;
						SameAs[index][1]=Predicate;
					    if(SameAs[index][1].indexOf(SameAs[index][0])!=-1){
					    	//SameAs[index][0]=null;
					    	//SameAs[index][1]=null;
					    }else{
					    	Count[index]++;
							index++;
					    }
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				//System.out.println(pre+" "+index);
				pre =br.readLine();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				System.out.println(pre);
				pre =br.readLine();
			  }
			}
			//System.out.println("ALL Done");
			int indexDataset=0;
			for(int i=0;i<index;i++){
				if(DatasetName.indexOf(SameAs[i][0])==-1)
					DatasetName.add(SameAs[i][0]);
				if(DatasetName.indexOf(SameAs[i][1])==-1)
					DatasetName.add(SameAs[i][1]);	
		     } 
			double[][] arrayS=new double[DatasetName.size()][DatasetName.size()];
			for(int i=0;i<DatasetName.size();i++)//列
				for(int j=0;j<DatasetName.size();j++)//行{
				{
					String IndexL=DatasetName.get(i).toString();
					double allNum=0; //所有从Indexl引出去的边的总数
					for(int m=0;m<index;m++)
					{
						if(SameAs[m][0].equals(IndexL))
							allNum+=Count[m];
					}
					double Num=0; //所有从Indexl引出去的到某一个边的总数
					for(int m=0;m<index;m++)
					{
						if(SameAs[m][0].equals(IndexL)&& SameAs[m][1].equals(DatasetName.get(j).toString()))
							Num+=Count[m];
					}
					//System.out.println(allNum);
					//System.out.println(Num);
					if(allNum==0)
						arrayS[j][i]=0.0;
					else{
					  arrayS[j][i]=Num/allNum;
					}
				}
			return arrayS;
	}
    public  static Map PageRank(double[][] arrayS) {  
    	 Map RDB=new HashMap();
    	 int length=arrayS.length;
         ArrayList pageRank=new ArrayList();// 每一步的PageRank值
         ArrayList pageRankpre=new ArrayList();// 每一步的前一次迭代的PageRank值
         for(int i=0;i<length;i++){
        	 pageRank.add(i, Intite);
         }
         int iterator =1; 
         /*
          * 计算当前的PageRank值
          */
         do{
             pageRankpre=pageRank;
             pageRank=doPageRank(pageRankpre,arrayS);
             iterator++;
         }while(compareAbs(pageRankpre,pageRank));//如果符合迭代条件继续计算，否者退出
        System.out.println("迭代次数："+iterator); 
         for(int i=0;i<length;i++){
        	 RDB.put(DatasetName.get(i),pageRank.get(i).toString());
         }
           return RDB;  
       }
    /** 
     * 计算 PageRank的下一次迭代的值
     *  
     * @param init 
     * @param alpho 
     * @return 
     */  
 
    private static ArrayList doPageRank(ArrayList pageRankpre, double[][] arrayS) {  
   	 int length=pageRankpre.size();
   	 ArrayList pageRank=new ArrayList();// 每一步的PageRank值
   	 for(int i=0;i<length;i++){
   		 double sum=0;
       	 for(int j=0;j<length;j++){
       		 sum+=Double.parseDouble(pageRankpre.get(j).toString())*arrayS[i][j];
       	 }
       	 sum=(1-ALPHA)+(ALPHA)*sum;
       	 pageRank.add(i,sum);
        }
        return pageRank;  
    }
    /* 

     * 同阶矩阵，比较对应元素的的绝对值。如果对任意的i,j=1,2,...,n。都有|a(ij)|>|b(ij)|则返回true,否则返回false 

     */  

    public static boolean compareAbs(ArrayList pageRankpre,ArrayList pageRank){  

            boolean flag = true;  
            for(int i=0;i<pageRankpre.size();i++){  
           	 double Difference=Double.parseDouble(pageRankpre.get(i).toString())-Double.parseDouble(pageRank.get(i).toString());
                   if(Math.abs(Difference)>DISTANCE){  
                                    flag = false;
                                    break;
                    }  
            }  
            return !flag;  
    } 
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String fileName="E:\\RDF Data\\BTC2012SameAs\\SameAsAll.txt";  //SameAs的存储路径
		ArrayList DatasetName=new ArrayList();
	/*	DatasetName=DataBaseSameAs(fileName);
		FileWriter fw = new FileWriter("E:\\RDF Data\\SameAsResult.txt");//分析结果存储
		 BufferedWriter bw=new BufferedWriter(fw);
		 for(int i=0;i<DatasetName.size();i++){
			    bw.write(DatasetName.get(i).toString());
			    bw.newLine();
			    bw.flush();
		 }*/
		 
		 FileWriter fw = new FileWriter("E:\\RDF Data\\SameAsResult.txt");//分析结果存储
		 BufferedWriter bw=new BufferedWriter(fw);
		double[][] arrayS =AnalyzeSameAs(fileName);
		Map RDB=new HashMap();
		RDB=PageRank(arrayS);
		Iterator iter = RDB.entrySet().iterator();
		while (iter.hasNext()) {
		    Map.Entry entry = (Map.Entry) iter.next();
		    Object key = entry.getKey();
		    Object val = entry.getValue();
		    bw.write(key+"  "+ val);
		    bw.newLine();
		    bw.flush();
		}
         
	}
}  

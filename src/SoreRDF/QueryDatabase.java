package SoreRDF;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

import com.mysql.jdbc.PreparedStatement;
public class QueryDatabase {

	/**
	 * @param args
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		 Connection conn = null;
	        String sql ="";
	        // MySQL的JDBC URL编写方式：jdbc:mysql://主机名称：连接端口/数据库的名称?参数=值
	        // 避免中文乱码要指定useUnicode和characterEncoding
	        // 执行数据库操作之前要在数据库管理系统上创建一个数据库，名字自己定，
	        // 下面语句之前就要先创建javademo数据库
	        String url = "jdbc:mysql://localhost:3306/btc2012?"
	                + "user=root&password=btc2012&useUnicode=true&characterEncoding=UTF8";
            String pre=null;
	       
	            // 之所以要使用下面这条语句，是因为要使用MySQL的驱动，所以我们要把它驱动起来，
	            // 可以通过Class.forName把它加载进去，也可以通过初始化来驱动起来，下面三种形式都可以
	            Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动
	            // or:
	            // com.mysql.jdbc.Driver driver = new com.mysql.jdbc.Driver();
	            // or：
	            // new com.mysql.jdbc.Driver();
	 
	            System.out.println("成功加载MySQL驱动程序");
	            // 一个Connection代表一个数据库连接
	            conn = DriverManager.getConnection(url);
	            // Statement里面带有很多方法，比如executeUpdate可以实现插入，更新和删除等
	            PreparedStatement pstmt;
	            ResultSet rset;
	            String filePath="E:\\快盘\\当前任务\\任务1-Trust问题\\Dataset\\Film\\SameAsData\\";
	            File file = new File(filePath);    //读取文件夹
				String[] filelist = file.list();
				for(int i=0;i<filelist.length;i++)
				{
				    FileReader fr = new FileReader(filePath+filelist[i]);
					BufferedReader br = new BufferedReader(fr);
					pre = br.readLine();
					String dir="E:\\快盘\\当前任务\\任务1-Trust问题\\Dataset\\Film\\RawData\\";
	    			long t1= System.currentTimeMillis(); 
	    			 FileWriter fw = new FileWriter(dir+"Triple"+filelist[i]);//分析结果存储
					while (pre!= null) {
	                    // long t1= System.currentTimeMillis(); 
	                     // int result = stmt.executeUpdate(sql);// executeUpdate语句会返回一个受影响的行数，如果返回-1就没有成
						ArrayList reslut=new ArrayList();  
				       BufferedWriter bw=new BufferedWriter(fw); 
					   for(int j=1;j<=14;j++){
				        String indexname=pre;
				        try {
				        sql="SELECT * from btc"+j+" where Subject='<"+indexname+">'";
				    	pstmt=(PreparedStatement) conn.prepareStatement(sql);
				    	rset=pstmt.executeQuery();
				        while (rset.next()){
				             String sb=rset.getString("Subject")+" "+rset.getString("Predicate")+" "+rset.getString("Object")+"\n";
				             if(!reslut.contains(sb)){
				            	 reslut.add(sb);
					             bw.write(sb.toString());
						   		 bw.flush();// Print col 1
					   		     } 
				               }
				   		 reslut.clear();
					   }catch (SQLException e) {
				            System.out.println("MySQL操作错误");
				            System.out.println(pre+"vv"+filelist[i]);
				            e.printStackTrace();
				            continue;
				        }
				        finally{
				        	pre=br.readLine();
				        	continue;
				        }
					   }
				   		 pre=br.readLine();
					}
						long t2= System.currentTimeMillis(); 
						System.out.println("Processing "+(i+1)+" file "+(t2-t1)+"ms.");
				   		System.gc();
			}
	      }
}

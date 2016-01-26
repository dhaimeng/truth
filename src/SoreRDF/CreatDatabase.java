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
public class CreatDatabase {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		 Connection conn = null;
	        String sql ="";
			double index=0;
	        // MySQL的JDBC URL编写方式：jdbc:mysql://主机名称：连接端口/数据库的名称?参数=值
	        // 避免中文乱码要指定useUnicode和characterEncoding
	        // 执行数据库操作之前要在数据库管理系统上创建一个数据库，名字自己定，
	        // 下面语句之前就要先创建javademo数据库
	        String url = "jdbc:mysql://localhost:3306/btc2012?"
	                + "user=root&password=btc2012&useUnicode=true&characterEncoding=UTF8";
            String pre=null;
	        try {
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
	            Statement stmt = conn.createStatement();
	            conn.setAutoCommit(false);  
	            int result;
	           // int result = stmt.executeUpdate(sql);// executeUpdate语句会返回一个受影响的行数，如果返回-1就没有成功
	                String s="abc";
	                String filePath="E:\\RDF Data\\BTC2012\\";
	                File file = new File(filePath);   
	                String[] filelist = file.list();
	                for(int i=0;i<filelist.length;i++){
	                	System.out.println("Processing the "+filelist[i].toString()+"\n");
	    				FileReader fr = new FileReader(filePath+filelist[i]);
	    				NxParser nxp = new NxParser(fr);
	    				Node[] ns;
	    				long t1= System.currentTimeMillis(); 
	    				while (nxp.hasNext()) {
	    					   index++;
	    					    ns = nxp.next();
	    					    int Tableindex=(int) (index/100000000)+1;
	    					    sql="insert into btc"+Tableindex+"(Subject,Predicate,Object) values('"+ns[0].toN3().replace("'", "\\'")+"',"+"'"+ns[1].toN3()+"',"+"'"+ns[2].toN3().replace("'", "\\'")+"')";
	    					    result = stmt.executeUpdate(sql);
	    					    if(index%10000==0){
		    				        conn.commit();
		    				        long t2= System.currentTimeMillis(); 
	    							System.out.print("Done "+index+" :"+(t2-t1)+"ms."+"  插入表btc"+Tableindex+"\n");
	    							t1=t2;
	    				            }
	    					    }
	    				      System.out.println(filelist[i].toString()+"Done\n");
	    					}
	       
	        } catch (SQLException e) {
	            System.out.println("MySQL操作错误");
	            System.out.println(pre);
	            e.printStackTrace();
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            conn.close();
        }
	        System.out.print(" ALL Done");
	 
	}

}

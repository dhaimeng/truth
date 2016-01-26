package Filter;
import org.nnsoft.sameas4j.*;
/*
 * Copyright (c) 2009-2012 The 99 Software Foundation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.nnsoft.sameas4j.*;

import com.mysql.jdbc.PreparedStatement;
/**
 * Reference test class for {@link org.nnsoft.sameas4j.SameAsServiceImpl}.
 * http://99soft.github.io/sameas4j/howto.html
       利用英国大学的SameAsAPI接口，来抽取某一个实体的相等实体
 */
public class ExtractSameAs {
	/*
	 * 根据用户输入的URl,通过sameAs接口进行实体消解
	 */
     public static  ArrayList DataBaseSameAs( String Uri) throws Exception, URISyntaxException {
    	 ArrayList SameAsResult=new ArrayList(); //存储最后的结果集合
		  SameAsService sameAsService = DefaultSameAsServiceFactory.createNew();
		  EquivalenceList equivalences = null;
		  Equivalence equivalence = null;
		  String Urls = Uri; //SamenAs消解的url,例如：http://data.nytimes.com/14184568698435582741
		  equivalence = sameAsService.getDuplicates( new URI(Urls));
            for ( URI uri : equivalence )
			  {
            	SameAsResult.add(uri+"\n");
			  }
			return SameAsResult;
		}
 	/*
 	 * 爬取每一条url对应的rdf文档，该方法主要数据集为BTC2012所用的数据集
 	 */
     public static ArrayList QueryDataBase( String Uri) throws Exception{
    	    ArrayList reslut=new ArrayList();   //返回的结果列表
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
			    for(int j=1;j<=14;j++){
				    try {
				      sql="SELECT * from btc"+j+" where Subject='<"+Uri+">'";
				      pstmt=(PreparedStatement) conn.prepareStatement(sql);
				      rset=pstmt.executeQuery();
				      while (rset.next()){
				             String sb=rset.getString("Subject")+" "+rset.getString("Predicate")+" "+rset.getString("Object")+"\n";
				             if(!reslut.contains(sb)){
				            	 reslut.add(sb);
					   		     } 
				               }
				   		 reslut.clear();
					   }catch (SQLException e) {
				            System.out.println("MySQL操作错误");
				            e.printStackTrace();
				        }
					   }
			    return reslut;
	      }
}
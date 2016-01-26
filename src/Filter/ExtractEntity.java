package Filter;
import org.nnsoft.sameas4j.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
/**
 * Reference test class for {@link org.nnsoft.sameas4j.SameAsServiceImpl}.
 * http://99soft.github.io/sameas4j/howto.html
 利用英国大学的SameAsAPI接口，来抽取某一个实体的相等实体
 */
public class ExtractEntity {
	  public static void main(String[] args) throws Exception, URISyntaxException {
		  String FeedDir="E:\\快盘\\当前任务\\任务1-Trust问题\\Dataset\\Music\\enetiy.txt";
		  String desFiledir="E:\\快盘\\当前任务\\任务1-Trust问题\\Dataset\\Music\\";
		  StringBuffer sb=new StringBuffer();
		  int i=1;
		  SameAsService sameAsService = DefaultSameAsServiceFactory.createNew();
		  EquivalenceList equivalences = null;
		  Equivalence equivalence = null;
			//设置代理服务器
			FileReader fr = new FileReader(FeedDir);
			BufferedReader br = new BufferedReader(fr);
			String pre = br.readLine();
			String next = br.readLine();
			while (next!= null) {
		    System.out.println( "Procedding the number" + i);
		    String Urls = null;
			try {
				Urls = pre.toString();
			} catch (Exception e) {
				// TODO: handle exception
				 System.out.println( "THe Wrong Entity " +  pre.toString() );
			}
			equivalence = sameAsService.getDuplicates( new URI(Urls));
           // System.out.println( "Number of equivalent URIs: " + equivalence.getAmount() );
            for ( URI uri : equivalence )
			  {
            	sb.append(uri+"\n");
			   }
            FileWriter fw = new FileWriter(desFiledir + i + ".txt",true);
            BufferedWriter bw=new BufferedWriter(fw);
	        bw.write(sb.toString());
	        bw.newLine();
	        bw.flush();
	        fw.close();
	        i++;
	        sb=new StringBuffer();
            pre = next;
			next = br.readLine();
			}
			  System.out.println( "All Done ");
		}
}

package Filter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
public class UniqueEntity {
/** 过滤RDF文件中含有的.rdf 和genoname中的实体，并且输出所有的Subject
 * 
 */
  public static void main(String[] args) {
			// TODO Auto-generated method stub
			String fileName = "E:\\快盘\\当前任务\\任务1-Trust问题\\Dataset\\descriptors\\";
			File file = new File(fileName);   
			String[] filelist = file.list();
			int fileId = 1;
			int line=2;
			String dir = "E:\\快盘\\当前任务\\任务1-Trust问题\\Dataset\\descriptors\\";
			FileWriter fw = null;
			try {
				for(int i=0;i<filelist.length;i++){
					System.out.println("Processing the "+filelist[i].toString());
					FileReader fr = new FileReader(fileName+filelist[i]);
					BufferedReader br = new BufferedReader(fr);
					StringBuffer sb=new StringBuffer();
					String pre = br.readLine();
					String next = br.readLine();
					String pre_prefix = null;
					try {
						pre_prefix = pre.substring(0, pre.indexOf(" "));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String next_prefix="";
					if(pre_prefix.indexOf(".rdf")==-1 && pre_prefix.indexOf("sws.geonames.org")==-1)
					{
						sb.append(pre_prefix +"\n");
					}
					while (next != null) {
							if(line%10000==0)
								System.out.println("Processing the "+filelist[i].toString()+"  line:"+line);
							line++;
	     					next_prefix = next.substring(0, next.indexOf(" "));
							if (next_prefix.indexOf(pre_prefix)==-1) {
								if(next_prefix.indexOf(".rdf")==-1 && next_prefix.indexOf("sws.geonames.org")==-1)
								{
								   sb.append(next_prefix+"\n");
								}
							}
							fileId++; 
							pre = next;
							pre_prefix=pre.substring(0, pre.indexOf(" "));
							next = br.readLine();
					   }
					    fw= new FileWriter(dir+ filelist[i].toString()+ "UniqueSubject.txt",true);
						BufferedWriter bw=new BufferedWriter(fw);
				        bw.write(sb.toString());
				        bw.newLine();
				        bw.flush();
				        br.close();
					    fr.close();
					    System.gc(); 
					    System.out.println("Processing the "+filelist[i].toString()+"Done!");
					}
			}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		System.out.println("All Done!");
		System.out.println("entiy is "+fileId);
		System.out.println("entiy is "+line);
		}

	}

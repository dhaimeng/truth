package Filter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
public class FilterOther {
/** 过滤RDF文件中含有的.rdf 和genoname中的实体,把每一个实体单独保存一个文件；
 * 
 */
  public static void main(String[] args) {
			// TODO Auto-generated method stub
			String fileName = "E:\\RDF\\1\\";
			File file = new File(fileName);   
			String[] filelist = file.list();
			int fileId = 1;
			int line=2;
			String dir = "E:\\RDF\\1\\";
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
				    sb.append(pre+"\n");
					while (next != null) {
							if(line%10000==0)
								System.out.println("Processing the "+filelist[i].toString()+"  line:"+line);
							line++;
	     					next_prefix = next.substring(0, next.indexOf(" "));
						     if (next_prefix.indexOf(pre_prefix)!=-1) {
								         sb.append(next+"\n"); 
						}else{
							 if(pre_prefix.indexOf(".rdf")==-1 && pre_prefix.indexOf("sws.geonames.org")==-1){
							    fw= new FileWriter(dir+fileId+ ".txt",true);
								BufferedWriter bw=new BufferedWriter(fw);
						        bw.write(sb.toString());
						        bw.newLine();
						        bw.flush();
								fileId++; 
							 }
							sb=new StringBuffer();
							sb.append(next+"\n");
							pre = next;
							pre_prefix=pre.substring(0, pre.indexOf(" "));
								
					       }
							next = br.readLine();
					   }
					if(pre_prefix.indexOf(".rdf")==-1 && pre_prefix.indexOf("sws.geonames.org")==-1){	
						fw= new FileWriter(dir+fileId+ ".txt",true);
						BufferedWriter bw=new BufferedWriter(fw);
				        bw.write(sb.toString());
				        sb=new StringBuffer();
				        bw.newLine();
				        bw.flush();
				        fileId++;
					}
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

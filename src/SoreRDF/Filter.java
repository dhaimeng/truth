package SoreRDF;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;


public class Filter {
	/*
	 * @author:Wenqiang Liu
	 * 将一个文件夹下的所有文件有NQ格式转化为NT格式
	 */
	 public static void ConverNqtoNt(String filePath) throws IOException{
	    File file = new File(filePath);   
		String[] filelist = file.list();
		String pre=null;
	    for(int i=0;i<filelist.length;i++){
				System.out.println("Processing the "+filelist[i].toString());
				FileWriter fw = new FileWriter(filePath+filelist[i]+".nt",true);
				FileReader fr = new FileReader(filePath+filelist[i]);
				NxParser nxp = new NxParser(fr);
				Node[] ns;
				int index=0;
				String StringNt = null;
				while (nxp.hasNext()) {
					   index++;
						if(index%1000000==0){
							System.out.print("Done"+index+"\n");
						}
					    ns = nxp.next();
					    StringNt=ns[0].toN3()+" ";
						for (int i1=1;i1<3;i1++) {
							StringNt += ns[i1].toN3()+" ";
					    }
						StringNt += ".\n";
						if(StringNt.indexOf("@fr_1793")==-1){
						fw.write(StringNt);
						 fw.flush();
						 }
					  }
				fw.close();
			}
	    
	    System.out.print("Done");
}
	 public static void Read(String filePath,int Number) throws IOException{
		System.out.println("Processing the "+filePath.toString());
		FileReader fr = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fr);
		String pre = br.readLine();
		int index=1;
		while (pre != null) {
			if(index==Number){
				System.out.print(pre.toString());
			}
				index++;
				pre=br.readLine();
		}
		   // System.out.print("Done");
	}
public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Filter.ConverNqtoNt("E:\\RDF Data\\2\\");
			//Filter.Read("E:\\RDF Data\\BTC2012\\data-0.nq(1).nt", 37323);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

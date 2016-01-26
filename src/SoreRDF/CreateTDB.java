package SoreRDF;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.util.FileManager;

public class CreateTDB
{
	public LinkedList<String> relations = new LinkedList<String>();
	public HashMap<String,LinkedList<String>> rel_map = new HashMap<String, LinkedList<String>>();
	public void readdir(String nxpath,Model model) throws IOException{
		File file = new File(nxpath);
		String[] files = file.list();
//		for(String fi:files)
//			System.out.println(fi);
//		System.exit(1);
		for(String fl: files){
			System.out.println(fl);
			FileManager.get().readModel(model, nxpath+fl);
			}
		for(String r:this.relations)
			System.out.println(r);		
	}
	
    public static void main(String [] args) throws IOException
    {
    	String directory = "E:\\RDF Data\\BTCStore\\";
        Dataset ds = TDBFactory.createDataset(directory);//建立了一个test的TDB，如果存储test的TDB，则表示使用这个TDB
        Model model = ds.getDefaultModel();//这里使用TDB的默认Model
        String source = "E:\\RDF Data\\BTC2012\\";
        CreateTDB tdb = new CreateTDB();
        tdb.readdir(source,model);
//        String source = "F://myeclipse/workspace/try1/files/rdfstandard.rdf";
//        FileManager.get().readModel(model, source);//读取RDF文件到指定的model里面
         /*
         * 这里要详细说一下如何读取RDF到Model里面的方法了，其实model就有
         * read方法可以对RDF进行读取，但是上面用FileManager会比较好一点，它会自动
         * 处理许多问题
         */
      
        model.commit();//这里类似于数据库的commit，意思是把现在的操作立刻提交
        model.close();//结束使用的时候，一定要对Model和Dataset进行关闭
        ds.close();
        System.out.println("done");
    }
 
}
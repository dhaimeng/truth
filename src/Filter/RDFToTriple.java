package Filter;
import java.io.*;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.sparql.engine.http.*;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.assembler.assemblers.FileManagerAssembler;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.ModelFactory;
public class RDFToTriple {
	/**  RDF格式转化为Triple格式
	 * @param args
	 * @throws FileNotFoundException 
	 */
	
	public RDFToTriple(String inputFileName,String outputFileName){
		File file = new File(inputFileName);    //读取文件夹
		String[] filelist = file.list();
		String errorFileNameValue="error file number is:";
		File dir=new File(outputFileName);
		if(!dir.exists()){
			dir.mkdirs();
		}
		int error=0;
		int FileTotal=1104;
		for(int i=0;i<filelist.length;i++)
		{
			Model model = ModelFactory.createDefaultModel();
			InputStream in = FileManager.get().open(inputFileName + filelist[i]);
			if (in == null) {
			errorFileNameValue+=filelist[i]+"、";
			error++;
			    continue;
			}
			 
			// read the RDF/XML file
			
			    try {
			    	model.read(in, null);//解析rdf文件
			    	//model.read(in, "","N3");
                  // write it to standard out
					String FinalName=outputFileName+filelist[i].replaceAll(".rdf", "")+".nx";
					//String FinalinputFileName=inputFileName+i+".rdf";
					File f = new File(FinalName);
					OutputStream fs=new FileOutputStream(f);
					model.write(fs,"N-TRIPLE");
					//model.write(fs,"RDF/XML");
					fs.flush();
					fs.close();
					System.out.println("finished "+(i+1)+" files");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					errorFileNameValue+=filelist[i]+"、";
					error++;
					model.close();
					continue;
				}
		}
		System.out.println("all error files is  "+error+" ."+errorFileNameValue);
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		 
		 // use the FileManager to find the input file
		//String inputFileName="D:\\rdf\\db\\DS_l\\";
		//String inputFileName="D:\\dsgraph\\datastructure\\�ܹ�\\rdfʵ��\\";
		String inputFileName="C:\\Users\\Johnson\\Desktop\\1\\";  //url的存储位置路径

		//String outputFileName="D:\\rdf\\db\\DS_lnx\\";
		String outputFileName="C:\\Users\\Johnson\\Desktop\\1\\";
		
		RDFToTriple nx =new RDFToTriple(inputFileName, inputFileName);
	}
}

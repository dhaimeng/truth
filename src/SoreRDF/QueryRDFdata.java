package SoreRDF;



import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.update.UpdateAction;

public class QueryRDFdata {

	private Model model = null;

	public ResultSet SelectQuery(String queryString) {
		model = new ConnectTDB().getconnection();
		QueryExecution qexec = QueryExecutionFactory.create(queryString, model);
		ResultSet resultSet = qexec.execSelect();
		//qexec.close();
		model.close();
		return resultSet;
	}

	public Model DescribeQuery(String queryString) {
		model = new ConnectTDB().getconnection();
		QueryExecution qexec = QueryExecutionFactory.create(queryString, model);
		Model resultModel = qexec.execDescribe();
		//qexec.close();
		model.close();
		return resultModel;
	}

	public Model ConstructQuery(String queryString) {
		model = new ConnectTDB().getconnection();
		QueryExecution qexec = QueryExecutionFactory.create(queryString, model);
		Model resultModel = qexec.execConstruct();
		//qexec.close();
		model.close();
		return resultModel;
	}

	public Boolean AskQuery(String queryString) {
		model = new ConnectTDB().getconnection();
		QueryExecution qexec = QueryExecutionFactory.create(queryString, model);
		Boolean booleanresult = qexec.execAsk();
		//qexec.close();
		model.close();
		return booleanresult;
	}

	public void updateQuery(Dataset ds, String updateFile) {
		UpdateAction.readExecute(updateFile, ds);// updateFile是含有更新操作的sparql文件，就是把sparql保存到文件里面去
	}

	public ResultSet ModelToResultset(Model inputmodel){
		String string = "SELECT * { ?Subject ?Predicate ?Object } ";
		QueryExecution qexec1 = QueryExecutionFactory.create(string,
				inputmodel);
		ResultSet resultSet = qexec1.execSelect();	
		//qexec1.close();
		return resultSet;
	}
	
	public void outputFile(ResultSet results,String dir) throws IOException{
		String r = ResultSetFormatter.asText(results);// ******************输出到文件
		PrintWriter pw = new PrintWriter(new FileWriter(dir));
		pw.println(r);
		pw.close(); // 输出部分结束
		System.out.println("done");	
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		QueryRDFdata tdb = new QueryRDFdata();
		String des = "DESCRIBE ?resource where { ?resource <http://dbpedia.org/property/name>	\"Tim Burton\"@en}  ";
		long t1= System.currentTimeMillis(); 
		String s="select distinct ?s ?a ?Concept where {<http://www.johngoodwin.me.uk/family/I0377> ?a ?Concept}"; 
//		String IMDBquery1 =
//	 			"SELECT ?a ?film ?gname"+
//				"{"+
//				"?a ?film ?gname"+
//				
//				//"\"<http://nektar.oszk.hu/resource/auth/magyar_irodalom>\" ?film ?gname"+
//				"}" 
//				;
		ResultSet results = tdb.SelectQuery(s);
		long t2= System.currentTimeMillis(); 
		System.out.println("running time: "+(t2-t1)+"ms.");
		//tdb.outputFile(results,"E:/example.txt");
		ResultSetFormatter.out(results);//**********************控制台输出********************************
		long t3= System.currentTimeMillis(); 
		System.out.println("running time(contains output): "+(t3-t1)+"ms.");
		
		
	}

}

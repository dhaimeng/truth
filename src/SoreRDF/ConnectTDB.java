package SoreRDF;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDBFactory;

public class ConnectTDB {

	/**
	 * @author Wangmeng
	 * connect to RDF tdb AND return the model
	 */
	//Dataset ds = TDBFactory.createDataset("D:\\whynot\\data\\movie\\database");//建立了一个test的TDB，如果存储test的TDB，则表示使用这个TDB
	Dataset ds = TDBFactory.createDataset("E:\\RDF Data\\BTCStore\\");//建立了一个test的TDB，如果存储test的TDB，则表示使用这个TDB
	private Model model=null;
	public Model getconnection(){
		try {
			
	        model = ds.getDefaultModel();//这里使用TDB的默认Model	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return model;
	}
	public void close(){
		ds.close();
	}

}

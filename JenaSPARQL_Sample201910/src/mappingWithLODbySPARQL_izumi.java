import java.io.FileNotFoundException;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;


/* Endpoint へのSPARQLクエリをもちいたWikidataとのマッピング例
 *
 * 注）Proxyの設定が必要な環境で実行するときは，実行時のJVMのオプションとして
 *      -DproxySet=true -DproxyHost=wwwproxy.osakac.ac.jp -DproxyPort=8080
 *     を追加する，
 *     Eclipseの場合「実行の構成＞引数」で設定可能
 * /
 */
public class mappingWithLODbySPARQL_izumi {

	
	static public void main(String[] args) throws FileNotFoundException{


		String queryStr;
		Query query;
		QueryExecution qexec;
		ResultSet rs;
		
		for(int i=0;i<10;i++) {
		queryStr = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+"select * "
				+"where{?uri rdfs:label ?s .\n "+ "}";
		query = QueryFactory.create(queryStr);
		System.out.println("create:"+i);

		qexec = QueryExecutionFactory.sparqlService("http://ja.dbpedia.org//sparql"	, query) ;
        rs = qexec.execSelect();
        qexec.close();
        System.out.println("execSelect");
        
		}
	}
}

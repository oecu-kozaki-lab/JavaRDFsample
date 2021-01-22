import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;


/* SPARQL Endpoint に対するクエリ例
 * 注）Proxyの設定が必要な環境で実行するときは，実行時のJVMのオプションとして
 *      -DproxySet=true -DproxyHost=wwwproxy.osakac.ac.jp -DproxyPort=8080
 *     を追加する，
 *     Eclipseの場合「実行の構成＞引数」で設定可能
 * /
 */

public class searchRDFfromSPARQLendpoint {

	static public void main(String[] args) throws FileNotFoundException{

		//クエリの作成
		String queryStr = "select * where{?s ?p ?o.}LIMIT 100";
		Query query = QueryFactory.create(queryStr);

		 // Remote execution.
        try{
        	QueryExecution qexec = QueryExecutionFactory.sparqlService("https://query.wikidata.org/sparql"	, query) ;
            ((QueryEngineHTTP)qexec).addParam("timeout", "10000") ;

            //出力用のファイルの作成
	        FileOutputStream out;
			out = new FileOutputStream("output/SPARQL-output.txt");

			// クエリの実行.
	        ResultSet rs = qexec.execSelect();

	        // 結果の出力　※以下のどれか「１つ」を選ぶ（複数選ぶと，2つ目以降の結果が「空」になる）
	     	ResultSetFormatter.out(System.out, rs, query);		//表形式で，標準出力に
	     	//ResultSetFormatter.out(out, rs, query); 			//表形式で，ファイルに
	     	//ResultSetFormatter.outputAsCSV(System.out, rs);	//CSV形式で，標準出力に
	     	//ResultSetFormatter.outputAsCSV(out, rs);			//CSV形式で，ファイルに

	     	out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }



	}
}

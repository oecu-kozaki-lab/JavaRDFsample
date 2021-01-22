import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;


/* SPARQL Endpoint へのクエリをもちいたWikidataとのマッピング例
 *
 * 注）Proxyの設定が必要な環境で実行するときは，実行時のJVMのオプションとして
 *      -DproxySet=true -DproxyHost=wwwproxy.osakac.ac.jp -DproxyPort=8080
 *     を追加する，
 *     Eclipseの場合「実行の構成＞引数」で設定可能
 * /
 */

public class mappingWithWikidataBySPARQL {

	static public void main(String[] args) throws FileNotFoundException{

		//読み込むファイルを指定
		File file = new File("input/words.txt");

		try {
			//ファイルの読み込み用のReaderの設定
			BufferedReader br = new BufferedReader(	new InputStreamReader(new FileInputStream(file),"UTF8"));

			 //出力用のファイルの作成
	        FileOutputStream out;
			out = new FileOutputStream("output/mappingWikidata-output.txt");


			while(br.ready()) {
				String line = br.readLine(); //ファイルを1行ずつ読み込む
				System.out.println(line);

				//クエリの作成
				//String queryStr = "select distinct ?s where{?s ?p ?o.}LIMIT 10";
				String queryStr = "select ?s where{?s <http://www.w3.org/2000/01/rdf-schema#label> \""+line+"\"@ja.}LIMIT 10";
				//String queryStr = "select * where{?s ?p ?o.}LIMIT 100";

				// クエリの実行
				Query query = QueryFactory.create(queryStr);
				QueryExecution qexec = QueryExecutionFactory.sparqlService("http://kozaki-lab.osakac.ac.jp/agraph/wikidata_nearly_full"	, query) ;
	            
				//	QueryExecution qexec = QueryExecutionFactory.sparqlService("https://query.wikidata.org/sparql"	, query) ;
	            ((QueryEngineHTTP)qexec).addParam("timeout", "10000") ;
		        ResultSet rs = qexec.execSelect();
		        
		        while(rs.hasNext()) {
		        	QuerySolution qs = rs.next();
		        	Resource  res = qs.getResource("s");
		        	if(res!=null) {
		        		//subjects.add(res);
		        		System.out.println(res.toString());
		        	}
		        }

		        // 結果の出力　※以下のどれか「１つ」を選ぶ（複数選ぶと，2つ目以降の結果が「空」になる）
		     	//ResultSetFormatter.out(System.out, rs, query);		//表形式で，標準出力に
		     	//ResultSetFormatter.out(out, rs, query); 			//表形式で，ファイルに
		     	//ResultSetFormatter.outputAsCSV(System.out, rs);	//CSV形式で，標準出力に
		     	//ResultSetFormatter.outputAsCSV(out, rs);			//CSV形式で，ファイルに

			}



	     	out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }



	}
}

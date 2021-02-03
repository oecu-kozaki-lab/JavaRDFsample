import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;


/* Endpoint へのSPARQLクエリをもちいたWikidataとのマッピング例
 *
 * 注）Proxyの設定が必要な環境で実行するときは，実行時のJVMのオプションとして
 *      -DproxySet=true -DproxyHost=wwwproxy.osakac.ac.jp -DproxyPort=8080
 *     を追加する，
 *     Eclipseの場合「実行の構成＞引数」で設定可能
 * /
 */

public class mappingWithLODbySPARQL {

	static public void main(String[] args) throws FileNotFoundException{

		try {
			//入力ファイル指定
			File file = new File("input/words.txt");
			//ファイルの読み込み用のReaderの設定
			BufferedReader br = new BufferedReader(	new InputStreamReader(new FileInputStream(file),"UTF8"));

			//出力ファイル指定
			File fileOUT = new File("output/mappingLOD-output.ttl");
			//出力用のファイルのWiterの設定
			FileOutputStream out = new FileOutputStream(fileOUT);
			OutputStreamWriter ow = new OutputStreamWriter(out, "UTF-8");
			BufferedWriter bw = new BufferedWriter(ow);

			while(br.ready()) {
				String line = br.readLine(); //ファイルを1行ずつ読み込む
				System.out.println(line);

				//クエリの作成
				String queryStr = "select ?s where{?s <http://www.w3.org/2000/01/rdf-schema#label> \""+line+"\"@ja.}LIMIT 10";
				//String queryStr = "select ?s where{?s <http://www.w3.org/2000/01/rdf-schema#label> \""+line+"\"@ja.}LIMIT 10";
				/*「一致条件」を変更するには，ここのクエリを変えると良い*/


				// クエリの実行
				// Wikidata公式Endpointで試すとhttps関係でエラーが出るため，研究室内の複製版を使用している
				//  →原因は調査中
				Query query = QueryFactory.create(queryStr);
				QueryExecution qexec = QueryExecutionFactory.sparqlService("https://query.wikidata.org//sparql"	, query) ;

				//QueryExecution qexec = QueryExecutionFactory.sparqlService("https://dbpedia.org/sparql"	, query) ;
				/* Wikidata の場合（途中で止まる？）
				QueryExecution qexec = QueryExecutionFactory.sparqlService("https://query.wikidata.org//sparql"	, query) ;
				*/

				//	QueryExecution qexec = QueryExecutionFactory.sparqlService("https://query.wikidata.org/sparql"	, query) ;
	            ((QueryEngineHTTP)qexec).addParam("timeout", "10000") ;
		        ResultSet rs = qexec.execSelect();

		        int n=0;
		        while(rs.hasNext()) {
		        	QuerySolution qs = rs.next();
		        	Resource  res = qs.getResource("s");
		        	if(res!=null) {
		        		//入力したwordの出力
		        		if(n==0) {
		        			bw.write(line+"\t");
		        		}

		        		//マッピング情報の出力
		        		bw.write("<http://kozaki-lab.osakac.ac.jp/kg/prop/mappingToWikidata>\t<"+res.toString()+">");
		        		/* マッピング情報を表すために，独自プロパティを導入
		        		 * →必要ならば使用するプロパティを変えても良い　　　　 */


		        		//マッピング先が複数の場合は，「;」でつなぐ．最後は「.」
		        		if(rs.hasNext()) {
		        			bw.write(" ; ");
		        		}else {
		        			bw.write(" . \n");
		        		}
		        		n++;
		        	}
		        }

		        qexec.close();//これがないと，途中でクエリの応答がしなくなるので注意！

			}

			//入出力のストリームを閉じる【これを忘れると，ファイル処理が正しく終わらない】
			br.close();
	     	bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}

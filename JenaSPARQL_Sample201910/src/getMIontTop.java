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

public class getMIontTop {

	static public void main(String[] args) throws FileNotFoundException{

		try {
			//入力ファイル指定
			File file = new File("input/MIont/NEDO-MI-ont-TOP-20210520.nt");
			//ファイルの読み込み用のReaderの設定
			BufferedReader br = new BufferedReader(	new InputStreamReader(new FileInputStream(file),"UTF8"));

			//出力ファイル指定
			File fileOUT = new File("input/MIont/MIont-20210528.nt");
			//出力用のファイルのWiterの設定
			FileOutputStream out = new FileOutputStream(fileOUT);
			OutputStreamWriter ow = new OutputStreamWriter(out, "UTF-8");
			BufferedWriter bw = new BufferedWriter(ow);

			while(br.ready()) {
				String line = br.readLine(); //ファイルを1行ずつ読み込む
				System.out.println(line);
				bw.write(line+"\n");
				String data[] = line.split(" ");

				//クエリの作成
				String queryStr = "select ?s where{?s <http://www.wikidata.org/prop/direct/P279> "+data[0]+" .}";
				// クエリの実行[1段目]
				Query query = QueryFactory.create(queryStr);
				QueryExecution qexec = QueryExecutionFactory.sparqlService("http://kozaki-lab.osakac.ac.jp/agraph/NEDO_pj_11"	, query) ;
	            ((QueryEngineHTTP)qexec).addParam("timeout", "10000") ;
		        ResultSet rs = qexec.execSelect();

		        while(rs.hasNext()) {
		        	QuerySolution qs = rs.next();
		        	Resource  res = qs.getResource("s");
		        	if(res!=null) {
		        		String lbl="lbl";
		        		if(res.toString().indexOf("Q")>0) {
		        			lbl = res.toString().substring(res.toString().indexOf("Q"),res.toString().length()-1);
		        		}
		        		bw.write("<"+res.toString()+">\t<http://www.wikidata.org/prop/direct/P279>\t"+data[0]+" .\n");
		        		bw.write("<"+res.toString()+">\t<http://www.w3.org/2000/01/rdf-schema#label>\t\""+lbl+"\" .\n");

		        	}
		        }

		        qexec.close();//これがないと，途中でクエリの応答がしなくなるので注意！

		        //クエリの作成
				queryStr = "select ?s ?o where{?s <http://www.wikidata.org/prop/direct/P279> ?o ."
								+"?o <http://www.wikidata.org/prop/direct/P279> "+data[0]+" .}LIMIT 1000";
				// クエリの実行[2段目]
				query = QueryFactory.create(queryStr);
				qexec = QueryExecutionFactory.sparqlService("http://kozaki-lab.osakac.ac.jp/agraph/NEDO_pj_11"	, query) ;
	            ((QueryEngineHTTP)qexec).addParam("timeout", "10000") ;
		        rs = qexec.execSelect();

		        while(rs.hasNext()) {
		        	QuerySolution qs = rs.next();
		        	Resource  res1 = qs.getResource("s");
		        	Resource  res2 = qs.getResource("o");
		        	if(res1!=null && res2!=null ) {
		        		String lbl="lbl";
		        		if(res1.toString().indexOf("Q")>0) {
		        			lbl = res1.toString().substring(res1.toString().indexOf("Q"),res1.toString().length()-1);
		        		}
		        		bw.write("<"+res1.toString()+">\t<http://www.wikidata.org/prop/direct/P279>\t<"+res2.toString()+"> .\n");
		        		bw.write("<"+res1.toString()+">\t<http://www.w3.org/2000/01/rdf-schema#label>\t\""+lbl+"\" .\n");
		        		//bw.write("<"+res1.toString()+">\t<http://www.wikidata.org/prop/direct/P279>\t"+data[0]+" .\n");
		        	}
		        }

		        qexec.close();//これがないと，途中でクエリの応答がしなくなるので注意！


		        //クエリの作成[3段目]
				queryStr = "select ?s ?o where{?s <http://www.wikidata.org/prop/direct/P279> ?o ."
						+"?o  <http://www.wikidata.org/prop/direct/P279> ?o2 ."
						+"?o2 <http://www.wikidata.org/prop/direct/P279> "+data[0]+" .}LIMIT 1000";
				//クエリの実行[3段目]
				query = QueryFactory.create(queryStr);
				qexec = QueryExecutionFactory.sparqlService("http://kozaki-lab.osakac.ac.jp/agraph/NEDO_pj_11"	, query) ;
	            ((QueryEngineHTTP)qexec).addParam("timeout", "10000") ;
		        rs = qexec.execSelect();

		        while(rs.hasNext()) {
		        	QuerySolution qs = rs.next();
		        	Resource  res1 = qs.getResource("s");
		        	Resource  res2 = qs.getResource("o");
		        	if(res1!=null && res2!=null ) {
		        		bw.write("<"+res1.toString()+">\t<http://www.wikidata.org/prop/direct/P279>\t<"+res2.toString()+"> .\n");
			        	//bw.write("<"+res1.toString()+">\t<http://www.wikidata.org/prop/direct/P279>\t"+data[0]+" .\n");
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

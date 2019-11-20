import java.io.File;
import java.io.FileNotFoundException;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/* 「小説名」と「シーン番号」を指定して，その「シーンの内容（トリプル一覧）」を出力する
 *   引数 = <小説名> <シーン番号>
 *   例) DancingMen 003
 *
 * */
public class sample1 {

	static public void main(String[] args) throws FileNotFoundException{
		//起動時の引数の読み込み
		String story = args[0];
		String num = args[1];

	//RDFを操作する為のModelを作成
		Model model = ModelFactory.createDefaultModel() ;


		File file = new File("input/"+story+".ttl");//読み込むRDFファイルを指定
		System.out.println(file.getName()+"を読み込み...");


	//RDFの形式を指定して読み込む
		model.read(file.getAbsolutePath(), "TURTLE") ;
		//model.read(file.getAbsolutePath(), "RDF") ;
		//model.read("input/IOBC_jp_label.nt","N-TRIPLE") ;


		//クエリの作成
		String queryStr = "select ?p ?o\r\n" +
				"{<http://kgc.knowledge-graph.jp/data/"+story+"/"+num +"> ?p ?o .}";
        Query query = QueryFactory.create(queryStr);

        //クエリの実行
        QueryExecution qexec = QueryExecutionFactory.create(query, model);


		// クエリの実行.
        ResultSet rs = qexec.execSelect();

        // 結果の出力
     	ResultSetFormatter.out(System.out, rs, query);		//表形式で，標準出力に


	}
}

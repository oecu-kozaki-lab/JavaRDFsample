import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

/* 「小説名」と「シーン番号」を指定して，「そのシーンの内容（トリプル一覧）」を出力する
 *   引数 = <小説名> <シーン番号>
 *   例) DancingMen 003
 *
 * */
public class sample2 {

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


		String sceneURI = "<http://kgc.knowledge-graph.jp/data/"+story+"/"+num +"> ";

		ArrayList<Resource> subjects = getSubjectByScene(model, sceneURI);

		//クエリ結果を使って別クエリを発行するコードを以下に書く


	}

	//指定した場面の「subject（動作主）」を取得する
	static ArrayList<Resource> getSubjectByScene(Model model, String sceneURI) {
		System.out.println(sceneURI+"の「subject（動作主）」を取得する");

		//クエリの作成
		String queryStr = "select ?o\r\n" +
				"{"+sceneURI + "<http://kgc.knowledge-graph.jp/ontology/kgc.owl#subject> ?o .}";
        Query query = QueryFactory.create(queryStr);

        //クエリの実行
        QueryExecution qexec = QueryExecutionFactory.create(query, model);

     	// クエリの実行.
        ResultSet rs = qexec.execSelect();

        ArrayList<Resource> subjects = new ArrayList<Resource> ();

        while(rs.hasNext()) {
        	QuerySolution qs = rs.next();
        	Resource  res = qs.getResource("o");
        	if(res!=null) {
        		subjects.add(res);
        		System.out.println(res.toString());
        	}
        }


		return subjects;
	}
}

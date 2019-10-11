import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;


public class searchRDF {

	static public void main(String[] args) throws FileNotFoundException{
	//RDFを操作する為のModelを作成
		Model model = ModelFactory.createDefaultModel() ;


		File file = new File("input/DancingMen.ttl");//読み込むRDFファイルを指定
		System.out.println(file.getName()+"...");


	//RDFの形式を指定して読み込む
//		model.read(file.getAbsolutePath(), "RDF") ;
//		model.read("input/IOBC_jp_label.nt","N-TRIPLE") ;
		model.read(file.getAbsolutePath(), "TURTLE") ;


/*　以下のコードは，
 *  　https://github.com/KnowledgeGraphJapan/Apache-Jena-Sample-Programs/tree/master/src/main/java/jp/riken/accc/lod/symposium/sample
 * を参考にして実装

 * */

//	 	主語となっているResourceのリストを取得する
			System.out.println("==== listSubjects ==========");
			ResIterator rit = model.listSubjects();
			while( rit.hasNext() ) {
				Resource res = rit.next();
				System.out.println(res.getURI());
			}


			// 主語を指定してStatementのリストを取得する
			System.out.println("==== listStatements ========");
			Resource res = model.getResource("http://kgc.knowledge-graph.jp/data/DancingMen/129");
			StmtIterator sit = model.listStatements(res, (Property)null, (RDFNode)null);
			while( sit.hasNext() ) {
				Statement st = sit.next();
				Property prop = st.getPredicate();
				RDFNode objNode = st.getObject();
				System.out.println(prop.getURI() + ", " + objNode.toString());
			}


	}
}

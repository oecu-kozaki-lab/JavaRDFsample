import java.io.FileOutputStream;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public class ExampleDBpedia1
{
    static public void main(String...argv)
    {
        String queryStr = "select * where{?s ?p ?o.}LIMIT 10";
        Query query = QueryFactory.create(queryStr);

        try ( QueryExecution qexec = QueryExecutionFactory.sparqlService(
        		"https://query.wikidata.org/sparql" //Enpointの設定
        		, query) ) {

            // Set the DBpedia specific timeout.
            ((QueryEngineHTTP)qexec).addParam("timeout", "10000") ;

            FileOutputStream out;
			out = new FileOutputStream("output/dbp-output.txt");

			// クエリの実行.
            ResultSet rs = qexec.execSelect();

            ResultSetFormatter.outputAsCSV(out, rs);
            ResultSetFormatter.outputAsCSV(System.out, rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

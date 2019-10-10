import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;


public class readRDF {

	static public void main(String[] args) throws FileNotFoundException{
		Model model = ModelFactory.createDefaultModel() ;


		File file = new File("input/DancingMen.ttl");//読み込むRDFファイルを指定
		System.out.println(file.getName()+"...");
		String ftype = ".ttl";

//読み込むRDFの形式に応じて，選択
//		model.read(file.getAbsolutePath(), "RDF") ;
//		model.read("input/IOBC_jp_label.nt","N-TRIPLE") ;
		model.read(file.getAbsolutePath(), "TURTLE") ;

//読み込んだRDFをそのまま
		FileOutputStream out;

		out = new FileOutputStream("output/"+file.getName().replaceAll(ftype, ".ttl"));
		model.write(out,  "TURTLE");

		out = new FileOutputStream("output/"+file.getName().replaceAll(ftype, ".rdf"));
		model.write(out,  "RDF/XML");

		out = new FileOutputStream("output/"+file.getName().replaceAll(ftype, ".nt"));
		model.write(out,  "N-TRIPLE");



	}
}

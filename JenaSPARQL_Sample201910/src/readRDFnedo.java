import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;


public class readRDFnedo {

	static public void main(String[] args) throws FileNotFoundException{
	//RDFを操作する為のModelを作成
		Model model = ModelFactory.createDefaultModel() ;

	    long ld_time = System.currentTimeMillis();

		File file = new File("input/暫定版NEDOオントロジー1st_201224.nt");//読み込むRDFファイルを指定
		System.out.println(file.getName()+"...");


	//RDFの形式を指定して読み込む
//		model.read(file.getAbsolutePath(), "RDF") ;
		model.read(file.getAbsolutePath(),"N-TRIPLE") ;
//		model.read(file.getAbsolutePath(), "TURTLE") ;

		long lded_time = System.currentTimeMillis();
//		System.out.print("読み込み所要時間（ミリ秒）：");
//		System.out.println(ld_time-lded_time);


	//読み込んだRDFの形式を変換して保存する
//		FileOutputStream out;
//		out = new FileOutputStream("output/sampleOUTPUTnedo.ttl");
//		model.write(out,  "TURTLE");

	/* 元のファイル名の拡張子を変えて保存したいときのサンプル

		String ftype = ".ttl"; //元の拡張子
		out = new FileOutputStream("output/"+file.getName().replaceAll(ftype, ".ttl"));
		model.write(out,  "TURTLE");

		out = new FileOutputStream("output/"+file.getName().replaceAll(ftype, ".rdf"));
		model.write(out,  "RDF/XML");

		out = new FileOutputStream("output/"+file.getName().replaceAll(ftype, ".nt"));
		model.write(out,  "N-TRIPLE");
		*/


		try {
			//File f = new File("F:/latest-truthy.nt");
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(file),"UTF-8"));
			String line="";
//			File saveFile = new File("F:/wikidata_min_20200306.nt");//""+f.getName().replaceAll(".tsv", "")+".ttl");

//			FileOutputStream out;
//			out = new FileOutputStream(saveFile);
//			OutputStreamWriter ow = new OutputStreamWriter(out, "UTF-8");
//			BufferedWriter bw = new BufferedWriter(ow);

//			int i=0;
//			int c=0;

			long st_time = System.currentTimeMillis();

			//ファイルを1行ずつ読み込んで処理する
			//10:45 -> 12:11 約1時間半かかった
			while(br.ready()) {
				line = br.readLine();
				String[] data = line.split(" ");

				Resource res = model.getResource(data[0]);
				if(res != null) {
					System.out.println(res.toString()+":::OK!");
				}
//				if(line.contains("<http://www.wikidata.org/prop/direct/P279>")==true){
//					//bw.write(line+"\n");
//					c++;
//				}
			}


			System.out.print("読み込み所要時間（ミリ秒）：");
			System.out.println(lded_time-ld_time);

			long ed_time = System.currentTimeMillis();
			System.out.print("所要時間（ミリ秒）：");
			System.out.println(ed_time-st_time);
		}
		catch(Exception e) {

		}

	}
}

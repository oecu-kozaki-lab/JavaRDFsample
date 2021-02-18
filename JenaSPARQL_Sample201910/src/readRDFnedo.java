import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

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
			File f = new File("F:/Wikidata_full_201013/latest-truthy_201013.nt");
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(f),"SJIS"));
			String line="";

			File saveFile = new File("F:/nedo_relation_201013test.nt");//""+f.getName().replaceAll(".tsv", "")+".ttl");
			FileOutputStream out;
			out = new FileOutputStream(saveFile);
			OutputStreamWriter ow = new OutputStreamWriter(out, "UTF-8");
			BufferedWriter bw = new BufferedWriter(ow);

			int i=0;
			int hit=0;

			long st_time = System.currentTimeMillis();

			//ファイルを1行ずつ読み込んで処理する
			while(br.ready()) {
				line = br.readLine();
				String[] data = line.split(" ");

				Resource res_s =  model.getResource(data[0].replace("<","").replace(">",""));
				Resource res_o = model.getResource(data[2].replace("<","").replace(">",""));
				
				if((model.containsResource(res_s)) || model.containsResource(res_o)) {
					bw.write(line+"\n");
					hit++;
				}

				i++;

				if(i%10000==0) {
					System.out.println("..."+hit+"/"+i);
				}

			}


			System.out.print("読み込み所要時間（ミリ秒）：");
			System.out.println(lded_time-ld_time);

			br.close();
			bw.close();

			long ed_time = System.currentTimeMillis();
			System.out.print("所要時間（ミリ秒）：");
			System.out.println(ed_time-st_time);
		}
		catch(Exception e) {
			System.out.println(e);
		}

	}
}

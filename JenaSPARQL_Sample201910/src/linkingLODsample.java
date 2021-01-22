import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class linkingLODsample {

	public static void main(String[] args) {

		try {
			//入力ファイル指定
			File fileLOD = new File("input/TestLOD.txt");
			//ファイルの読み込み用のReaderの設定
			BufferedReader brLOD = new BufferedReader(	new InputStreamReader(new FileInputStream(fileLOD),"UTF8"));

			ArrayList<String[]> list = new ArrayList<String[]>();

			//LODのマッピング用「Word」と「URI」をリストに保存
			while(brLOD.ready()) {
				String line = brLOD.readLine();
				System.out.println("list+"+line);

				String[] data = line.split("\t");//指定した「区切り文字」で分割し，Stringの配列に格納する
				list.add(data);
			}


			//入力ファイル指定
			File file = new File("input/TestDoc.txt");
			//ファイルの読み込み用のReaderの設定
			BufferedReader br = new BufferedReader(	new InputStreamReader(new FileInputStream(file),"UTF8"));

			//出力ファイル指定
			File fileOUT = new File("output/linkingLOD-output.txt");
			//出力用のファイルのWiterの設定
			FileOutputStream out = new FileOutputStream(fileOUT);
			OutputStreamWriter ow = new OutputStreamWriter(out, "UTF-8");
			BufferedWriter bw = new BufferedWriter(ow);

			int doc_num = 0;

			while(br.ready()) {
				doc_num++;
				String line = br.readLine();
				String[] data = line.split("\t");//指定した「区切り文字」で分割し，Stringの配列に格納する

				System.out.println(data[0]);

				for (int i = 0; i < list.size(); i++) {
		            System.out.println("..."+list.get(i)[0]);

		            /* 本来は，同じ文に同じ単語が複数含まれる場合も判定できるような処理にする必要あり
		             * →data[0].indexOf(対象単語, 何文字目から調べるか)を使えばよい		             *
		             *
		             * */
		            int index = data[0].indexOf(list.get(i)[0]);
		            if(index>=0) {
		            	System.out.println("#### MATCH ####");
		            	bw.write(doc_num +":\t" + index +"\t"+list.get(i)[0]+"\t"+list.get(i)[1]+"\n");
		            	/* 出力形式は，元の文書に「タグを埋め込む」などの書式を考えても良い */
		            }
		        }

//				for(int i=0;i<data.length;i++) {
//					System.out.println(i+":"+data[i]);
//					}
			}



			//入出力のストリームを閉じる【これを忘れると，ファイル処理が正しく終わらない】
			br.close();
	     	bw.close();
		}
		catch(Exception e) {
			 e.printStackTrace();
		}
	}

}

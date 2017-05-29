package shineware.komoran;

import java.io.File;
import java.util.List;

import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;
import kr.co.shineware.util.common.model.Pair;

public class KmaReturn {
	
	private static String knowledgePath;
	private static String userDictionary;
	private static Komoran komoran;
	
	static {
		knowledgePath = System.getProperty("knowledge_path");
		userDictionary = System.getProperty("knowledge_path")+File.separator+"user_dict.txt";
		komoran = new Komoran(knowledgePath);
	}
	
	public static String getMorphTag(String text){
		// corpus_build에 있는 데이터로부터 models 생성
		//System.out.println("<< " + text);
		
		komoran.setUserDic(userDictionary);
		List<List<Pair<String,String>>> result = komoran.analyze(text);
		
		StringBuffer sb = new StringBuffer();
		for (List<Pair<String, String>> eojeolResult : result) {
			for (Pair<String, String> wordMorph : eojeolResult) {
				//System.out.println(wordMorph);
				sb.append(wordMorph.getFirst().replaceAll(" ", "")).append("/").append(wordMorph.getSecond());
				sb.append("+");
			}
			sb.deleteCharAt(sb.length()-1);
			//System.out.println();
			sb.append(" ");
		}
		
		//System.out.println(">> " + sb.toString());
		return sb.toString();
	}
	
	public static void main(String[] args){
		System.out.println(KmaReturn.getMorphTag("하이브리드를 살 경우 최대 230만 원까지 지원금을 받을 수 있다"));
	}
}

package shineware.komoran;

import java.util.List;

import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;
import kr.co.shineware.util.common.model.Pair;

public class Sample {
	
	public static void main(String[] args) {
		// corpus_build에 있는 데이터로부터 models 생성
		Komoran komoran = new Komoran("E:\\dev\\komoran\\models-full");
		
		List<List<Pair<String,String>>> result = komoran.analyze("10월말에 차가  나왔으니 2달쯤  되었네요. 지방으로  어디로 돌아다니면서  살짝  아쉬운감 몇가지  적어  봅니다");
		
		for (List<Pair<String, String>> eojeolResult : result) {
			for (Pair<String, String> wordMorph : eojeolResult) {
				System.out.println(wordMorph);
			}
			System.out.println();
		}

	}
}

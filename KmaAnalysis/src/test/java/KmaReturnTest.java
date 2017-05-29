import static org.junit.Assert.*;

import org.junit.Test;

import shineware.komoran.KmaReturn;


public class KmaReturnTest {

	@Test
	public void wiseKmaOrangeTest() {
		String text = "미국 컨슈머리포트 조사에서 동급 모델 중 연비와 가속 성능의 조합이 가장 우수한 것으로";
		System.out.print(text + " >> " + com.wisenut.core.KmaReturn.getMorphTag(text));
	}
	
	@Test
	public void komoranTest() {
		String text = "미국 컨슈머리포트 조사에서 동급 모델 중 연비와 가속 성능의 조합이 가장 우수한 것으로";
		System.out.print(text + " >> " + shineware.komoran.KmaReturn.getMorphTag(text));
	}

}

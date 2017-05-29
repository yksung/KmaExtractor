package dmap.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.wisenut.test.Kma2Excel;

import shineware.komoran.KmaReturn;

public class KmaGroupsTest {

	@Test
	public void testGetKmaGroups() {
		Kma2Excel k2e = new Kma2Excel();
		
		ArrayList<String> list = k2e.getKmaGroups("G80이 말리부보다 많이 팔리는것도 놀랍고 QM6는 첫달 싼타페 넘어서서 와 대박했는데 결국 싼타페 쏘렌토에 이어 투싼 스포티지한테도 밀리네요 ㅠㅠ 티볼리는 늘 그렇듯 소형suv왕자 차지");
		for(String str : list){
			System.out.println(str);
		}

	}

}

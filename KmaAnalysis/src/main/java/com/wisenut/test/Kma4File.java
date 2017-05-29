package com.wisenut.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wisenut.core.KmaReturn;

public class Kma4File {
	public static Pattern regex1 = Pattern.compile("[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]+(/NFG|/NNG)+\\+[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]+/XSD");
	public static Pattern regex2 = Pattern.compile("[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]+(/VA|/VV)");
	public static Pattern regex3 = Pattern.compile("[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]+/NNG\\+[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]+/XSN\\+[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]+(/VV|/XSD)");
	
	public static void main(String[] args) {
		String original = args[0];
		String result = args[1];
		
		extractKMA(original, result);
	}
	
	/***
		파일을 읽어서 형태소 분석된 결과를 +로만 연결해 다른 파일에 써주는 함수.
	*/

	public static void extractKMA(String original, String result){
		System.out.println("###### original : " + original);
		System.out.println("###### result : " +result);
		String toRead = original;
		String tempFile = toRead.substring(0, original.lastIndexOf(File.separator)+1)+result+".txt";
		//String resultFile = tempFile +"_result.txt";
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(toRead));
			BufferedWriter out = new BufferedWriter(new FileWriter(tempFile));
			String line = "";
			
			int count=0;
			while( (line = br.readLine()) != null){
				if(count % 1000 == 0){
					System.out.print("["+count+"]");
				}else{					
					System.out.print(".");
				}
				String mod = getKMAresult(KmaReturn.getMorphTag(line));
				//String mod2 = getKMAresult(mod1);
				// write 할 때 원문과 화제어를 함께 보려고 하는 경우
				// bica에서 필요한 형태소 분석 결과만 
				out.write(mod);
				out.newLine();
				//Thread.sleep(50);
				
				count++;
			}
			br.close();
			out.close();
			KmaReturn.closeAll();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	private static String getKMAResult2(String lineData){
		StringBuffer sb = new StringBuffer();
		List<String> list = findTagSet(lineData);
					
		for(String str : list){
			sb.append(str);
			sb.append("\t");
		}
					
		return sb.toString();
	}

	
	private static String getKMAresult(String lineData){
		lineData = lineData.replace("\t", "");
		lineData = lineData.replace("<sentence>", "");
		lineData = lineData.replace("</sentence>", "");
		lineData = lineData.replace("<![CDATA[", "");
		lineData = lineData.replace("]]>", "");
		lineData = lineData.replace("/NNG", "");
		lineData = lineData.replace("/NFG", "");
		lineData = lineData.replace("/NNP", "");
		lineData = lineData.replace("/NNC", "");
		lineData = lineData.replace("/NNU", "");
		lineData = lineData.replace("/NFU", "");
		lineData = lineData.replace("/NNI", "");
		lineData = lineData.replace("/NU", "");
		lineData = lineData.replace("/NP", "");
		lineData = lineData.replace("/NNB", "");
		lineData = lineData.replace("/NNR", "");
		lineData = lineData.replace("/VV", "");
		lineData = lineData.replace("/VA", "");
		lineData = lineData.replace("/EM", "");
		lineData = lineData.replace("/ETM", "");
		lineData = lineData.replace("/ETN", "");
		lineData = lineData.replace("/EP", "");
		lineData = lineData.replace("/N_", "");
		lineData = lineData.replace("/V_", "");
		lineData = lineData.replace("/VX", "");
		lineData = lineData.replace("/VC", "");
		lineData = lineData.replace("/IC", "");
		lineData = lineData.replace("/MA", "");
		lineData = lineData.replace("/MM", "");
		lineData = lineData.replace("/J_", "");
		lineData = lineData.replace("/E_", "");
		lineData = lineData.replace("/XSD", "");
		lineData = lineData.replace("/XP", "");
		lineData = lineData.replace("/XSN", "");
		lineData = lineData.replace("/FL", "");
		lineData = lineData.replace("/SN", "");
		lineData = lineData.replace("/SC", "");
		lineData = lineData.replace("/UW", "");
		return lineData;
	}
	
	public static List<String> findTagSet(String sentence){
		List<String> returnList = new ArrayList<String>();
		
		if(sentence != null && sentence.length()>0 ){
			Matcher matcher1 = regex1.matcher(sentence);
			while(matcher1.find()){
				//System.out.println(matcher1.group());
				returnList.add(matcher1.group());
			}
			Matcher matcher2 = regex2.matcher(sentence);
			while(matcher2.find()){
				//System.out.println(matcher2.group());
				returnList.add(matcher2.group());
			}
			Matcher matcher3 = regex3.matcher(sentence);
			while(matcher3.find()){
				//System.out.println(matcher3.group());
				returnList.add(matcher3.group());
			}
		}
		
		return returnList;
	}
	
}

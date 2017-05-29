package com.wisenut.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



import shineware.komoran.KmaReturn;
//import com.wisenut.core.KmaReturn;

public class Kma2Excel {

	public static void main(String[] args) {
		String filePath = args[0];
		int columnNum = Integer.parseInt(args[1]);
		
		Kma2Excel k2e = new Kma2Excel();
		k2e.kma2excel(filePath, columnNum);
	}
	
	public void kma2excel(String excelPathToRead, int columnLength){
		System.out.println(">>> \"" + excelPathToRead + "\" Analysis starts.");
		// 결과 파일 준비
		FileOutputStream isToWrite = null;
		XSSFWorkbook excelToWrite = null;
		XSSFSheet sheetToWrite = null;
		XSSFRow rowToWrite = null;
		XSSFCell cellToWrite = null;
		
		// 원본 파일 준비
		FileInputStream isToRead = null;
		XSSFWorkbook excelToRead = null;
		XSSFSheet sheetToRead = null;
		XSSFRow row;
		XSSFCell cell;
		
		try {
			// 읽을 파일 관련 객체 초기화
			String dir = excelPathToRead.substring(0, excelPathToRead.lastIndexOf(File.separator));
			isToWrite = new FileOutputStream(dir + File.separator + "분석결과.xlsx");
			excelToWrite = new XSSFWorkbook();
			sheetToWrite = excelToWrite.createSheet();
			
			// 쓸 파일 관련 객체 초기화
			isToRead = new FileInputStream(excelPathToRead);
			excelToRead = new XSSFWorkbook(isToRead);
			sheetToRead = excelToRead.getSheetAt(0);
			
			//취득된 sheet에서 rows수 취득
			int rows = sheetToRead.getLastRowNum();
			System.out.print(String.format("Total(%d) >> ", rows));
			
			//취득된 row에서 취득대상 cell수 취득
			//int cells = sheetToRead.getRow(0).getPhysicalNumberOfCells(); //
			//System.out.println(excelToRead.getSheetName(sheetIdx) + " sheet의 row에 취득대상 cell수 : " + cells);
			
			for (int r = 0; r < rows; r++) {
				row = sheetToRead.getRow(r); // row 가져오기
				rowToWrite = sheetToWrite.createRow(r); // 원본 파일과 싱크를 맞춤.
				
				String sentence = "";
				ArrayList<String> kmaGroups = null;
				if (row != null) {
					for (int c = 0; c < columnLength; c++) {
						cellToWrite = rowToWrite.createCell(c);
						
						cell = row.getCell(c);
						String value = null;
						
						if (cell != null) {
							if(cell.getCellTypeEnum() == CellType.FORMULA){
								value = cell.getCellFormula();
							}else if(cell.getCellTypeEnum() == CellType.NUMERIC){
								value = "" + cell.getNumericCellValue();
							}else if(cell.getCellTypeEnum() == CellType.STRING){
								value = "" + cell.getStringCellValue();
							}else if(cell.getCellTypeEnum() == CellType.BLANK){
								value = "[null 아닌 공백]";
							}else if(cell.getCellTypeEnum() == CellType.ERROR){
								value = "" + cell.getErrorCellValue();
							}
							//System.out.print(value + "\t");
						}
						
						// 0번째 column은 문장. 문장은 형태소 분석 실행.
						if(c==0){
							sentence = value;
						}
						
						cellToWrite.setCellValue(value);
					} // for(c) 문
					
					kmaGroups = getKmaGroups(sentence);
					for(int i=0; i<kmaGroups.size(); i++){
						cellToWrite=rowToWrite.createCell(i+columnLength);
						cellToWrite.setCellValue(kmaGroups.get(i));
					}
				}
				
				if(r%1000==0){						
					System.out.print("["+r+"]");
				}
				if(r>0 && r%100==0){						
					System.out.print(".");
				}
			} // for(r) 문
			
			excelToWrite.write(isToWrite);
			System.out.println("\n>> \""+dir + File.separator + "분석결과.xlsx"+"\" writing completed.");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(isToWrite != null){
				try {
					isToWrite.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(isToRead != null){
				try {
					isToRead.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(excelToWrite != null){
				try {
					excelToWrite.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(excelToRead != null){
				try {
					excelToRead.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public ArrayList<String> getKmaGroups(String text){
		ArrayList<String> groups = new ArrayList<String>();
		String clearPattern = "[^\\w^ㄱ-ㅎ^가-힣]";
		String tagPattern = "\\/[a-zA-Z_]+";
		String splitPattern = "(\\s|[\\+]+[^\\w]+\\/SP[\\+]+)";
		
		String[] independentNouns = {"NNG", "NNP", "XR"};
		String[] dependentNouns = {"MM", "NNB", "XPN", "XSN", "SN", "SL", "NR", "XSA"};
		try{
			text = text.replaceAll(clearPattern, " ");
			String kmaResult = KmaReturn.getMorphTag(text);
			
			for(String s : kmaResult.split(splitPattern)){
				if(s.contains("/NNG") || s.contains("/NNP")){
					if(s.split("[+]").length>1){
						ArrayList<String> validToken = new ArrayList<String>();
						for(String token : s.split("[+]")){
							String[] word_tag = token.split("/");
							// 의존명사가 포함되어 있는 경우
							if(Arrays.asList(independentNouns).contains(word_tag[1]) || Arrays.asList(dependentNouns).contains(word_tag[1])){
								validToken.add(word_tag[0]);
							}
						}
						groups.add(String.join("", validToken));
					}else{
						groups.add(s.replaceAll(tagPattern, ""));
					}
				}else if(s.contains("/SN")){
					if(s.split("[+]").length>1){
						ArrayList<String> validToken = new ArrayList<String>();
						for(String token : s.split("[+]")){
							String[] word_tag = token.split("/");
							// 의존명사가 포함되어 있는 경우
							if(Arrays.asList(dependentNouns).contains(word_tag[1])){
								validToken.add(word_tag[0]);
							}
						}
						groups.add(String.join("", validToken));
					}
				}else if(s.contains("/VV")){
					for(String token : s.split("[+]")){
						if(token.contains("/VV")){						
							groups.add(token.replaceAll(tagPattern, "")+"다");
						}
					}
				}else if(s.contains("/VA")){
					for(String token : s.split("[+]")){
						if(token.contains("/VA")){						
							groups.add(token.replaceAll(tagPattern, "")+"다");
						}
					}
				}else if(s.contains("/XR")){
					if(s.split("[+]").length>1){
						ArrayList<String> validToken = new ArrayList<String>();
						for(String token : s.split("[+]")){
							String[] word_tag = token.split("/");
							// 의존명사가 포함되어 있는 경우
							if(Arrays.asList(independentNouns).contains(word_tag[1]) || Arrays.asList(dependentNouns).contains(word_tag[1])){
								validToken.add(word_tag[0]);
							}
						}
						groups.add(String.join("", validToken)+"다");
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage() + " >>> \""+text+"\"");
		}
		
		return groups;
	}

}

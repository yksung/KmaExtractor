package com.wisenut.core;
import java.util.*;

import kr.co.wisenut.wisekmaorange.*;

/**
*	@brief	sample Class

e	This class is a sample class for executing wisekma-orange.
*/
public class KmaReturn {
	private static WK_Knowledge knowledge;
	private static WK_Analyzer analyzer;
	private static WK_Eojul eojul;
	private static String knowledgePath;
	
	public static void main(String[] args){
		System.out.println("최종결과 : " + KmaReturn.getMorphTag(" BMW센터에서 정비를 받을 수 있게끔 해주었으면 좋겠다하심"));
		KmaReturn.closeAll();
	}
	
	static {
		knowledgePath = System.getProperty("knowledge_path");
		// native library 세팅
		new LoadLibrary();
		
		int ret = 0;
		
		// Create knowledge
		knowledge = new WK_Knowledge();
		knowledge.createObject();

		// Load System Dictionary
		ret = knowledge.loadKnowledge( knowledgePath, true, false );
		if( ret < 0 ) {
			System.out.println( "Knowledge loading faild!!!" );

			System.exit(1);
		}

		// Load UserNoun Dictionary
		ret = knowledge.loadUserNoun( knowledgePath, "usernoun.txt", true );
		if( ret < 0 ) {
			System.out.println( "UserNoun loading faild!!!" );
			System.exit(1);
		}
	}
	
	public static void setKnowledge(){
		int ret = 0;
		
		// Create Analysis
		//WK_Analyzer analyzer = new WK_Analyzer( knowledge );
		analyzer = new WK_Analyzer( knowledge );

		// Create Eojul
		eojul = new WK_Eojul();
		eojul.createObject();

		// Set Eojul for analyzing
		analyzer.setEojul( eojul );
		

		// Set Option for Analzyer
		analyzer.setOption( WK_Global.WKO_OPTION_N_BEST, 5 );
		analyzer.setOption( WK_Global.WKO_OPTION_EXTRACT_ALPHA, 1 );
		analyzer.setOption( WK_Global.WKO_OPTION_EXTRACT_NUM, 1 );
		analyzer.setOption( WK_Global.WKO_OPTION_EOJUL_OFFSET_MODE, 1 );
	}
	
	public static String getMorphTag(String text){
		//System.out.println("[getMorphTag] text : " + text);
		
		setKnowledge();
		
		//HashMap<String,List<String>> resultMap = new HashMap<String, List<String>>();
		StringBuffer sb = new StringBuffer();
		
		StringTokenizer tokens = new StringTokenizer( text, "  \t\r\n?!.-,\"'()[]<>~\\:;`_/|+=*&^%$#@{}" );
		if( tokens.countTokens() == 0 ){
			return null;
		}

		for( int posOfToken = 0; tokens.hasMoreElements(); posOfToken++ ){
			int ret = 0;

			String eojulStr = tokens.nextToken();
			if("".equals(eojulStr.trim())){
				continue;
			}

			// Eojul Initialize
			eojul.eojulInitialize();

			// Set String for analyzing
			eojul.setString( eojulStr );

			// analyzing
			ret = analyzer.runWithEojul();
			if( ret < 0){
				System.out.println( "Analyze Faild: " + eojul.getString() );

				break;
			}

			// Print Result
			//System.out.println( "\n> [" + eojul.getString() + "]" );

			for( int posOfList = 0; posOfList < 1/*eojul.getListSize()*/; posOfList++ ){
				sb.append(" ");
				for( int posOfMorph = 0; posOfMorph < eojul.getCount(posOfList); posOfMorph++ )	{
					if( posOfMorph != 0 ) {
						//System.out.print( "+" );
						sb.append("+");
					}else{
						//System.out.print( "\t" + posOfList +" : ");
					}
					String morph = eojul.getLexicon(posOfList, posOfMorph);
					String tag  = eojul.getStrPOS(posOfList, posOfMorph);

					//System.out.print(morph + "/" + tag);
					sb.append(morph + "/" + tag);
				}

				//System.out.println("");
			}
			
		}
		
		return sb.toString().trim();
	}
	
	public static void closeAll(){
		eojul.finalize();
		analyzer.finalize();
		knowledge.finalize();
	}
}

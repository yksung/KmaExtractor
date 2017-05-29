package com.wisenut.test;
import java.io.*;
import java.util.*;

import com.wisenut.core.LoadLibrary;

import kr.co.wisenut.wisekmaorange.*;

/**
*	@brief	sample Class

e	This class is a sample class for executing wisekma-orange.
*/
public class sample
{
	public static void main( String [] args ) throws Exception
	{
		if( args.length == 0 )
		{
			System.out.println( "Wise KMA Orange, Korean Morphological Analysis" );
			System.out.println( "" );
			System.out.println( "Usage:" );
			System.out.println( "   sample <Knowledge Path>" );
			System.out.println( "" );

			return;
		}

		int ret = 0;
		
		new LoadLibrary();
		
		// Create knowledge
		WK_Knowledge knowledge = new WK_Knowledge();
		knowledge.createObject();

		// Load System Dictionary
		ret = knowledge.loadKnowledge( args[0], true, false );
		if( ret < 0 )
		{
			System.out.println( "Knowledge loading faild!!!" );

			return;
		}

		// Load UserNoun Dictionary
		ret = knowledge.loadUserNoun( args[0], "usernoun.txt", true );
		if( ret < 0 )
		{
			System.out.println( "UserNoun loading faild!!!" );

			return;
		}

		// Create Analysis
		//WK_Analyzer analyzer = new WK_Analyzer( knowledge );
		WK_Analyzer analyzer = new WK_Analyzer( knowledge );

		// Create Eojul
		WK_Eojul eojul = new WK_Eojul();
		eojul.createObject();

		// Set Eojul for analyzing
		analyzer.setEojul( eojul );

		// Set Option for Analzyer
		analyzer.setOption( WK_Global.WKO_OPTION_N_BEST, 5 );
		//analyzer.setOption( WK_Global.WKO_OPTION_EXTRACT_ALPHA, 1 );
		analyzer.setOption( WK_Global.WKO_OPTION_EXTRACT_NUM, 1 );
		analyzer.setOption( WK_Global.WKO_OPTION_EXTRACT_VERB_STEMS, 1);
		analyzer.setOption( WK_Global.WKO_OPTION_EOJUL_OFFSET_MODE, 1 );

		// Input/Analyze
		{
			InputStreamReader is = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(is);

			while( true )
			{
				System.out.print( "Input> " );
				String str = br.readLine();

				StringTokenizer tokens = new StringTokenizer( str, "  \t\r\n?!.-,\"'()[]<>~\\:;`_/|+=*&^%$#@{}" );

				if( tokens.countTokens() == 0 )
				{
					break;
				}

				for( int posOfToken = 0; tokens.hasMoreElements(); posOfToken++ )
				{
                    String eojulStr = tokens.nextToken();

					// Eojul Initialize
					eojul.eojulInitialize();

					// Set String for analyzing
					eojul.setString( eojulStr );

					// analyzing
					ret = analyzer.runWithEojul();
					if( ret < 0)
					{
						System.out.println( "Analyze Faild: " + eojul.getString() );

						break;
					}

					// Print Result
					System.out.println( "\n> [" + eojul.getString() + "]" );

					for( int posOfList = 0; posOfList < eojul.getListSize(); posOfList++ )
					{
						for( int posOfMorph = 0; posOfMorph < eojul.getCount(posOfList); posOfMorph++ )
						{
							if( posOfMorph != 0 )
								System.out.print( " + " );
							else
								System.out.print( "\t" + posOfList +" : ");

							String morph = eojul.getLexicon(posOfList, posOfMorph);
							String tag  = eojul.getStrPOS(posOfList, posOfMorph);

							System.out.print(morph + "/" + tag);

							if( eojul.isIndexWord(posOfList, posOfMorph) == true )
								System.out.print(" [1]");
							else
								System.out.print(" [0]");
						}

						System.out.println("");
					}
				}
			}
		}

		// Finalize Object (must keep this follow)
		eojul.finalize();
		analyzer.finalize();
		knowledge.finalize();

		return;
	}
}

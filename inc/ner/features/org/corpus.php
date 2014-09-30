<?php
/**
* check in corpus
*/
class NER_Features_Org_Corpus extends NER_Features_Abstract
{
	
	public static function check(NER_WordStatistics $word, NER_Text $context)
	{

		if( in_array($word->word, Config::$corpus_org) )
			return 1;
		
		return 0;
	}
}
<?php
/**
* the word preceeded by a place indicator
*/
class NER_Tfunctions_Org_Prefix extends NER_Features_Abstract
{
	
	public static function check(NER_WordStatistics $word, NER_Text $context)
	{

		$previous = parent::previous($word, $context);
		if( $previous!=null and in_array($previous->word, Config::$org_preceedor) ){
			return 1;
		}
		
		return 0;
	}
}
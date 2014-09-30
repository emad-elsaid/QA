<?php
/**
* if the word preceeded by a honored word then it is 
* a name
*/
class NER_Tfunctions_Pers_Honorship extends NER_Features_Abstract
{
	
	
	public static function check(NER_WordStatistics $word, NER_Text $context)
	{

		$previous = parent::previous( $word, $context );
		if($previous==null)
			return 0;

		foreach(config::$honors as $honor){
			if( strstr($previous->word, $honor)>-1 )
				return 1;
		}

		return 0;
	}
}
<?php
/**
* if word is followed by or preceeded by name separator like
* ibn , bn, ..etc
*/
class NER_Features_Pers_Nameseparator extends NER_Features_Abstract
{
	
	
	public static function check(NER_WordStatistics $word, NER_Text $context)
	{

		$previous = parent::previous( $word, $context );

		if( $previous!=null and in_array($previous->word, Config::$person_separator ) )
			return 1;

		$next = parent::next( $word, $context );
		if( $next!=null and in_array($next->word, Config::$person_separator ) )
			return 1;

		return 0;
	}
}
<?php
/**
* the word is not a name if it is from alamat eltarqeem
*/
class NER_Features_O_Tarqeem extends NER_Features_Abstract
{
	
	public static function check(NER_WordStatistics $word, NER_Text $context)
	{

		// if 3alamet tarqeem then make it 
		if( in_array($word->word, Normalizer::$alamat_tarqeem) ){
			return 1;
		}
		
		return 0;
	}
}
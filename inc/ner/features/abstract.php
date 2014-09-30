<?php 
/**
* this is the abstract class of features for NER
* to calculate the word probability according to 
* ANER_MaxEntropy.pdf : page 147
*/
abstract class NER_Features_Abstract
{
	abstract public static function check(NER_WordStatistics $word, NER_Text $context);


	// get previous word
	public static function previous(NER_WordStatistics $word, NER_Text $context)
	{
		if( count($context->text)==0 or $word===$context->text[0] )
			return null;
		
		for($i=1; $i<count($context->text); $i++)
		{
			if($context->text[$i]===$word)
			{
				return $context->text[$i-1];
			}
		}

		return null;
	}

	public static function next(NER_WordStatistics $word, NER_Text $context )
	{
		if( count($context->text)==0 )
			return null;
		
		for($i=0; $i<count($context->text)-1; $i++)
		{
			if($context->text[$i]===$word)
			{
				return $context->text[$i+1];
			}
		}

		return null;
	}
}
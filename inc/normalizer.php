<?php
/**
* this class sanitize the text in different ways
* for exmple replace all "alef" character with a single one
* remove new lines
* remove double spaces
* replace "ya'" with single one 
* also has the stemming method
* as specified in teh reasearch : Survy_of_QA.pdf : page 281
*/
class Normalizer
{
	
	function __construct()
	{
		
	}


	public static $replacements = array(
							'ا' => array('أ','إ','آ'),
							'ئ' => array('ىء'),
							'ي' => array('ى'),
							'ﻻ' => array('ﻵ','ﻹ','ﻷ'),
							'ؤ' => array('وء'),
							" " => array("\n"), // to replace new lines with space
							"ه" => array("ة")
							);
	public static $tashkeel = array('ّ','َ','ً','ُ','ٌ','ِ','ٍ','~','ْ');
	public static $alamat_tarqeem = array('،','-','.','؟','+',':','"','\'','!','(',')');

	/*
		prepare text to be processed, before analysing any text and dealing with it
		we should prepare it first using this method, this will make sure the all text
		is written the same way in terms of character usages
	*/
	static function prepare($text)
	{
		
		$text = preg_replace_callback('/&#([0-9a-fx]+);/mi', 'replace_num_entity', $text);
		// replacing all different typing methods to single typing method
		foreach (self::$replacements as $replace_with => $replaces) {
			foreach ($replaces as $token ) {
				$text = str_replace($token, $replace_with, $text);
			}
		}

		// removeing tashkeel
		foreach (self::$tashkeel as $token) {
			$text = str_replace($token, '', $text);
		}

		// add spaces between "3alamat el-tarqeem" and previous and next word
		foreach (self::$alamat_tarqeem as $token) {
			$text = str_replace($token, " $token ", $text);
		}

		$text = str_replace('&nbsp;', ' ', $text);
		// remove double spaces
		$text = preg_replace("/\s+/", " ", $text);
		$text = html_entity_decode($text);


		return $text;
	}


	static function stem($word)
	{
		// to do
	}

	static function remove_stopwords($text)
	{
		$text = explode( ' ', $text );
		$result = array();
		foreach( $text as $word )
		{
			if( (! in_array($word, Config::$stopwords)) and (! in_array($word, Normalizer::$alamat_tarqeem) ) )
			{
				array_push($result, $word);
			}
		}

		return implode(' ', $result );
	}

}
function replace_num_entity($ord)
{
    $ord = $ord[1];
    if (preg_match('/^x([0-9a-f]+)$/i', $ord, $match))
    {
        $ord = hexdec($match[1]);
    }
    else
    {
        $ord = intval($ord);
    }
    
    $no_bytes = 0;
    $byte = array();

    if ($ord < 128)
    {
        return chr($ord);
    }
    elseif ($ord < 2048)
    {
        $no_bytes = 2;
    }
    elseif ($ord < 65536)
    {
        $no_bytes = 3;
    }
    elseif ($ord < 1114112)
    {
        $no_bytes = 4;
    }
    else
    {
        return;
    }

    switch($no_bytes)
    {
        case 2:
        {
            $prefix = array(31, 192);
            break;
        }
        case 3:
        {
            $prefix = array(15, 224);
            break;
        }
        case 4:
        {
            $prefix = array(7, 240);
        }
    }

    for ($i = 0; $i < $no_bytes; $i++)
    {
        $byte[$no_bytes - $i - 1] = (($ord & (63 * pow(2, 6 * $i))) / pow(2, 6 * $i)) & 63 | 128;
    }

    $byte[0] = ($byte[0] & $prefix[0]) | $prefix[1];

    $ret = '';
    for ($i = 0; $i < $no_bytes; $i++)
    {
        $ret .= chr($byte[$i]);
    }

    return $ret;
}
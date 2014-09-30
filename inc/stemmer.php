<?php
/**
* Arabic stemmer
* this is not working on linux and not tested on windows
*/
class Stemmer
{
	
	public static function is_working()
	{
		self::stem('.');
	}

	public static function stem($text)
	{
		file_put_contents(Config::$tmp.'/stem.o', $text);
		$out = array();
		exec( Config::$stemmer_path, $out);
		$output = implode("\n", $out);
		unlink(Config::$tmp.'/stem.o');
		return $output;
	}
	
}
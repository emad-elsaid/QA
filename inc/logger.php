<?php
/**
* logging class
*/
class Logger
{
	
	public static function log($message)
	{
		if(DEBUG)
			echo $message;
	}

	public static function box( $label, $message )
	{
		echo "<div class=\"info\"><label>$label</label><span class=\"message\">$message</span></div>";
		flush();
	}
}
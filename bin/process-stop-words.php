<?php
// this file is used to process the stopwords list
// from the application "arabic stopwords.py"
$text = file_get_contents('../data/stopwords.txt');
$text = explode("\n", $text);
$words = array();
foreach($text as $line)
{
	$l = trim($line);
	$l = explode("\t", $l);
	array_push($words, $l[0]);
}

file_put_contents( '../data/stopwords_list.txt', implode("\n",$words) );

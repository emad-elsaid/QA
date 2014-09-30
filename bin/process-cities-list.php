<?php
// cities list : http://download.maxmind.com/download/worldcities/worldcitiespop.txt.gz
$input = '../../resources/worldcitiespop.txt';
$output = '../data/cities_list.txt';

$file = file_get_contents($input);
$file = explode("\n", $file);
$cities = array();

foreach( $file as $line )
{
	$parts = explode(',', $line);
	if( $parts[0]!='Country' and $parts[1]!='City' )
	{
		array_push( $cities, $parts[0], $parts[1] );
	}

}

$cities = array_unique($cities);
$nu = count($cities);
echo "Cities found: $nu";
file_put_contents( $output, implode( "\n", $cities) );

<?php
/*
 autoloading classes 
 if the class name is "Foo_Bar_Baz"
 it will include the class file from the path 
 	'inc/foo/bar/baz.php'
 this way the system will be expandible with 
 other class easily
*/
function own_autoload($class_name) {
	$path = str_replace('_', '/', strtolower($class_name) );
	if(file_exists("inc/{$path}.php"))
	{
    	include "inc/{$path}.php";
    	return true;
	}else{
		return false;
	}
}
spl_autoload_register('own_autoload',true,true);
$config = new Config();
$trainer = new Trainer();
$trainer->train();
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
//require 'vendor/autoload.php';

// now create a new name entity recognizer to analyse the question
if( isset($_POST['q']) )
{
	// create a config class to initialize the app with its constructor
	$config = new Config();
	Config::print_corpora_statistics();

	// create question analyser
	$text = $_POST['q'];
	$ner = new NER($text);
	$question = new Question($ner);

	Logger::box( "السؤال: ", $ner->text->render() );
	Logger::box( "بعد حذف الكلمات الزائدة : ", $ner->text_without_stopwords->render() );

	// print question analysis results 
	$asking_about = array();
	foreach( $question->asking_about as $cls)
	{
		array_push( $asking_about, Config::entity_val2entity_type($cls) );
	}
	Logger::box("السؤال يمكن ان يكون عن : ", implode(', ', $asking_about));


	$passages = new Passages($ner->text);
	$p_count = count($passages->paragraphs);
	Logger::Box( 
		"الفقرات : ", "تم الحصول على {$p_count} فقرة من الإنترنت تتراوح اطوالهم من "
		.Config::$paragraphs_minimum_length
		." إلى "
		.Config::$paragraphs_maximum_length
		.' حرف' );


	$ir = new Config::$IR_Class($passages, $question);
	Logger::box("اعلى الفقرات","" );

	foreach($ir->get_top(5) as $i){
		echo '<div class="success">';
		echo $passages->paragraphs[$i];
		echo '</div>';
	}

	$answers = $ir->extract_arr($question->asking_about);
	Logger::Box("الإجابات المحتملة : ", implode(', ', $answers));

}
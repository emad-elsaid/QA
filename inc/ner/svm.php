<?php
/**
* Name entity recognition class based on Support
* vector machine algorithm
*/
use Camspiers\StatisticalClassifier\Classifier\SVM;
use Camspiers\StatisticalClassifier\DataSource\DataArray;

class NER_SVM extends NER_WordStatistics
{
	
	public static $classifier = null;
	public function process(NER_Text $context){
	}

	protected static function load_svm()
	{
		self::$classifier = new SVM($source = new DataArray());
		foreach( Config::$array_of_types as $type ){
			$array = 'corpus_' . strtolower( Config::entity_val2entity_type($type) );
			// foreach( Config::$$array as $e){
			// 	$source->addDocument($type, $e);
			// }
			$source->addDocument($type, implode(' ', Config::$$array));
		}
	}

	public function get_type()
	{
		if(self::$classifier==null)
			self::load_svm();
		return self::$classifier->classify($this->word); // string "ham"
	}

	public function render()
	{
		$data_type = Config::entity_val2entity_type($this->get_type());
		$title = "Type: $data_type";
		return "<span title=\"$title\">{$this->word}</span>";
	}
}
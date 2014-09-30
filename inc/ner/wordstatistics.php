<?php
/**
* parent class for word processing
* this will be the parent of Maximum entropy and CRF methods
*/
abstract class NER_WordStatistics
{

	public $word = '';
	public $statistics = array(
			O => 0.25,
			PERS => 0.25,
			LOC => 0.25,
			ORG => 0.25
		);

	function __construct($word)
	{
		$this->word = $word;
	}


	public function get_type()
	{
		asort( $this->statistics );
		$keys = array_keys($this->statistics);
		$old_key = $keys[0];

		arsort( $this->statistics );
		$keys = array_keys($this->statistics);
		if($old_key==$keys[0])
		{
			return O;
		}else{
			return $keys[0];
		}
	}

	public function get_word()
	{
		return $this->word;
	}

	abstract public function process(NER_Text $context);
	
	public function render()
	{
		$data_type = Config::entity_val2entity_type($this->get_type());
		$title = "Type: $data_type, <br> O:{$this->statistics[O]}, PERS:{$this->statistics[PERS]}, LOC:{$this->statistics[LOC]}, ORG:{$this->statistics[ORG]}";
		return "<span title=\"$title\">{$this->word}</span>";
	}
}
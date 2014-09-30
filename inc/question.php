<?php
/**
* Question analysis module main class
*/
class Question
{
	
	public $question = null;
	public $asking_about = array(0);

	function __construct( NER $question)
	{
		$this->question = $question;
		$this->classify();
	}

	private function classify()
	{
		$q_text = $this->question->text->get_text();
		$this->asking_about = array();

		foreach( Config::$question_starters as $class => $tools )
		{
			foreach( $tools as $tool )
			{
				if( strpos( $q_text, $tool)===0 )
				{
					array_push( $this->asking_about, $class);
				}
			}
		}

		$this->asking_about = array_unique($this->asking_about);

	}



}
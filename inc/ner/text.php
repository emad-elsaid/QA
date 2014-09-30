<?php
/**
* stream of text for ENR
*/
class NER_Text
{
	public $text = array();

	function __construct($text)
	{

		$words = explode(' ', $text);
		foreach ($words as $word ) {
			array_push( $this->text, new Config::$processing_method($word) );
		}

	}

	public function process()
	{
		foreach ($this->text as $word) {
			$word->process($this);
		}
	}

	public function render()
	{
		$words = array();
		foreach ($this->text as $text) {
			array_push($words, $text->render());
		}
		return implode(' ', $words);
	}

	public function get_text()
	{
		$words = array();
		foreach ($this->text as $text) {
			array_push($words, $text->get_word());
		}
		return implode(' ', $words);
	}
}
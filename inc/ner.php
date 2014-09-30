<?php
/**
* name entity recognition class
*/

class NER
{
	public $text = null;
	public $text_without_stopwords = null;

	public function __construct( $text ){

		$prepared_text = Normalizer::prepare( $text );

		$this->text = new NER_Text($prepared_text);
		$this->text->process();

		$this->text_without_stopwords = new NER_Text( Normalizer::remove_stopwords($prepared_text) );
		$this->text_without_stopwords->process();
		
	}

	public function get_entity($entity)
	{

		$entities = array();
		$last = -1;
		foreach ($this->text_without_stopwords->text as $word) {
			$type = $word->get_type();
			if( $type==$entity ){
				if( $type!=$last ){
					$entities[] = $word->word;
				}else{
					$entities[count($entities)-1] = $entities[count($entities)-1].' '.$word->word;
				}

			}

			$last = $type;
		}

		return $entities;
	}
}
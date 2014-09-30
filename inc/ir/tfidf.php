<?php
/**
* Vector space model algorithm implementation
* http://en.wikipedia.org/wiki/Vector_Space_Model
*/
class IR_TFIDF extends IR
{

	public $vectors;
	public $tfidf;
	public $passages;
	public $question;

	function __construct(Passages $passages, Question $question)
	{

		$this->passages = $passages;
		$this->question = $question;
		// TF calculating
		foreach ($passages->paragraphs as $k => $paragraph) {
			$p = Normalizer::remove_stopwords($paragraph);
			$words = explode(' ', $p);
			$this->vectors[$k] = array();
			foreach ($words as $word) {
				if(array_key_exists($word, $this->vectors[$k])){
					$this->vectors[$k][$word] += 1;
				}else{
					$this->vectors[$k][$word] = 1;
				}
			}
		}

		$question_text = $question->question->text->get_text();
		$q = Normalizer::remove_stopwords($question_text);
		$words = explode(' ', $q);
		$this->vectors['idf'] = array();

		// IDF calculating
		foreach ($words as $word) {
			$idf_denominator = 0;
			foreach ($this->vectors as $vector) {
				if(array_key_exists($word, $vector)){
					$idf_denominator++;
				}
			}
			$this->vectors['idf'][$word] = log(count($this->passages->paragraphs)/(1+$idf_denominator));
		}

		$this->tfidf = array();
		foreach ($passages->paragraphs as $k => $paragraph) {
			$tfidf_p = 1;
			foreach ($words as $word) {
				if(array_key_exists($word, $this->vectors[$k])){
					$tfidf_p += $this->vectors[$k][$word] * $this->vectors['idf'][$word];
				}
			}
			$this->tfidf[$k] = $tfidf_p;
		}

		arsort($this->tfidf);

	}

	
	/*
	get most relevant paragraghs in the passages set;
	*/
	public function get_top($number)
	{
		return array_slice(array_keys($this->tfidf), 0, $number);
	}

	/*
	get most common entity from the paragraphs
	which belongs to class $entity_class
	*/
	public function extract($entity_class)
	{
		$top = $this->get_top(Config::$top_IR_count);
		$entities = array();

		$query_ner = new NER($this->question->question->text->get_text());
		$query_entities = $query_ner->get_entity($entity_class);

		foreach ($top as $key) {
			$paragraphs = $this->passages->paragraphs[$key];
			$ner = new NER($paragraphs);

			// get entities
			$p_entities = $ner->get_entity($entity_class);
			foreach ($p_entities as $entity) {
				if(!(in_array($entity,$query_entities) or strpos($this->question->question->text->get_text(), $entity)>0)){ // don't add entities in query

					if(array_key_exists($entity, $entities)){
						$entities[$entity] += 1;
					}
					else
					{
						$entities[$entity] = 1;
					}

				}
			}

		}

		arsort($entities);
		$keys = array_keys($entities);
		if( count($keys)>0 )
			return $keys[0];
		else
			return null;
	}


	/*
	like extract but query for multiple classes and return array of answers
	*/
	public function extract_arr($entities)
	{
		$answers = array();

		foreach ($entities as $entity) {
			$answers[] = $this->extract($entity);
		}

		return $answers;
	}


}
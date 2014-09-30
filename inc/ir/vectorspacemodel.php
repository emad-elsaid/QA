<?php
/**
* Vector space model algorithm implementation
* http://en.wikipedia.org/wiki/Vector_Space_Model
*/
class IR_VectorSpaceModel extends IR
{

	public $vectors;
	public $eds;
	public $passages;

	function __construct(Passages $passages, Question $question)
	{

		// counting words

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
		$this->vectors['q'] = array();
		foreach ($words as $word) {
			if(array_key_exists($word, $this->vectors['q'])){
				$this->vectors['q'][$word] += 1;
			}else{
				$this->vectors['q'][$word] = 1;
			}
		}

		// normalize
		//  = ( term 1 frequency ) / sqrt [ ( term 1 freq )2 + ( term 2 freq )2 +......+ ( term n freq )2 ]
		foreach ($this->vectors as $key => $vector) {
			$v = $vector;
			foreach ($v as $word => $freq) {
				$n_sum = 0; 
				foreach ($v as $sibling => $sib_freq) {
					$n_sum += $sib_freq*$sib_freq;
				}
				$v[$word] = $freq / sqrt($n_sum);
			}
			$this->vectors[$key] = $v;
		}

		// Euclidean distances
		$eds = array();

		foreach ($this->vectors as $key => $vector) {
			if( $key=='q')
				continue;

			$q = $this->vectors['q'];
			$sums = 0;
			foreach ($vector as $word => $freq) {
				if(array_key_exists($word, $q)){
					$sums += ($q[$word] - $freq) * ($q[$word] - $freq);
				}else{
					$sums += (0 - $freq) * (0 - $freq);
				}
			}
			$eds[$key] = sqrt($sums);
		}

		asort($eds);
		$this->eds = $eds;
		$this->passages = $passages;

	}

	
	/*
	get most relevant paragraghs in the passages set;
	*/
	public function get_top($number)
	{
		return array_slice(array_keys($this->eds), 0, $number);
	}

	/*
	get most common entity from the paragraphs
	which belongs to class $entity_class
	*/
	public function extract($entity_class)
	{
		$top = $this->get_top(Config::$top_IR_count);
		$entities = array();
		foreach ($top as $key) {
			$paragraphs = $this->passages->paragraphs[$key];
			$ner = new NER($paragraphs);

			// get entities
			$p_entities = $ner->get_entity($entity_class);
			foreach ($p_entities as $entity) {
				if(array_key_exists($entity, $entities)){
					$entities[$entity] += 1;
				}
				else
				{
					$entities[$entity] = 1;
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
<?php
/**
* IR class
*/
Abstract class IR
{
	abstract function __construct(Passages $passages, Question $question);
	
	/*
	get most common entity from the paragraphs
	which belongs to class $entity_class
	*/
	abstract public function extract($entity_class);

	/*
	like extract but query for multiple classes and return array of answers
	*/
	abstract public function extract_arr($entities);
}
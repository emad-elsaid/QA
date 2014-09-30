<?php
/**
* configuration class, it'll contain all constants
* and configurationsneeded as static member and static methods
*/
define('DEBUG', false);
set_time_limit(0);
ini_set("memory_limit","1024M");

// entities types
define('PERS', 0); // proper name of person
define('LOC', 1); // location name
define('ORG', 2); // organization name
define('O', 3); // not a proper name

class Config
{

	public static $array_of_types = array(PERS, LOC, ORG, O);
	// word processing class available :
	// NER_Maxent
	// NER_CRF
	// NER_SVM --> not working 
	public static $processing_method = 'NER_Maxent';

	// class of passage rating
	// IR_VectorSpaceModel
	// IR_TFIDF
	Public static $IR_Class = 'IR_TFIDF';

	public static $stemmer_path = 'java bin/ArabicStemmerCode/ArabicStemmer';
	public static $tmp = 'tmp';

	public static $stopwords = null;
	public static $honors = array('رئيس', 'سيد','ملك','امير','الاستاذ','الحاج', 'ابي', 'ابو', 'ام');
	public static $person_separator = array('بن','ابن');
	public static $place_preceedor = array('في', 'الي', 'من', 'جزيره', 'جبل', 'ميدان', 'بحر');
	public static $org_preceedor = array('صحيفه' , 'منظمه' , 'حزب' , 'شركه' , 'وكاله' , 'اتحاد' , 'معرض');
	public static $corpus_files = array(
					'data/ANERCorp.txt',
					'data/corpus/Atom.txt',
					'data/corpus/Crusades.txt',
					'data/corpus/Ibn_Tolun_Mosque.txt',
					'data/corpus/Islamic_History.txt',
					'data/corpus/Periodic_Table.txt',
					'data/corpus/Razi.txt',              
					'data/corpus/Solaris.txt',
					'data/corpus/Christiano_Ronaldo.txt',  
					'data/corpus/Damascus.txt',      
					'data/corpus/Imam_Hussein_Shrine.txt',  
					'data/corpus/Light.txt',            
					'data/corpus/Physics.txt',                 
					'data/corpus/Real_Madrid.txt',       
					'data/corpus/Summer_Olympics2004.txt',
					'data/corpus/Computer_Software.txt',   
					'data/corpus/Enrico_Fermi.txt',  
					'data/corpus/Internet.txt',             
					'data/corpus/Linux.txt',            
					'data/corpus/Portugal_football_team.txt',  
					'data/corpus/Richard_Stallman.txt',  
					'data/corpus/Ummaya_Mosque.txt',
					'data/corpus/Computer.txt',            
					'data/corpus/Football.txt',      
					'data/corpus/Islamic_Golden_Age.txt',   
					'data/corpus/Nuclear_Power.txt',    
					'data/corpus/Raul_Gonzales.txt',           
					'data/corpus/Soccer_Worldcup.txt',   
					'data/corpus/X_window_system.txt'
				);
	public static $corpus_pers = array();
	public static $corpus_loc = array();
	public static $corpus_org = array();
	public static $corpus_o = array();

	public static $paragraphs_total_urls = 10;
	// minimum paragraph text when extracting pharagraphs from webpages
	public static $paragraphs_minimum_length = 300; // characters
	// ignore character larger than 2000 to prevent getting 
	// pages text
	public static $paragraphs_maximum_length = 2000; // characters

	public static $ner_features_classes = array(
		/*
		the NER_Features_*_Corpus are implementing the point (iii) in 
		Arabic-named-entity.pdf : page 3
		*/
		PERS => array(
			'NER_Features_Pers_Nameseparator' => 1,
			'NER_Features_Pers_Corpus' => 1
			),
		LOC => array(
			'NER_Features_Loc_Corpus' => 1
			),
		ORG => array(
			'NER_Features_Org_Corpus' => 1
			),
		O => array(
			'NER_Features_O_Tarqeem' => 100, // this is really really important rule
			'NER_Features_O_Corpus' => 2
			)
		);

	public static $ner_tfunctions_classes = array(

		PERS => array(
			'NER_Tfunctions_Pers_Honorship' => 2
			),
		LOC => array(
			'NER_Tfunctions_Loc_Prefix' => 2,
			),
		ORG => array(
			'NER_Tfunctions_Org_Prefix' => 10,
			),
		O => array(
			
			)
		);

	/*
	Questioning tools, we could classify what teh user 
	is asking about if we know what question starter is aiming to.
	*/
	public static $question_starters = array(
		PERS => array(
			'من'
			),
		LOC => array(
			'اين',
			'ما',
			'الي',
			'في'
			),
		ORG => array(
			'ما'
			),
		O => array(
			'كيف',
			'هل',
			'متي',
			'كم',
			'ماذا',
			'ما'
			)
		);

	public static $top_IR_count = 100;

	public static $trainer_files_path = 'data/training-documents/bbc-arabic-utf8';
	public static $trainer_write_to = 'data/training-statistics';


	function __construct()
	{
		$file = file_get_contents('data/stopwords_list.txt');
		self::$stopwords = explode("\n", $file);

		foreach( self::$corpus_files as $filename)
		{
			self::load_corpus_file($filename);
		}

		self::load_gazateer('data/AlignedLocGazetteer.txt', self::$corpus_loc);
		self::load_gazateer('data/AlignedOrgGazetteer.txt', self::$corpus_org);
		self::load_gazateer('data/AlignedPersGazetteer.txt', self::$corpus_pers);
	}

	protected static function load_corpus_file($file_name)
	{
		// loading the ANER corpus
		$corpus = file_get_contents($file_name);
		$lines = explode("\n", $corpus);
		foreach( $lines as $line )
		{
			$parts = explode(" ", $line);
			if( count($parts)<2 )
				continue;
			
			switch ($parts[1]) {
				case 'B-LOC':
				case 'I-LOC':
					array_push(self::$corpus_loc, Normalizer::prepare($parts[0]));
					break;
				case 'B-PERS':
				case 'I-PERS':
				case 'B-PER':
				case 'I-PER':
					array_push(self::$corpus_pers, Normalizer::prepare($parts[0]));
					break;
				case 'B-ORG':
				case 'I-ORG':
					array_push(self::$corpus_org, Normalizer::prepare($parts[0]));
					break;
				default:
					array_push(self::$corpus_o, Normalizer::prepare($parts[0]));
					break;
			}
		}
	}

	public static function print_corpora_statistics()
	{
		$loc = count(self::$corpus_loc);
		$pers = count(self::$corpus_pers);
		$org = count(self::$corpus_org);
		$o = count(self::$corpus_o);

		Logger::box("احصائيات القاموس :", "الأماكن : $loc , المنظمات : $org , الأشخاص : $pers , غير الأسماء : $o");
	}

	protected static function load_gazateer($from_file, $to_array)
	{
		$file = file_get_contents($from_file);
		$lines = explode("\n", $file);
		$ignore_lines_starts_with = array('*','-','=','.');
		foreach( $lines as $line )
		{
			if( strlen($line)==0 or in_array( $line[0], $ignore_lines_starts_with) )
				continue;
			array_push($to_array, Normalizer::prepare($line) );
		}
		$to_array = array_unique($to_array);
	} 
	
	// convert entity constant to string 
	// useful for rendering entity type
	public static function entity_val2entity_type($value)
	{
		switch ($value) {
			case PERS:
				return 'PERS';
				break;
			
			case LOC:
				return 'LOC';
				break;
			case ORG:
				return 'ORG';
				break;
			case O:
				return 'O';
				break;
		}
	}

}
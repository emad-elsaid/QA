<?php
/**
* Get passages from the web for a query
*/
class Passages
{
	
	public $query;
	public $paragraphs = array();
	function __construct(NER_Text $query)
	{
		$this->query = $query;
		// load SimpleHTMLDom
		$this->fetch_from_google();
	}

	protected function fetch_from_google()
	{
		new SimpleHTMLDom();

		$passages = array();
		$start = -1;
		while( $start<count($passages) and count($passages)<Config::$paragraphs_total_urls )
		{

			$start = count($passages);
			$html = file_get_html('http://www.google.com/search?hl=en&output=search&q=' . urlencode($this->query->get_text()) .'&start='.$start );
			foreach($html->find('li[class=g]') as $element) {
			    foreach($element->find('h3[class=r]') as $h3) 
			    {
			        foreach( $h3->find('a') as $a)
			        {
			        	
			        	$url = $a->href;
			        	$q = strpos($url, 'q=')+2;
			        	$url = substr($url, $q);
			        	$and = strpos($url, '&');
			        	$url = substr($url, 0, $and);

			        	$passages[] =  $this->get_paragraphs( urldecode($url) );
			        }
			    }
			}
		}

	}

	protected function get_paragraphs($url)
	{
		new SimpleHTMLDom(); // load SimpleHTMLDom library
		$html = @file_get_html( $url ); // get the webpage
		
		if( ! is_object($html) )
			return;

		foreach($html->find('p') as $element) { // get each paragraph tag 

			$p = Normalizer::prepare($element->plaintext);
			if( strlen($p) >= Config::$paragraphs_minimum_length
				and strlen($p) <= Config::$paragraphs_maximum_length )
			{
		    	$this->paragraphs[] = $p ; // add paragraph if it meets the requirements
			}

		} // end of iterating on the paragraphs tag
	}



}
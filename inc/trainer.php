<?php
/**
* training class for entities prefix/suffix
*/
class Trainer
{
	
	public function train()
	{
		$files = $this->enumerate_docs();
		
		$statistics = array();

		// preparing statistics
		foreach( Config::$array_of_types as $type){
			$statistics[$type] = array(
										"before"=>array(),
										"after"=>array()
									  );
		}

		// open each file and process it
		foreach( $files as $k => $file ){
			$text = file_get_contents($file);
			echo "i read the file $file\n";
			$ner = new NER($text);
			$text= $ner->text->get_text();
			echo "i loaded the file $file\n";

			// get statistics for each entity type
			foreach (Config::$array_of_types as $type) {
				if($type==O)
					continue;

				echo "trying to get entities for ".Config::entity_val2entity_type($type)."\n";
				// get all entities from certain type
				$entities = $ner->get_entity($type);

				// get the previous and next word for each entity
				echo "got (".count($entities)."), iterating on each entity \n";
				foreach ($entities as $entity) {

					$exploded = explode($entity, $text);

					foreach($exploded as $exp){
						$e = explode(' ', trim($exp));
						$word_before = $e[count($e)-1];
						if(!array_key_exists($word_before, $statistics[$type]['before']))
							$statistics[$type]['before'][$word_before] = 0;
						$statistics[$type]['before'][$word_before]++;


						$word_after = $e[0];
						if(!array_key_exists($word_after, $statistics[$type]['after']))
							$statistics[$type]['after'][$word_after] = 0;

						$statistics[$type]['after'][$word_after]++;							
					}

				}

			}
			echo "finished ".($k+1)." of ".count($files)." = ". (($k+1)/count($files))."%\n";
			$this->print_statistics($statistics);
			$this->write_statistics($statistics);
		}

	}

	protected function print_statistics($statistics)
	{
		foreach (Config::$array_of_types as $type) {
			echo Config::entity_val2entity_type($type). " Before :".count($statistics[$type]['before'])." After :".count($statistics[$type]['before'])."\n";
		}

		echo "\n";
	}

	

	public function enumerate_docs($dir=null)
	{
		if( $dir==null ){
			$dir = Config::$trainer_files_path;
		}

		$files = array();
		$dir_o = opendir($dir);
		while($file=readdir($dir_o)){
			if(!in_array($file, array('.','..'))){

				$file_path = $dir.'/'.$file;
				if(is_file($file_path)){
					$files[] = $file_path;
				}else{
					$files = array_merge($this->enumerate_docs($file_path),$files);
				}

			}
		}

		closedir($dir_o);
		return $files;

	}

	public function write_statistics($statistics)
	{
		$path = Config::$trainer_write_to.'/';
		foreach($statistics as $type => $stats)
		{	
			$text = "";
			foreach(array('before','after') as $pos)
			{
				foreach($stats[$pos] as $word => $count)
				{
					$text .= "$word\t$pos\t$count\n";
				}
			}
			file_put_contents($path.Config::entity_val2entity_type($type).'.txt', $text);
		}
	}
}
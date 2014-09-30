<?php
/**
* JIRS class
*/
class JIRS
{
	
	public $create_doc_list;
	public $indexing;
	public $result;
	public $return_value;

	function __construct(Passages $passages, Question $question)
	{
		$this->clean();
		foreach ($passages->paragraphs as $k => $paragraph) {
			file_put_contents(Config::$tmp.'/'.$k.'.txt', $paragraph);
		}

		$question_text = $question->question->text->get_text();
		file_put_contents(
						Config::$tmp.'/'.Config::$JIRS_question_file,
						$question_text."\n"
						);

		$this->create_doc_list = `bin/jirs/jpm.sh bin/jirs/config/runs.xml CreateDocList -collection tmpcol -language arabic -folder tmp`;
		$this->indexing = `bin/jirs/jpm.sh bin/jirs/config/runs.xml Indexing -collection tmpcol -language arabic -folder tmp`;

		$command = "echo \"$question_text\" | bin/jirs/jpm.sh bin/jirs/config/runs.xml SearchPassages -client/server no -repeat no -collection tmpcol -language arabic";
		$descriptorspec = array(
			0 => array("file", Config::$tmp.'/'.Config::$JIRS_question_file, "r"),  // stdin is a pipe that the child will read from
			1 => array("file", Config::$tmp.'/'.Config::$JIRS_output_file, "w") , // stdout is a pipe that the child will write to
		);

		$cwd = '.';

		$process = proc_open($command, $descriptorspec, $pipes);

		if (is_resource($process)) {
		    
		    $this->result = file_get_contents(Config::$tmp.'/'.Config::$JIRS_output_file);

		    // It is important that you close any pipes before calling
		    // proc_close in order to avoid a deadlock
		    $this->return_value = proc_close($process);
		}

	}

	protected function clean()
	{
		$dir = opendir(Config::$tmp);
		while( $file=readdir($dir) )
		{
			if(!in_array($file, array('.','..')) )
			{
				unlink(Config::$tmp.'/'.$file);
			}
		}
	}

}
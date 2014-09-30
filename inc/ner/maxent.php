<?php
/**
* statistics of max entropy for word
*/
class NER_Maxent extends  NER_WordStatistics
{
	
	public function process(NER_Text $context)
	{

		// apply the equation in ANER_MAX_ENTROPY.pdf : page 147
		
		// p(c|x) = (1/Z(x)) * exp( sum[i]( weight[i]*f[i](x,c) ) )
		// Z(x) = sum[c`]( exp( sum[i]( weight[i]*f[i](x,c`) ) ) )
		$classes = array_keys($this->statistics);
		foreach ($classes as $class ) {

			Logger::log( "Testing class : $class<br>" );
			$z_of_x = 0; // sum[c`]( exp( sum[i]( weight[i]*f[i](x,c`) ) ) )
			foreach ($classes as $class_dash) {
				
				$z_of_x_class_sum = 0; // sum[i]( weight[i]*f[i](x,c`) )
				foreach (Config::$ner_features_classes[$class_dash] as $feature => $weight) {
					$feature_result = $feature::check($this,$context);
					$z_of_x_class_sum += $weight*$feature_result;
					Logger::log( "$feature($this->word) = $feature_result<br>" );
				}

				$z_of_x += exp($z_of_x_class_sum);
			}

			Logger::log( "The main features:<br>" );
			$p_of_c_given_x_sum = 0; // sum[i]( weight[i]*f[i](x,c) ) 
			foreach (Config::$ner_features_classes[$class] as $feature => $weight) {
				$feature_result = $feature::check($this,$context);
				$p_of_c_given_x_sum += $weight*$feature_result;
				Logger::log( "$feature($this->word) = $feature_result<br>" );
			}

			$p_of_c_given_x = (1/$z_of_x) * exp($p_of_c_given_x_sum); // (1/Z(x)) * exp( sum[i]( weight[i]*f[i](x,c) ) )
			$this->statistics[$class] = $p_of_c_given_x;

		} // iterate for each class

	}

	
}
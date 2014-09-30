<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>QA System</title>
	<link rel="stylesheet" href="css/style.css">
	<link rel="stylesheet" href="css/tipsy.css" />
	<script type="text/javascript" src="js/jquery.js"></script>
	<script type="text/javascript" src="js/prefixfree.js"></script>
	<script type="text/javascript" src='js/jquery.tipsy.js'></script>
	<script type="text/javascript" src="js/script.js"></script>
</head>
<body>
	<div class="header">
		<div class=" container_12">
			<div class="grid_12 logo">
				<img src="css/logo.png" />
			</div>
			<div class="grid_12">

				<form class="ask" action="index.php" method="post">
					<?php 
					if( isset($_POST['q']) ){
						$question = $_POST['q'];
					}else{
						$question = 'من هو عميد الأدب العربى ؟';
					}
					?>
					<input name="q" class="q" placeholder="أسأل سؤالك هنا" value="<?php echo $question ?>">
					<button type="submit" name="submit" class="submit" >اسأل!</button>
				</form>
			</div>
		</div>
	</div>
	<div class="container_12">
		<div class="grid_12">
			<div class="results">
				<?php include 'bootstrap.php' ?>
			</div>
		</div>
	</div>
	<footer>
		<div class="container_12">
			<div class="grid_12">
				تم إنشاء هذا المشروع لأغراض بحثية <br/>
				يمكنك تشغيل سكريبت تدريب لإستخلاص مجموعة من الإحصائيات عن الكلمات التى تسبق و  تلى
				الكيانات فى النصوص من <a href="trian.php">هنا</a>
			</div>
		</div>
	</footer>
</body>
</html>
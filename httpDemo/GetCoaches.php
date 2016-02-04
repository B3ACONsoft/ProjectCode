<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Get Coaches PHP</title>
</head>

<body>
<!--comment-->
<p>Currently the database has one table named "coaches", with five fields: 
"id", "firstname", "lastname", "phone", "email"</p>
<p>There are three records of information<br/>
<table border="1">
	<tr><th>id</th><th>firstname</th><th>lastname</th><th>phone</th><th>email</th></tr>
    <tr><td>1</td><td>fname1</td><td>lname1</td><td>1234567890</td><td>lname1@gmail.com</td></tr>
    <tr><td>2</td><td>fname2</td><td>lname2</td><td>2345678901</td><td>lname2@gmail.com</td></tr>
    <tr><td>3</td><td>fname3</td><td>lname3</td><td>3456789012</td><td>lname3@gmail.com</td></tr>
</table>
</p>
</body>
</html>
<?php
echo "<h2>Get Coaches PHP</h2>";
echo "<p>PHP version " . phpversion() . "</p>";

$dsn = 'mysql:host=mysql13.000webhost.com;dbname=a7033823_survey';
$username = 'a7033823_scott';
$password = 'pass12';
try {
	$db = new PDO($dsn, $username, $password);
	echo "<p>You are connected to the database.</p>";
}catch(PDOException $e){
	$error_message = $e->getMessage();
	echo "<p>An error occurred while connecting to the database:
	$error_message</p>";
}

$query = 'SELECT * FROM coaches';

$allCoaches = $db->query($query);
echo"<p>Current state of the database!!!.</p>";
echo'<table border="1">';
foreach($allCoaches as $aCoach){ ?>
	<tr>
		<td><?php echo $aCoach['id']; ?></td>
    	<td><?php echo $aCoach['firstname']; ?></td>
		<td><?php echo $aCoach['lastname']; ?></td>
		<td><?php echo $aCoach['phone']; ?></td>
		<td><?php echo $aCoach['email']; ?></td>
    </tr>
<?php }; 
echo"</table>";
/*	//mysql_connect(localhost, mysql_user, mysql_password)
	$DBConnect = mysql_connect("mysql13.000webhost.com","a7033823_scott","pass12");
	if ($DBConnect === FALSE)
		echo "<p>Connection error: " . mysql_error() . "</p>\n";
	else {
		$DBName = "a7033823_survey";
		if (mysql_select_db($DBName, $DBConnect) === FALSE)
		{
			echo "<p>Could not select the \"$DBName\" " . "database: " . mysql_error($DBConnect) . "</p>\n";
		}else{
			$SQLString = "SELECT * FROM coaches";
			$QueryResult = mysql_query($SQLString, $DBConnect);
			$Row = mysql_fetch_row($QueryResult);

			mysql_free_result($QueryResult);

			mysql_close($DBConnect);
			//echo "<p>Connection closed</p>\n";
		}
	}
*/

?>
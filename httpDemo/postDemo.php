<?php
/*
	This just shows how you get data up to the server ussing a POST
	request.
	There is no data validation or security checking etc etc...
    
    Dave Johnson 2/3/2016
	
*/

$db;

//scott's DB connection stuff
function connectToDB() {
    echo "connecting to DB\r\n\r\n";
    global $db;
        //create scotts database objects

    $dsn = 'mysql:host=mysql13.000webhost.com;dbname=a7033823_survey';
	$username = 'a7033823_scott';
	$password = 'pass12';
	try {
		$db = new PDO($dsn, $username, $password);
		echo "You are connected to the database.\r\n\r\n";
	}catch(PDOException $e){
		$error_message = $e->getMessage();
		echo "An error occurred while connecting to the database:
		$error_message\r\n\r\n";
	}
    
}

function closeDB() {
    global $db;
    $db->close();
}

/*
    Create prepared statement,
    bind the parameters to the statement,
    execute query,
    echo results
*/
function insertOperation($params) {
    echo "performing insert opperation\r\n\r\n";
    global $db;
    //query string
    $stmt = "INSERT INTO coaches (id, firstname, lastname, phone, email) VALUES (:id, :firstname, :lastname, :phone, :email)";
    if($db != NULL) {
        $query = $db->prepare($stmt);
    
        //bind the params
        $query->bindParam(':id', $params['id']);
        $query->bindParam(':firstname', $params['firstname']);
        $query->bindParam(':lastname', $params['lastname']);
        $query->bindParam(':phone', $params['phone']);
        $query->bindParam(':email', $params['email']);
	
        try {
            
            if($query->execute()) {
                echo 'SUCCESS!!!\nInserted Values:\n'.$params['id'].', '.$params['firstname'].', '.$params['lastname'].', '.$params['phone'].', '.$params['email'].'\r\n\r\n'; 
            } else {
                echo "\nPDOStatement::errorInfo():\n";
                $arr = $query->errorInfo();
                print_r($arr);
            }
            
        } catch (PDOException $e) {
            $error_message = $e->getMessage();
		    echo "An error occurred while connecting to the database:
		    $error_message\r\n\r\n";
        }
        
    }

}

/*
    Does a select all on Scott's db and echos the results
*/
function selectOperation($params) {
    echo "performing select operation\r\n\r\n";
    global $db;
    
    $query = 'SELECT * FROM coaches';

    $allCoaches = $db->query($query);
    
    foreach($allCoaches as $aCoach) {
        echo 'Coach '.$aCoach['id'].":\r\n  ".$aCoach['firstname']."\r\n  ".$aCoach['lastname']."\r\n  ".$aCoach['phone']."\r\n  ".$aCoach['email']."\r\n\r\n";
    }
}

/*
    Checks for data in the POST array.
    If it has data it switches on the two possible command types.
    
*/
if($_POST != NULL) {
    
    connectToDB();
    
    switch($_POST['command'])
    {
        case 'INSERT':
            insertOperation($_POST);        //pass the $_POST array to the insertOperation
            break;
            
        case 'SELECT':
            $params = array($_POST['options']);
            selectOperation($params);      //perform the select operation,
                                           // I didn't really need to pass the $_POST array here...
        break;
    }
	
    //closeDB();
    
} else {
    echo("you didn't post anything...\r\n\r\n");
}

?>
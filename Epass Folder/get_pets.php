<?php 

header("Content-Type: application/json; charset=UTF-8");

require_once 'connect.php';

$query = "SELECT * FROM pets ORDER BY id DESC ";
$result = mysqli_query($conn, $query);
$response = array();

$server_name = $_SERVER['SERVER_ADDR'];

while( $row = mysqli_fetch_assoc($result) ){

    array_push($response, 
    array(
        'id'        =>$row['id'], 
        'name'      =>$row['name'], 
        'species'   =>$row['species'],
        'breed'     =>$row['breed'],
        'birth'     =>$row['birth'],
        'picture'   =>"http://$server_name" . $row['picture'],
        'gapo'      =>$row['gapo'],) 
    );
}

echo json_encode($response);

mysqli_close($conn);

?>
<?php 

header("Content-Type: application/json; charset=UTF-8");

require_once 'connect.php';

$key = $_POST['key'];

if ( $key == "update" ){

    $id         = $_POST['id'];
    $name       = $_POST['name'];
    $species    = $_POST['species'];
    $breed      = $_POST['breed'];
    $birth      = $_POST['birth'];
    $picture    = $_POST['picture'];
    $gapo    = $_POST['gapo'];


    //$birth =  date('Y-m-d', strtotime($birth));

    $query = "UPDATE pets SET 
    name='$name', 
    species='$species', 
    breed='$breed',
    birth='$birth',
    gapo='$gapo'

    WHERE id='$id' ";

        if ( mysqli_query($conn, $query) ){

            if ($picture == null) {

                $result["value"] = "1";
                $result["message"] = "Success";
    
                echo json_encode($result);
                mysqli_close($conn);

            } else {

                $path = "pets_picture/$id.jpeg";
                $finalPath = "/demo_pets/".$path;

                $insert_picture = "UPDATE pets SET picture='$finalPath' WHERE id='$id' ";
            
                if (mysqli_query($conn, $insert_picture)) {
            
                    if ( file_put_contents( $path, base64_decode($picture) ) ) {
                        
                        $result["value"] = "1";
                        $result["message"] = "Success!";
            
                        echo json_encode($result);
                        mysqli_close($conn);
            
                    } else {
                        
                        $response["value"] = "0";
                        $response["message"] = "Error! ".mysqli_error($conn);
                        echo json_encode($response);

                        mysqli_close($conn);
                    }

                }
            }

        } 
        else {
            $response["value"] = "0";
            $response["message"] = "Error! ".mysqli_error($conn);
            echo json_encode($response);

            mysqli_close($conn);
        }
}

?>
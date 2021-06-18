package ucucite.edu.epass_admin;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by christian alavazo.
 */

public interface AdminApiInterface {

    @POST("get_pets.php")
    Call<List<Admintravellers>> getPersons();

    @FormUrlEncoded
    @POST("add_pet.php")
    Call<Admintravellers> insertPersons(
            @Field("key") String key,
            @Field("name") String name,
            @Field("species") String species,
            @Field("breed") String breed,
            @Field("birth") String birth,
            @Field("picture") String picture,
            @Field("gapo") String gapo,
            @Field("eta") String eta);



    @FormUrlEncoded
    @POST("update_pet.php")
    Call<Admintravellers> updatePersons(
            @Field("key") String key,
            @Field("id") int id,
            @Field("name") String name,
            @Field("species") String species,
            @Field("breed") String breed,
            @Field("birth") String birth,
            @Field("picture") String picture,
            @Field("gapo") String gapo,
            @Field("eta") String eta);


    @FormUrlEncoded
    @POST("delete_pet.php")
    Call<Admintravellers> deletePersons(
            @Field("key") String key,
            @Field("id") int id,
            @Field("picture") String picture);

    @FormUrlEncoded
    @POST("update_love.php")
    Call<Admintravellers> updateLove(
            @Field("key") String key,
            @Field("id") int id,
            @Field("love") boolean love);

}

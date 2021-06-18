package ucucite.edu.epass_admin;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by christian alavazo.
 */

public interface UsersApiInterface {

    @POST("get_users.php")
    Call<List<Userstravellers>> getPersons();

    @FormUrlEncoded
    @POST("add_users.php")
    Call<Userstravellers> insertPersons(
            @Field("key") String key,
            @Field("name") String name,
            @Field("species") String species,
            @Field("breed") String breed,
            @Field("birth") String birth,
            @Field("picture") String picture);

    @FormUrlEncoded
    @POST("update_users.php")
    Call<Userstravellers> updatePersons(
            @Field("key") String key,
            @Field("id") int id,
            @Field("name") String name,
            @Field("species") String species,
            @Field("breed") String breed,
            @Field("birth") String birth,
            @Field("picture") String picture);


    @FormUrlEncoded
    @POST("delete_users.php")
    Call<Userstravellers> deletePersons(
            @Field("key") String key,
            @Field("id") int id,
            @Field("picture") String picture);

    @FormUrlEncoded
    @POST("update_love.php")
    Call<Userstravellers> updateLove(
            @Field("key") String key,
            @Field("id") int id,
            @Field("love") boolean love);

}

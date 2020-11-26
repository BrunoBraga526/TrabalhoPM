package ipvc.estg.room.API_Login

import ipvc.estg.room.Output_Login
import retrofit2.Call
import retrofit2.http.*

interface PostLogin {

    @FormUrlEncoded
    @POST("myslim/api/login/post")
    fun postTest(@Field("utilizador") username: String, @Field("palavrapasse") password: String?): Call<Output_Login>
}
package com.androidcourse.youtubefinal.api;

import com.androidcourse.youtubefinal.model.Resultado;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YoutubeService {


    //https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&maxResults=20&key=AIzaSyAgudARUNy5DRSo2QjW8iUMKH-qdBP1MDQ&channelID=UCVHFbqXqoYvEWM1Ddxl0QDg&q=desenvolvimento


    @GET("search")
    Call<Resultado>recuoerarVideos(@Query("part") String part, @Query("order") String order, @Query("maxResults") String maxResults, @Query("key") String key, @Query("channelId") String channelId, @Query("q") String q);

}

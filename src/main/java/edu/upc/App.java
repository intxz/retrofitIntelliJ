package edu.upc;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.*;

public class App 
{
    public static final String API_URL = "http://localhost:8080/dsaApp/";//path defined in BASE_URI on API
    //class song where a song will be stored
    public static class Song {
        public final String id;
        public final String title;
        public final String singer;

        public Song(String id, String title, String singer) {
            this.id = id;
            this.title = title;
            this.singer = singer;
        }
    }
    //Interface that will use get method to get the song list. Here use the path in swagger. In this case http://localhost:8080/dsaApp/tracks. Since we have the rest just add tracks
    public interface TracksInterface {
        @GET("tracks")
        Call<List<Song>> songs();

        @GET("tracks/{id}")
        Call<Song> song1(@Path("id") String id);

        @POST("tracks")
        Call<Song> song2(@Body Song song);

        @PUT("tracks")
        Call<Void> song3(@Body Song body);

        @DELETE("tracks/{id}")
        Call<Void> song4(@Path("id") String id);
    }

    public static void main( String[] args ) throws IOException
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        // Create a very simple REST adapter which points the API.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of our tracks API interface.
        TracksInterface tracksInterface = retrofit.create(TracksInterface.class);

        // Create a call instance for looking up Retrofit songs.
        Call<List<Song>> call = tracksInterface.songs();

        // Fetch and print a list of the contributors to the library.
        List<Song> songs = call.execute().body();
        for (Song song : songs) {
            System.out.println("Song ID: "+song.id+"\nTitle: "+ song.title+"\nSinger: " + song.singer+"\n");
        }


        Call<Song> call1 = tracksInterface.song1("T3E4WC79863585");
        Song song = call1.execute().body();
        System.out.println("Song ID: "+song.id+"\nTitle: "+ song.title+"\nSinger: " + song.singer+"\n");



        //Song titi = new Song("titi","titi","titi");
        //Call<Song> call2 = tracksInterface.song2(titi);
        //retrofit2.Response<Song> songResponse = call2.execute();
        //Song song22 = songResponse.body();
        //System.out.println("Song ID: "+song22.id+"\nTitle: "+ song22.title+"\nSinger: " + song22.singer+"\n");


        Song barbacoa = new Song("T3E4WC79863585", "holi", "holi");
        Call<Void> call3 = tracksInterface.song3(barbacoa);
        retrofit2.Response<Void> songResponse1 = call3.execute();
        if(songResponse1.code() == 201) {System.out.println("si");}

        Call<Void> call4 = tracksInterface.song4("titi");
        retrofit2.Response<Void> voidResponse = call4.execute();
        if (voidResponse.code() == 201) System.out.println("sisi");
    }
}

package com.androidcourse.youtubefinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.androidcourse.youtubefinal.activity.PlayerActiviy;
import com.androidcourse.youtubefinal.adapter.AdapterVideo;
import com.androidcourse.youtubefinal.api.YoutubeService;
import com.androidcourse.youtubefinal.helper.RetrofitConfig;
import com.androidcourse.youtubefinal.helper.YoutubeConfig;
import com.androidcourse.youtubefinal.listener.RecyclerItemClickListener;
import com.androidcourse.youtubefinal.model.Item;
import com.androidcourse.youtubefinal.model.Resultado;
import com.androidcourse.youtubefinal.model.Video;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {


    private RecyclerView recyclerVideos;
    private MaterialSearchView searchView;
    private List<Item> videos = new ArrayList<>();
    private  Resultado resultado;
    private AdapterVideo adapterVideo;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerVideos = findViewById(R.id.recyclerVideos);
        searchView = findViewById(R.id.searchView);

        //Configurações iniciais
        retrofit = RetrofitConfig.getRetrofit();



        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Youtube");
        setSupportActionBar(toolbar);


        //Recupera vídeos
        recuperarVideos("");

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recuperarVideos(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
               recuperarVideos("");
            }
        });


    }


    private void recuperarVideos (String pesquisa){

        String q = pesquisa.replaceAll(" ","+");

        YoutubeService youtubeService = retrofit.create(YoutubeService.class);
        youtubeService.recuoerarVideos("snippet","date","20", YoutubeConfig.CHAVE_YOUTUBE_API,YoutubeConfig.CANAL_ID, q )
                .enqueue(new Callback<Resultado>() {
                    @Override
                    public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                        Log.d("resultado","resultado"+ response.toString());
                        if(response.isSuccessful()){
                            Resultado resultado= response.body();
                            if(resultado.items.size()>0){
                            videos = resultado.items;
                            configurarRecyclerView(); }
                            else{
                                Toast.makeText(MainActivity.this, "No found results", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<Resultado> call, Throwable t) {

                    }
                });

    };


    public void  configurarRecyclerView(){
        adapterVideo = new AdapterVideo(videos, this);
        recyclerVideos.setHasFixedSize(true);
        recyclerVideos.setLayoutManager(new LinearLayoutManager(this));
        recyclerVideos.setAdapter(adapterVideo);

        recyclerVideos.addOnItemTouchListener( new RecyclerItemClickListener(this, recyclerVideos, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Item video = videos.get(position);
                String idVideo = video.id.videoId;

                Intent intent = new Intent(MainActivity.this, PlayerActiviy.class);
                intent.putExtra("idVideo", idVideo);
                startActivity(intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.menu_search);
        searchView.setMenuItem(item);
        return true;

    }
}

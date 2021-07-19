package br.com.projetonoticias;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import br.com.projetonoticias.util.Article;
import br.com.projetonoticias.util.ArticleAdapter;
import br.com.projetonoticias.util.ArticlesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    SwipeRefreshLayout refreshLayout;
    private String apiKey = "ae68700d7dad43d0bc90bda8e85caa12";
    private RecyclerView recyclerView;
    private SearchView searchView;
    private Retrofit retrofit;
    private ArticleService service;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.lista);
        refreshLayout = findViewById(R.id.refreshLayout);
        searchView = findViewById(R.id.searchView);
        retrofit = new Retrofit.Builder()
                .baseUrl(ArticleService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ArticleService.class);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscarNoticias();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                buscarNoticias();
                refreshLayout.setRefreshing(false);
            }
        });


    }

    public void buscarNoticias() {
        
        CharSequence perguntaUser = searchView.getQuery();
        service.buscarArtigos(apiKey, perguntaUser.toString(), "pt").enqueue(new Callback<ArticlesResponse>() {
            @Override
            public void onResponse(Call<ArticlesResponse> call, Response<ArticlesResponse> response) {
                if (response.isSuccessful()) {
                    onResult(response.body().getArticles());

                }
            }

            @Override
            public void onFailure(Call<ArticlesResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResult(List<Article> articlesResponses) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(new ArticleAdapter(articlesResponses));
    }
}


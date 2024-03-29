package br.com.projetonoticias.util;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.projetonoticias.R;


public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private List<Article> listaArtigos;

    public ArticleAdapter(List<Article> listaArtigos) {
        this.listaArtigos = listaArtigos;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View minhaView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        ArticleViewHolder viewHolder = new ArticleViewHolder(minhaView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleAdapter.ArticleViewHolder holder, int position) {
        String titulo = listaArtigos.get(position).getTitle();
        holder.getTitulo().setText(titulo);
        String description = listaArtigos.get(position).getDescription();
        holder.getDescription().setText(description);
        String urlToImage = listaArtigos.get(position).getUrlToImage();
        Picasso.get().load(urlToImage).into(holder.getImageNews());
        holder.getShareButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, description);
                i.putExtra(Intent.EXTRA_TEXT, listaArtigos.get(position).getUrl());
                v.getContext().startActivity(Intent.createChooser(i, titulo));

            }
        });
        Source source = listaArtigos.get(position).getSource();
        holder.getImageNews().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(listaArtigos.get(position).getUrl()));
                v.getContext().startActivity(i);


            }
        });
        if (source != null) {
            holder.getSource().setText(source.name);
        }


    }

    @Override
    public int getItemCount() {
        if (listaArtigos == null) {
            return 0;

        } else {
            return listaArtigos.size();
        }
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {

        private final TextView titulo;
        private final ImageView imageNews;
        private final TextView source;
        private final TextView description;
        private final ImageView shareButton;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.newsText);
            imageNews = itemView.findViewById(R.id.newsImage);
            source = itemView.findViewById(R.id.newsSource);
            description = itemView.findViewById(R.id.description);
            shareButton = itemView.findViewById(R.id.shareButton);
        }

        public TextView getTitulo() {
            return titulo;
        }

        public ImageView getImageNews() {
            return imageNews;
        }

        public TextView getSource() {
            return source;
        }

        public TextView getDescription() {
            return description;
        }

        public ImageView getShareButton() {
            return shareButton;
        }
    }

}


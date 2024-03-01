package com.example.newkey;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecommendationBigsizeAdapter extends RecyclerView.Adapter<RecommendationBigsizeAdapter.ViewHolder> {

    private List<RecommendationItemBigsize> recommendationItems;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView titleView;
        public TextView pressView;
        public TextView timeView;

        public CircleImageView circleImageView;

        public ImageView bookmarkBigImageView;

        public Boolean isClicked = Boolean.FALSE;

        public ViewHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.news_image_big);
            titleView = v.findViewById(R.id.news_title_big);
            pressView = v.findViewById(R.id.news_press_big);
            timeView = v.findViewById(R.id.news_time_big);
            circleImageView = v.findViewById(R.id.news_press_image_big);
            bookmarkBigImageView = v.findViewById(R.id.item_bookmark_big);
        }
    }

    public RecommendationBigsizeAdapter(List<RecommendationItemBigsize> recommendationItems) {
        this.recommendationItems = recommendationItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommendation_item_bigsize, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecommendationItemBigsize newsItem = recommendationItems.get(position);
        holder.titleView.setText(newsItem.getTitle());
        holder.pressView.setText(newsItem.getPress());
        holder.timeView.setText(newsItem.getTime());

        new ImageLoadTask(holder.imageView).loadImage(newsItem.getImageUrl());
        new ImageLoadTask(holder.circleImageView).loadImage(newsItem.getCircleImageUrl());

        holder.bookmarkBigImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.isClicked == Boolean.FALSE){
                    holder.isClicked = Boolean.TRUE;
                    holder.bookmarkBigImageView.setImageResource(R.drawable.bookmark_checked);
                }
                else {
                    holder.isClicked = Boolean.FALSE;
                    holder.bookmarkBigImageView.setImageResource(R.drawable.bookmark_unchecked);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return recommendationItems.size();
    }

    private static class ImageLoadTask {
        private ImageView imageView;
        private ExecutorService executorService = Executors.newSingleThreadExecutor();
        private Handler handler = new Handler(Looper.getMainLooper());

        public ImageLoadTask(ImageView imageView) {
            this.imageView = imageView;
        }

        public void loadImage(String url) {
            executorService.execute(() -> {
                final Bitmap bitmap = downloadImage(url);
                handler.post(() -> imageView.setImageBitmap(bitmap));
            });
        }

        private Bitmap downloadImage(String urlDisplay) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(urlDisplay);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }
}
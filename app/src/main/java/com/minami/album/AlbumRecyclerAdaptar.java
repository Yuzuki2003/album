package com.minami.album;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AlbumRecyclerAdaptar extends RecyclerView.Adapter<AlbumRecyclerAdaptar.ViewHolder> {

    private Album albums;

    private Context context;
    private LayoutInflater inflater;

    public AlbumRecyclerAdaptar(Album albums, Context context, LayoutInflater inflater) {
        this.inflater = inflater;
        this.albums = albums;
        this.context = context;
    }

    public void updatePhoto(Uri uri) {
        albums.listPictures.add(uri);
        // Re-load again
        notifyDataSetChanged();
    }

    /**
     * ViewHolderを作ったとき
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.listitem_album_image, parent, false));
    }

    /**
     * ViewHolderと結びついたとき
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Uri uri = albums.listPictures.get(position);
        try {
            InputStream is = context.getContentResolver().openInputStream(uri);
            Bitmap bmp = BitmapFactory.decodeStream(is);
            is.close();
            holder.imageView.setImageBitmap(bmp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * RecyclerViewのアダプターが何個アイテムを持っているのか
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return albums.listPictures.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}

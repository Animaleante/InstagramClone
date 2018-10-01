package com.opet.diogo.instagramclone;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by opet on 01/10/2018.
 */

public class SeeAllAdapter extends ArrayAdapter<Photo> {

    private int resource;
    private List<Photo> photos;

    public SeeAllAdapter(Context context, int resource, List<Photo> objects) {
        super(context, resource, objects);
        this.resource = resource;
        photos = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View currentView, ViewGroup parent) {
        View mView = currentView;

        if(mView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = inflater.inflate(resource, null, false);
        }

        Photo photo = photos.get(position);

        TextView comment = mView.findViewById(R.id.photoComment);
        ImageView image = mView.findViewById(R.id.photoImage);

        if(comment != null) {
            comment.setText(photo.getPhotoComment());
        }

        if(image != null) {
            Picasso.get()
                .load(photo.getPhotoPath())
                //.resize(image.getWidth(),image.getWidth())
                .into(image);
        }

        return mView;
    }
}

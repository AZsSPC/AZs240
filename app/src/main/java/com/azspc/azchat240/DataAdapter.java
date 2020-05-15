package com.azspc.azchat240;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Post> phones;

    DataAdapter(Context context, List<Post> phones) {
        this.phones = phones;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {
        Post post = phones.get(position);
        holder.text.setText(post.getText());
        holder.title.setText(post.getTitle());
        holder.title.setBackgroundColor(post.getColor());
    }

    @Override
    public int getItemCount() {
        return phones.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView text, title;

        ViewHolder(View view) {
            super(view);
            text = view.findViewById(R.id.post_text);
            title = view.findViewById(R.id.post_title);
        }
    }
}
package com.azspc.azchat240.cheat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.azspc.azchat240.R;

import java.util.List;

class CheatAdapter extends RecyclerView.Adapter<CheatAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Cheat> cheats;

    CheatAdapter(Context context, List<Cheat> cheats) {
        this.cheats = cheats;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public CheatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.cheat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CheatAdapter.ViewHolder holder, int position) {
        Cheat cheat = cheats.get(position);
        holder.title.setText(cheat.getTitle());
    }

    @Override
    public int getItemCount() {
        return cheats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final Button title;

        ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.cheat_lay);
        }
    }
}
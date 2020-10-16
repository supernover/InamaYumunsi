package com.supernover.inamayumunsiclient.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.supernover.inamayumunsiclient.Model.GetSongs;
import com.supernover.inamayumunsiclient.Model.Utility;
import com.supernover.inamayumunsiclient.R;

import java.util.List;

public class JcSongsAdapter extends RecyclerView.Adapter<JcSongsAdapter.SongsAdapterViewHolder> {
    private int seletedPosition;
    Context context;
    List<GetSongs> arrayListSongs;
    private RecyclerItemClickLister listener;

    public JcSongsAdapter(Context context, List<GetSongs> arrayListSongs, RecyclerItemClickLister listener) {
        this.context = context;
        this.arrayListSongs = arrayListSongs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SongsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.songs_row,parent,false);

        return new SongsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongsAdapterViewHolder holder, int position) {

        GetSongs getSongs = arrayListSongs.get(position);
        if (getSongs != null){
            if (seletedPosition == position){
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
                holder.iv_play_active.setVisibility(View.VISIBLE);
            }else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.transparent));
                holder.iv_play_active.setVisibility(View.INVISIBLE);

            }
        }
        holder.tv_title.setText(getSongs.getSongTitle());
        holder.tv_artist.setText(getSongs.getArtist());
        String duration = Utility.convertDuration(Long.parseLong(getSongs.getSongDuration()));
        holder.tv_duration.setText(duration);
        holder.bind(getSongs, listener);


    }

    @Override
    public int getItemCount() {
        return arrayListSongs.size();
    }

    public class SongsAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title,tv_artist,tv_duration;
        ImageView iv_play_active;


        public SongsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_artist = itemView.findViewById(R.id.tv_artist);
            tv_duration = itemView.findViewById(R.id.tv_duration);
            iv_play_active = itemView.findViewById(R.id.iv_play_active);


        }

        public void bind(final GetSongs getSongs, final RecyclerItemClickLister listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listener.onClickListener(getSongs,getAdapterPosition());


                }
            });
        }
    }

    public interface RecyclerItemClickLister {
        void onClickListener(GetSongs songs, int position);

    }

    public int getSeletedPosition() {
        return seletedPosition;
    }

    public void setSeletedPosition(int seletedPosition) {
        this.seletedPosition = seletedPosition;
    }
}

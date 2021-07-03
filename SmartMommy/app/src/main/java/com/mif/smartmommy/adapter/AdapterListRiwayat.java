package com.mif.smartmommy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mif.smartmommy.R;
import com.mif.smartmommy.model.ModelListAnak;
import com.mif.smartmommy.model.ModelRiwayat;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterListRiwayat extends RecyclerView.Adapter<AdapterListRiwayat.MyViewHolder> {
    private List<ModelRiwayat> item;
    private Context context;
    private OnHistoryClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView id_anak, nama_anak, tanggal_lahir, riwayat;
        ImageView foto_anak;
        public MyViewHolder(View itemView, final OnHistoryClickListener listener) {
            super(itemView);
            id_anak = itemView.findViewById(R.id.text_idanak_riwayat);
            nama_anak = itemView.findViewById(R.id.text_namaanak_riwayat);
            tanggal_lahir = itemView.findViewById(R.id.text_tgllahiranak_riwayat);
            riwayat = itemView.findViewById(R.id.text_hasil_riwayat);
            foto_anak = itemView.findViewById(R.id.image_fotoanak_riwayat);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onClick(position);
                        }
                    }
                }
            });
        }
    }

    public AdapterListRiwayat(Context context, List<ModelRiwayat> item) {
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_riwayat, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(layout, listener);
        return myViewHolder;
    }

    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        ModelRiwayat me = item.get(position);
        holder.id_anak.setText(me.getId_anak());
        holder.nama_anak.setText(me.getNama_anak());
        holder.tanggal_lahir.setText(me.getTanggal_lahir());
//        holder.riwayat.setText(me.getRiwayat());
        if (me.getRiwayat() > 00.8 && me.getRiwayat() <= 1){
            holder.riwayat.setText("Hasil Kuisioner : Anak Sesuai Dengan Tahapan");
        } else if (me.getRiwayat() > 00.6 && me.getRiwayat() <= 00.8){
            holder.riwayat.setText("Hasil Kuisioner : Anak Penyimpangan Meragukan");
        } else if (me.getRiwayat() < 00.6){
            holder.riwayat.setText("Hasil Kuisioner : Kemungkinan Penyimpangan");
        }
        Picasso.get().load(me.getFoto_anak()).into(holder.foto_anak);
//        holder.foto.setImageResource(R.drawable.ic_chevron_right_primary);
    }

    public int getItemCount() {
        return item.size();
    }

    public interface OnHistoryClickListener {
        public void onClick(int position);
    }

    public void setListener(OnHistoryClickListener listener) {
        this.listener = listener;
    }
}

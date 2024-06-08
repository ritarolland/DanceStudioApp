package com.example.dancestudioapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dancestudioapp.databinding.MasterclassItemBinding;
import com.example.dancestudioapp.interfaces.MasterclassesInterface;
import com.example.dancestudioapp.models.Masterclass;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MasterclassesAdapter extends RecyclerView.Adapter<MasterclassesAdapter.MasterclassesViewHolder> {

    private  final MasterclassesInterface masterclassesInterface;
    Context context;
    ArrayList<Masterclass> masterclasses;
    private final String currentUserId;

    public MasterclassesAdapter(MasterclassesInterface masterclassesInterface, Context context, ArrayList<Masterclass> masterclasses) {
        this.masterclassesInterface = masterclassesInterface;
        this.context = context;
        this.masterclasses = masterclasses;
        this.currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
    }

    @NonNull
    @Override
    public MasterclassesAdapter.MasterclassesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MasterclassItemBinding binding = MasterclassItemBinding.inflate(inflater, parent, false);
        return new MasterclassesViewHolder(binding, masterclassesInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MasterclassesAdapter.MasterclassesViewHolder holder, int position) {
        Masterclass masterclass = masterclasses.get(position);
        holder.bind(masterclass, currentUserId);
    }

    @Override
    public int getItemCount() {
        return masterclasses.size();
    }

    public static class MasterclassesViewHolder extends RecyclerView.ViewHolder{
        MasterclassItemBinding binding;
        public MasterclassesViewHolder(@NonNull MasterclassItemBinding binding, MasterclassesInterface masterclassesInterface) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> {
                if(masterclassesInterface != null) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        masterclassesInterface.onItemClick(pos);
                    }
                }
            });
        }

        public void bind(Masterclass masterclass, String currentUserId) {
            binding.masterclassName.setText(masterclass.getName());
            Date date = masterclass.getDateObject();
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
            SimpleDateFormat sdfDate = new SimpleDateFormat("E dd.MM", Locale.getDefault());
            binding.time.setText(sdfTime.format(date));
            binding.date.setText(sdfDate.format(date));
            //если среди участников мастеркласса есть currentUserId то отображать поле takingPart
            if (masterclass.getMemberIds().contains(currentUserId)) {
                binding.takingPart.setVisibility(View.VISIBLE);
            } else {
                binding.takingPart.setVisibility(View.INVISIBLE);
            }
        }
    }
}

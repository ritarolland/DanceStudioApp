package com.example.dancestudioapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dancestudioapp.databinding.MemberItemBinding;
import com.example.dancestudioapp.models.User;
import com.example.dancestudioapp.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MembersViewHolder> {

    Context context;
    ArrayList<String> memberIds;

    public MembersAdapter(Context context, ArrayList<String> memberIds) {
        this.context = context;
        this.memberIds = memberIds;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<String> newMembersIds) {
        this.memberIds.clear();
        this.memberIds.addAll(newMembersIds);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MembersAdapter.MembersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MemberItemBinding binding = MemberItemBinding.inflate(inflater, parent, false);
        return new MembersAdapter.MembersViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MembersAdapter.MembersViewHolder holder, int position) {
        String memberId = memberIds.get(position);
        holder.bind(memberId);
    }

    @Override
    public int getItemCount() {
        return memberIds.size();
    }

    public static class MembersViewHolder extends RecyclerView.ViewHolder{
        MemberItemBinding binding;
        public MembersViewHolder(@NonNull MemberItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        public void bind(String memberId) {
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference(Constants.KEY_COLLECTION_USERS).child(memberId);
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String userName = snapshot.child(Constants.KEY_USER_NAME).getValue(String.class);
                        binding.memberName.setText(userName);
                    } else {
                        binding.memberName.setText("Unknown User");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    binding.memberName.setText("Error Loading User");
                }
            });

        }
    }
}

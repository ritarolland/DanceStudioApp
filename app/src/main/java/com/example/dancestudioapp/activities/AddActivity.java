package com.example.dancestudioapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.dancestudioapp.R;
import com.example.dancestudioapp.databinding.ActivityAddBinding;
import com.example.dancestudioapp.models.Masterclass;
import com.example.dancestudioapp.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    FirebaseDatabase DanceStudio;
    FirebaseAuth mAuth;
    DatabaseReference masterclasses;

    ActivityAddBinding activityAddBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddBinding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(activityAddBinding.getRoot());
        init();
        activityAddBinding.addButton.setOnClickListener(this::OnAddClick);
        activityAddBinding.buttonBack.setOnClickListener(v -> finish());
    }

    private void init() {
        DanceStudio = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        masterclasses = DanceStudio.getReference(Constants.KEY_COLLECTION_MASTERCLASSES);
    }

    public void OnAddClick(View view) {
        String name = activityAddBinding.title.getText().toString();
        String choreographer = activityAddBinding.choreographer.getText().toString();
        String hallName = activityAddBinding.hall.getText().toString();
        String maxMembersNumber = activityAddBinding.membersNumber.getText().toString();
        String date = activityAddBinding.date.getText().toString();
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(choreographer) && !TextUtils.isEmpty(hallName)
        && !TextUtils.isEmpty(maxMembersNumber) && !TextUtils.isEmpty(date)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd.MM.yyyy", Locale.getDefault());
            sdf.setLenient(false); // Устанавливаем строгий режим проверки
            try {
                Date dateObject = sdf.parse(date);
                Masterclass masterclass = new Masterclass(dateObject, name, choreographer, hallName, Integer.parseInt(maxMembersNumber));
                String key = masterclasses.push().getKey();
                masterclass.setId(key);
                // Сохраняем объект Masterclass в базе данных под полученным ключом
                masterclasses.child(key).setValue(masterclass);
                finish();
            } catch (ParseException e) {
                Toast.makeText(this, R.string.wrong_format, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.fill_fields, Toast.LENGTH_SHORT).show();
        }
    }
}
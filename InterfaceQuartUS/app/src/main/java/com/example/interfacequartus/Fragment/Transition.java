package com.example.interfacequartus.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.interfacequartus.R;

public class Transition extends Dialog
{
    //Variables
    private int essaies = 0;
    ProgressBar chargement;

    public Transition(@NonNull Context context)
    {
        super(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.layout_transition);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        super.onCreate(savedInstanceState);
    }
    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.layout_transition, container);

        view.setBackground(new ColorDrawable(Color.TRANSPARENT));
        //Attributions
        chargement = view.findViewById(R.id.chargement);

        chargement.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return view;
    }*/

    @Override
    public void onBackPressed()
    {
        Toast message;
        essaies++;
        if(essaies == 3)
        {
            message = Toast.makeText(getContext(),"Va falloir que tu sois plus patient que ça!", Toast.LENGTH_LONG);
        }
        else if(essaies == 2)
        {
            message = Toast.makeText(getContext(),"NOPE!", Toast.LENGTH_SHORT);
        }
        else
        {
            message = Toast.makeText(getContext(),"Bien éssayé!", Toast.LENGTH_SHORT);
        }
        message.show();
    }
}

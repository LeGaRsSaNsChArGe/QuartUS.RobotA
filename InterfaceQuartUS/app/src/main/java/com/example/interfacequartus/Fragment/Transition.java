package com.example.interfacequartus.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.interfacequartus.R;

public class Transition extends DialogFragment
{
    //Variables
    private int essaies = 0;
    ProgressBar chargement;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.layout_transition, container, false);

        //Attributions
        chargement = view.findViewById(R.id.chargement);

        chargement.setIndeterminate(true);
        chargement.setIndeterminateDrawable(getResources().getDrawable(R.drawable.style_chargement, null));

        return view;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        return new Dialog(getActivity(), getTheme())
        {
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
                message.setGravity(Gravity.CENTER, 0, 0);
                message.show();
            }
        };
    }
}

package com.android.dubaicovid19.view.custom;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.android.dubaicovid19.R;

public class AlertDialogUtil {

    private static ViewGroup viewGroup;

    public static void alertDialog(String message, Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                //.setTitle(title)
                .setMessage(message)
                .setIcon(R.drawable.ic_warning)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        positiveButton.setAllCaps(false);

        Typeface face = Typeface.createFromAsset(context.getAssets(), "Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setTypeface(face);
        textView.setPadding(80, 25, 25, 10);
    }
}

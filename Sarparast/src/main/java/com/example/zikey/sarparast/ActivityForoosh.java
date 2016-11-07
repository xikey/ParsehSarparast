package com.example.zikey.sarparast;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.zikey.sarparast.Helpers.PreferenceHelper;

public class ActivityForoosh extends AppCompatActivity {

    private PreferenceHelper preferenceHelper;
    private ImageView imgBack;

    private LinearLayout lyMantaghe;
    private LinearLayout lyTarikh;
    private LinearLayout lyMahane;
    private LinearLayout lyVisitor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foroosh);

        lyMantaghe = (LinearLayout) findViewById(R.id.lyMantaghe);
        lyTarikh = (LinearLayout) findViewById(R.id.lyTarikh);
        lyMahane = (LinearLayout) findViewById(R.id.lyMahane);
        lyVisitor = (LinearLayout) findViewById(R.id.lyVisitor);


        preferenceHelper = new PreferenceHelper(this);
        imgBack = (ImageView) findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        lyMahane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityForoosh.this, ActivityAnalyseOfSefareshat.class);
                intent.putExtra("State", "Mahane");
                startActivity(intent);

            }
        });
        lyMantaghe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CharSequence options[] = new CharSequence[]{"سطح یک", "سطح دو", "سطح سه", "سطح چهار", "سطح پنج"};

                new android.app.AlertDialog.Builder(ActivityForoosh.this)
                        .setTitle("انتخاب سطح منطقه")
                        .setCancelable(true)
                        .setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0: {
                                        regionLevelChooser(1);
                                        dialog.dismiss();
                                        break;
                                    }
                                    case 1: {
                                        regionLevelChooser(2);
                                        dialog.dismiss();
                                        break;
                                    }
                                    case 2: {
                                        regionLevelChooser(3);
                                        dialog.dismiss();
                                        break;
                                    }
                                    case 3: {
                                        regionLevelChooser(4);
                                        dialog.dismiss();
                                        break;
                                    }
                                    case 4: {
                                        regionLevelChooser(5);
                                        dialog.dismiss();
                                        break;
                                    }
                                }
                            }
                        }).create().show();

            }
        });
        lyTarikh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityForoosh.this, ActivityAnalyseOfSefareshat.class);
                intent.putExtra("State", "Date");
                startActivity(intent);

            }
        });
        lyVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityForoosh.this, ActivityAnalyseOfSefareshat.class);
                intent.putExtra("State", "Visitor");
                startActivity(intent);

            }
        });

    }

    private void regionLevelChooser(int level) {

        Intent intent = new Intent(ActivityForoosh.this, ActivityAnalyseOfSefareshat.class);
        intent.putExtra("State", "Manategh");
        intent.putExtra("level", level);
        startActivity(intent);
    }
}

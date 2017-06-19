package com.github.a5turman.flower;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class FlowerActivity extends AppCompatActivity {

    private FlowerView flowerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_flower);

        flowerView = (FlowerView) findViewById(R.id.flower);
        flowerView.setLeafCoords(App.getPot(this).getCoords());

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditor();
            }
        });
    }

    void startEditor() {
        Intent intent = new Intent(this, LeafEditor.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && resultCode == RESULT_OK) {
            flowerView.setLeafCoords(App.getPot(this).getCoords());
        }
    }

}

package com.github.a5turman.flower;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

/**
 * Created by 5turman on 6/16/2017.
 */
public class LeafEditor extends Activity implements View.OnTouchListener {

    Pot pot;

    LeafView leaf;

    int pointIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pot = App.getPot(this);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_leaf_editor);

        leaf = (LeafView) findViewById(R.id.leaf);
        leaf.setCoords(pot.getCoords());
        leaf.setOnTouchListener(this);

        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaf.dump(dumpCallback);
            }
        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pointIndex = leaf.peak(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                pointIndex = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                if (pointIndex >= 0) {
                    leaf.updatePoint(pointIndex, event.getX(), event.getY());
                }
                break;
        }
        return true;
    }

    private final LeafView.DumpCallback dumpCallback = new LeafView.DumpCallback() {
        @Override
        public void onDump(float[] coords) {
            pot.setCoords(coords);
            setResult(RESULT_OK);
            finish();
        }
    };

}

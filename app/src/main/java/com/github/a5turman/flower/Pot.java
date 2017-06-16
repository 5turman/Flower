package com.github.a5turman.flower;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

/**
 * Created by 5turman on 6/16/2017.
 */
public class Pot {

    private static final String KEY_COORDS = "coords";
    private static final String COORDS_DEFAULT = "0.682094,0.39097074,1.0171707,0.15305719";

    private final SharedPreferences prefs;

    public Pot(Context context) {
        prefs = context.getSharedPreferences("pot", Context.MODE_PRIVATE);
    }

    /**
     * @return polar coordinates of 2 main points of leaf: (radius1, angle1, radius2, angle2)
     */
    @Nullable
    public float[] getCoords() {
        String[] values = prefs.getString(KEY_COORDS, COORDS_DEFAULT).split(",");

        float[] coords = new float[values.length];
        for (int i = 0; i < values.length; ++i) {
            coords[i] = Float.valueOf(values[i]);
        }

        return coords;
    }

    public void setCoords(float[] coords) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < coords.length; ++i) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(coords[i]);
        }

        prefs.edit().putString(KEY_COORDS, sb.toString()).apply();
    }

}

package com.github.a5turman.flower;

import android.app.Application;
import android.content.Context;

/**
 * Created by 5turman on 6/16/2017.
 */
public class App extends Application {

    public static Pot getPot(Context context) {
        App app = (App) context.getApplicationContext();
        return app.pot;
    }

    private Pot pot;

    @Override
    public void onCreate() {
        super.onCreate();

        pot = new Pot(this);
    }

}

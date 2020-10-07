package de.lingen.hsosna.texview;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;

/**
 * Der HandlerThread setzt die angesprochene Aktivität in den Hintergrund.
 */
public class ExampleHandlerThread extends HandlerThread {

    private static final String TAG = "ExampleHandlerThread";

    private Handler handler;

    public ExampleHandlerThread () {
        super("ExampleHandlerThread", Process.THREAD_PRIORITY_BACKGROUND); // first just for debugging purposes ---------
    }

    @Override
    protected void onLooperPrepared () {
        handler = new Handler();
    }

    public Handler getHandler () {
        return handler;
    }
}

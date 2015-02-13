package com.firebase.so28487255;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;

import java.util.concurrent.CountDownLatch;


public class MainActivity extends Activity {
    public final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);

        Firebase ref = new Firebase("https://dinosaurs.firebaseio.com/hello");


        final CountDownLatch latch = new CountDownLatch(1);
        Log.v(TAG, "Starting new transaction");
        ref.runTransaction(new Transaction.Handler() {

            @Override
            public void onComplete(FirebaseError e, boolean b, DataSnapshot data) {
                Log.v(TAG, "onComplete ran");
                if(e != null || !b) {
                    Log.e(TAG, "error: " + e + ", committed: " + b);
                }
                Log.v(TAG, "transaction finished!");
//                latch.countDown();
            }

            @Override
            public Transaction.Result doTransaction(MutableData data) {
                Log.v(TAG, "doing transaction");
                data.setValue("world");

                Transaction.Result result = Transaction.success(data);
                return result;
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Log.v(TAG, "never printed");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

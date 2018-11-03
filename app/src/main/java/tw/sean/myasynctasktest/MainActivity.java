package tw.sean.myasynctasktest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;

/*
 *
 */

public class MainActivity extends AppCompatActivity {
    private TextView mesg;
    private MyAsyncTask myAsyncTask;
    private int[] imgIds = {R.id.img0, R.id.img1, R.id.img2};
    private ImageView[] imgViews = new ImageView[imgIds.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mesg = findViewById(R.id.mesg);
        for (int i=0; i<imgViews.length; i++){
            imgViews[i] = findViewById(imgIds[i]);
        }
    }

    public void test1(View view) {
        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute(
                "http://pic1.win4000.com/pic/0/a9/1fc31230689.jpg",
                "http://pic1.win4000.com/pic/b/03/21691230679.jpg"
        );
    }

    public void test2(View view) {
        if (myAsyncTask != null){
            myAsyncTask.cancel();
        }
    }

    private class MyAsyncTask extends AsyncTask <String, Bitmap, Void>{
        private int index;
        @Override
        //執行前
        protected void onPreExecute() {
            super.onPreExecute();
            Log.v("brad", "onPreExecute()");
        }

        @Override
        //背景執行
        protected Void doInBackground(String... urls) {
            //Log.v("brad", "doInBG");
            for (String url : urls){
                Log.v("brad", url);

                try {
                    if (isCancelled()){
                        throw new Exception();
                    }
                    URL imgurl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection)imgurl.openConnection;
                    Bitmap bmp = BitmapFactory.decodeStream(conn.getInputStream());
                    //發布進度(可帶參數進去)
                    publishProgress();
                }catch (Exception e){
                    Log.v("brad","");
                    break;
                }



            }
            return null;
        }

        @Override
        //執行進度更新
        //...代表一次可以接收多個參數
        protected void onProgressUpdate(Bitmap... bmps) {
            super.onProgressUpdate(bmps);
            imgViews[index].setImageBitmap(bmp[0]);
            index++;
            //Log.v("brad", "onProgressUpdate()");
        }

        @Override
        //執行後
        protected void onPostExecute(String mesg) {
            super.onPostExecute(mesg);
            new AlertDialog.Builder(MainActivity.this)
                .setMessage("Load Finish")
                .show();
            Log.v("brad", "onPostExecute()");
        }

        @Override
        //被取消
        protected void onCancelled(String mesg) {
            super.onCancelled(mesg);
            Log.v("brad", "onCancelled(Void aVoid)");
        }

        @Override
        //被取消
        protected void onCancelled() {
            super.onCancelled();
            Log.v("brad", "onCancelled()");
        }

    }
}

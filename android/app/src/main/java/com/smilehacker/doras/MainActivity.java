package com.smilehacker.doras;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.code.regexp.Matcher;
import com.google.code.regexp.Pattern;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getName();

    private Button mBtnRouter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnRouter = (Button) findViewById(R.id.btn_router);

        RouterManager.init(this);

        mBtnRouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testUrl();
            }
        });
    }

    private void testRex() {
        Pattern p = Pattern.compile("abc(?<foo>\\d+)xyz");
        Matcher m = p.matcher("abc123xyz");
        System.out.println(m.find());       // true
        System.out.println(m.group("foo")); // 123
    }

    private void testUrl() {
        String url = "http://smilehacker.com/user/10010?a=1";
        RouterResult result = RouterManager.inst().matchUrl(url);
        if (result != null) {
            Log.i(TAG, "get id:" + result.item.compomentID);
        }
    }
}

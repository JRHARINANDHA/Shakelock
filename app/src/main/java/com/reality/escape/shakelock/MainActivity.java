package com.reality.escape.shakelock;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {


    Switch aSwitch;
    TextView textView;
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mComponentName;
    private static final int ADMIN_INTENT = 15;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    int check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aSwitch = (Switch)findViewById(R.id.switch1);
        textView = (TextView)findViewById(R.id.switch1) ;
        mDevicePolicyManager = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(this, MyAdminReceiver.class);
        sharedPreferences = getSharedPreferences("check",MODE_PRIVATE);
        check = sharedPreferences.getInt("check",0);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b)
                {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Add this app to enable lock and unlock");
                    startActivityForResult(intent,ADMIN_INTENT);
                    startService(new Intent(getBaseContext(),Shaker.class));
                    textView.setText("Service is turned ON");
                    editor = getSharedPreferences("check",MODE_PRIVATE).edit();
                    editor.putInt("check",1);
                    editor.commit();
                }
                else

                {

                    stopService(new Intent(getBaseContext(),Shaker.class));
                    textView.setText("Service is turned OFF");
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADMIN_INTENT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Registered As Admin", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Failed to register as Admin", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

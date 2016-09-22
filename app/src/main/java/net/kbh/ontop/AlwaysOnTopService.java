package net.kbh.ontop;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

public class AlwaysOnTopService extends Service {
    private View mView; //항상 보이게 할 뷰

    @Override
    public IBinder onBind(Intent arg0) { return null; }

    @Override
    public void onCreate() {
        super.onCreate();

        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.onscreen, null);

        //최상위 윈도우에 넣기 위한 설정
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,					//항상 최 상위에 있게
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,		//터치 인식, 나중에 기능 추가를 위해 일단 넣어둠
                PixelFormat.TRANSLUCENT);													//투명

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);	//윈도우 매니저 불러옴.
        wm.addView(mView, params);		//최상위 윈도우에 뷰 넣기. *중요 : 여기에 permission을 미리 설정해 두어야 한다. 매니페스트에
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mView != null) { //서비스 종료시 뷰 제거. *중요 : 뷰를 꼭 제거 해야함.
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mView);
            mView = null;
        }
    }

}
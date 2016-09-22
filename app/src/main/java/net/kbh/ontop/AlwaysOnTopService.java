package net.kbh.ontop;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;

public class AlwaysOnTopService extends Service {
    private View mPopupView;							//항상 보이게 할 뷰
    private WindowManager.LayoutParams mParams;		//layout params 객체. 뷰의 위치 및 크기를 지정하는 객체
    private WindowManager mWindowManager;			//윈도우 매니저
    private SeekBar mSeekBar;								//투명도 조절 seek bar

    private float START_X, START_Y;							//움직이기 위해 터치한 시작 점
    private int PREV_X, PREV_Y;								//움직이기 이전에 뷰가 위치한 점
    private int MAX_X = -1, MAX_Y = -1;					//뷰의 위치 최대 값

    private void show(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private OnTouchListener mViewTouchListener = new OnTouchListener() {
        @Override public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction()==MotionEvent.ACTION_UP) {
                show("mViewTouchListener");
            }
            return true;
        }
    };

    @Override
    public IBinder onBind(Intent arg0) { return null; }

    @Override
    public void onCreate() {
        super.onCreate();

        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPopupView = mInflater.inflate(R.layout.onscreen, null);
        mPopupView.setOnTouchListener(mViewTouchListener);										//팝업뷰에 터치 리스너 등록

        //최상위 윈도우에 넣기 위한 설정
        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,					//항상 최 상위에 있게. status bar 밑에 있음. 터치 이벤트 받을 수 있음.
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,		//이 속성을 안주면 터치 & 키 이벤트도 먹게 된다.
                //포커스를 안줘서 자기 영역 밖터치는 인식 안하고 키이벤트를 사용하지 않게 설정
                PixelFormat.TRANSLUCENT);										//투명
        mParams.gravity = Gravity.RIGHT | Gravity.TOP;						//왼쪽 상단에 위치하게 함.

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);	//윈도우 매니저 불러옴.
        mWindowManager.addView(mPopupView, mParams);		//최상위 윈도우에 뷰 넣기. *중요 : 여기에 permission을 미리 설정해 두어야 한다. 매니페스트에
    }

    @Override
    public void onDestroy() {
        if(mWindowManager != null) {		//서비스 종료시 뷰 제거. *중요 : 뷰를 꼭 제거 해야함.
            if(mPopupView != null) mWindowManager.removeView(mPopupView);
            if(mSeekBar != null) mWindowManager.removeView(mSeekBar);
        }
        super.onDestroy();
    }
}
package ravi.lib.mchronometer;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ravi Prakash on 7/9/2014.
 */
public class Chronometer extends TextView {
    CharSequence Startingtime,TimeInterval;
    Boolean Inflated=false;
    DecimalFormat l_format,format;
    long startTime;
    long hrs = 00;
    long min = 00;
    long sec = 00;
    long elapsed=00;
    Timer timer;
    long mill = 0;
    Context context;
    public Chronometer(Context context) {
        super(context);
        this.context=context;
    }

    public Chronometer(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a=context.obtainStyledAttributes(attrs,
                R.styleable.Chronometer);
        format=new DecimalFormat("00");
        l_format=new DecimalFormat("000");
        Startingtime = a.getString(R.styleable.Chronometer_startingtime);
        TimeInterval= a.getString(R.styleable.Chronometer_timeinterval);
        this.context=context;
    }

    public Chronometer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a=context.obtainStyledAttributes(attrs,
                R.styleable.Chronometer);
        Startingtime = a.getString(R.styleable.Chronometer_startingtime);
        TimeInterval= a.getString(R.styleable.Chronometer_timeinterval);
        this.context=context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Inflated=true;
        Chronometer.this.setText("00:00:00:000");
    }

    public void pause()
    {
        if(elapsed!=00)
        {
            timer.cancel();
            Chronometer.this.setText(hrs+":"+min+":"+sec + ":" + mill);
        }
    }


    public void  reset()
    {
        if(elapsed!=00)
        {
            timer.cancel();
            hrs=00;min=00;sec=00;mill=00;elapsed=00;
            Chronometer.this.setText("00:00:00:000");
        }
    }

    public void start()
    {
        startTime = System.currentTimeMillis();
        if(Inflated)
        {
         timer  =new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    CalThread();
                }
            }, Integer.parseInt((String)Startingtime),((Integer.parseInt((String)TimeInterval)>=0)?1:(Integer.parseInt((String)TimeInterval))));//Integer.parseInt((String)Duration));
        }
    }

    private void CalThread() {
        ((Activity) context).runOnUiThread(new RunCalThread());
    }

    private class RunCalThread extends Thread{
        @Override
        public void run() {
            elapsed = System.currentTimeMillis() - startTime;
                if (elapsed >= 1000) {

                    sec = elapsed / 1000;
                    mill = elapsed % 1000;
                    if(sec>59)
                    {
                        min=sec/60;
                        sec=sec%60;
                    }
                    if(min>59)
                    {
                        hrs=min/60;
                        min=min%60;
                    }
                }
                else
                    mill = elapsed;

            Chronometer.this.setText(format.format(hrs)+":"+format.format(min)+":"+format.format(sec) + ":" + l_format.format(mill));
        }
    }

}

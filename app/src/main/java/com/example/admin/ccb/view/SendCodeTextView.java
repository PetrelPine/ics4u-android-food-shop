package com.example.admin.ccb.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.admin.ccb.R;


/**
 * Get Verification Code Button
 */
@SuppressLint("AppCompatCustomView")
public class SendCodeTextView extends TextView {

    private CountDownTimer countDownTimer;

    public SendCodeTextView(Context context) {
        this(context, null);
    }

    public SendCodeTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SendCodeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean performClick() {
        sendCountDownTimer();
        return super.performClick();
    }

    public void sendCountDownTimer() {
        // setBackgroundResource(R.drawable.shape_bge0e0e0_radius3);
        setTextColor(getResources().getColor(R.color.color_e0e0e0));
        setClickable(false);
        if (countDownTimer != null) {
            countDownTimer = null;
        }
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int s = (int) (millisUntilFinished / 1000);
                if (s < 10)
                    // (60 sec)
                    setText(" 0" + s + "to resend");
                else
                    setText(" " + s + "to resend");
            }

            @Override
            public void onFinish() {
                setText("Get Verification Code");
                countDownTimer.cancel();
                setClickable(true);
                // setBackgroundResource(R.drawable.shap_bgffffff_e0e0e0_radius3);
                setTextColor(getResources().getColor(R.color.color_292929));
                countDownTimer = null;
            }
        };
        countDownTimer.start();
    }
}

package www.ccb.com.common.widget.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import www.ccb.com.common.R;


public class CbLoadingDialog extends ProgressDialog {

    private final int progressColor = -1;
    private TextView tvMessage;
    private CharSequence message;

    public CbLoadingDialog(Context context) {
        super(context, R.style.CbLoadingDialog);
    }

    public CbLoadingDialog(Context context, int theme) {
        super(context, R.style.CbLoadingDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(getContext());
    }

    private void init(Context context) {
        setCancelable(true);
        setCanceledOnTouchOutside(false);

        setContentView(R.layout.c_progress_dialog);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        tvMessage = findViewById(R.id.progress_dialog_message);
        tvMessage.setText(message);
        if (TextUtils.isEmpty(message)) {
            tvMessage.setVisibility(View.GONE);
        }
    }

    public void setMessage(CharSequence message) {
        this.message = message;
        if (tvMessage != null) tvMessage.setText(message);

    }

    @Override
    public void show() {
        super.show();
    }

    public void Cancelable(boolean b) {
        setCancelable(b);
    }
}

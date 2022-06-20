package www.ccb.com.common.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;

import java.util.List;

import www.ccb.com.common.R;
import www.ccb.com.common.utils.GsonUtils;
import www.ccb.com.common.utils.L;
import www.ccb.com.common.utils.ToastUtils;
import www.ccb.com.common.widget.dialog.CbLoadingDialog;


public abstract class BaseActivity extends AppCompatActivity {

    public Context mContext;
    public Bundle savedInstanceState;
    public ImmersionBar mImmersionBar;
    private CbLoadingDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewResource());
        this.savedInstanceState = savedInstanceState;
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();  // All subclasses will inherit these same properties
        mContext = this;
        initView();
        initData();
        initList();
    }

    public void start(Class clazz) {
        startActivity(new Intent(mContext, clazz));
    }

    public abstract int getContentViewResource();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initList();

    public void UpTitle(String title) {
        if (findViewById(R.id.vBar) == null) return;
        mImmersionBar.titleBar(R.id.vBar).statusBarDarkFont(true, 0.2f).init();
        findViewById(R.id.tvTitleBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tvTitleBar)).setText(title == null ? "" : title);
    }

    public void okGetRequest(String url) {
        okGetRequest(null, url, null);
    }

    public void okGetRequest(String with, String url) {
        okGetRequest(with, url, null);
    }

    public void okGetRequest(String with, String url, List<Object> params) {
        okGetRequest(with, url, params, null, null, false);
    }

    public void okGetRequest(String with, String url, List<Object> params, Class clazz, String dialogMsg, boolean isDialog) {
        if (TextUtils.isEmpty(with)) with = url;
        String finalWith = with;
        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                url = url + "/" + params.get(i);
            }
        }
        OkGo.<String>get(url).execute(new StringCallback() {
            @Override
            public void onStart(com.lzy.okgo.request.base.Request<String, ? extends com.lzy.okgo.request.base.Request> request) {
                super.onStart(request);
                if (isDialog) showProgressDialog(dialogMsg);
                okResponseStart(finalWith);
            }

            @Override
            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                L.out(finalWith + "Request Result:__", response.body());
                okResponseSuccess(finalWith, response.body());
            }

            @Override
            public void onError(com.lzy.okgo.model.Response<String> response) {
                super.onError(response);
                okResponseError(finalWith, response.body());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (isDialog) dismissProgressDialog();
                okResponseFinish(finalWith);
            }
        });
    }

    public void okPostRequest(final String what, final String httpurl, HttpParams params, final Class clazz) {
        okPostRequest(what, httpurl, params, clazz, null, false);
    }

    /**
     * OK network request
     *
     * @param httpurl      request url, also used for marking
     * @param params       request parameters
     * @param clazz        returned bean object
     * @param DialogMsg    pop-up dialog text
     * @param isShowDialog if pop-up dialog, default it enabled
     */
    public void okPostRequest(String what, String httpurl, HttpParams params, final Class clazz, final String DialogMsg, final boolean isShowDialog) {
        final String url = httpurl;
        final String finalWhat = TextUtils.isEmpty(what) ? url : what;
        OkGo.<String>post(url).params(params).execute(new StringCallback() {
            @Override
            public void onStart(com.lzy.okgo.request.base.Request<String, ? extends com.lzy.okgo.request.base.Request> request) {
                super.onStart(request);
                if (isShowDialog) showProgressDialog(DialogMsg);
                okResponseStart(finalWhat);
            }

            @Override
            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                L.out(finalWhat + "Request Result:__", response.body());
                if (clazz == null) {
                    okResponseSuccess(finalWhat, response.body());
                } else {
                    try {
                        Object bean = GsonUtils.fromJson(response.body(), clazz);
                        okResponseSuccess(finalWhat, bean);
                    } catch (Exception e) {
                        okResponseSuccess(finalWhat, null);
                        ToastUtils.GsonExtremely();
                        L.e("Error Info: " + e.toString());
                    }
                }
            }

            @Override
            public void onError(com.lzy.okgo.model.Response<String> response) {
                super.onError(response);
                L.out(finalWhat + "Request Result:__", response.body());
                okResponseError(finalWhat, response.body());
                ToastUtils.failNetRequest();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (isShowDialog) dismissProgressDialog();
                okResponseFinish(finalWhat);
            }
        });
    }

    protected void okResponseSuccess(String whit, Object t) {
    }

    protected void okResponseError(String whit, String body) {
    }

    protected void okResponseStart(String flag) {
    }

    protected void okResponseFinish(String flag) {
    }

    public void showProgressDialog(String msg) {
        if (this.mProgressDialog == null)
            this.mProgressDialog = new CbLoadingDialog(mContext);
        if (!TextUtils.isEmpty(msg)) {
            this.mProgressDialog.setMessage(msg);
        } else {
            this.mProgressDialog.setMessage("");
        }
        this.mProgressDialog.show();
    }

    public void dismissProgressDialog() {
        if (this.mProgressDialog != null)
            this.mProgressDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();  // This method must be called to prevent memory leakage. Do not call this method. If the interface BAR changes, exit this interface and enter the state that will remember the last bar change without closing app
    }
}

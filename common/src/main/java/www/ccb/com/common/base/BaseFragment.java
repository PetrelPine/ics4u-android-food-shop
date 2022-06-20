package www.ccb.com.common.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyf.barlibrary.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;

import java.util.List;

import www.ccb.com.common.utils.GsonUtils;
import www.ccb.com.common.utils.L;
import www.ccb.com.common.utils.ToastUtils;
import www.ccb.com.common.widget.dialog.CbLoadingDialog;


public abstract class BaseFragment extends Fragment {

    public ImmersionBar mImmersionBar;
    public Context mContext;
    public View rootView;
    protected LayoutInflater inflater;
    private boolean isVisible;
    private CbLoadingDialog mProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.inflater = inflater;
        mContext = getActivity();
        rootView = initContentView(inflater, container, savedInstanceState);
        initView(rootView);
        loadData();
        initListener();
        return rootView;
    }

    public <T extends View> T findViewById(@IdRes int ids) {
        return rootView.findViewById(ids);
    }

    public void start(Class clazz) {
        startActivity(new Intent(mContext, clazz));
    }

    public void isTitleBar(boolean is, View v) {
        if (is) {
            mImmersionBar = ImmersionBar.with(this);  // can be any view;
            mImmersionBar.titleBarMarginTop(v).statusBarDarkFont(true, 0.2f).init();
        }
    }

    /**
     * If used with ViewPager, setUserVisibleHint is called
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * If the display is controlled by the FragmentTransaction show and hide methods, onHiddenChanged is called.
     * If the Fragment was originally shown, it would need to hide and then show to trigger the event
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }

        if (!hidden && mImmersionBar != null)
            mImmersionBar.init();
    }

    protected void onVisible() {
    }

    protected void onInvisible() {
    }

    protected abstract View initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void initView(View view);

    public abstract void loadData();

    public abstract void initListener();

    public void okGetRequest(String url) {
        okGetRequest(null, url);
    }

    public void okGetRequest(String with, String url) {
        okGetRequest(with, url, null);
    }

    public void okGetRequestWeb(String with, String url, List<Object> params) {
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
                okResponseFinish(finalWith);
            }
        });
    }

    public void okGetRequest(String with, String url, HttpParams params) {
        if (TextUtils.isEmpty(with)) with = url;
        String finalWith = with;
        OkGo.<String>get(url).params(params).execute(new StringCallback() {
            @Override
            public void onStart(com.lzy.okgo.request.base.Request<String, ? extends com.lzy.okgo.request.base.Request> request) {
                super.onStart(request);
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
        this.mProgressDialog.show();
    }

    public void dismissProgressDialog() {
        if (this.mProgressDialog != null)
            this.mProgressDialog.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }
}
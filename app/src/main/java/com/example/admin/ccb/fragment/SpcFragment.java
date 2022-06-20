package com.example.admin.ccb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.admin.ccb.R;
import com.example.admin.ccb.activity.GoodsInfoActivity;
import com.example.admin.ccb.activity.ShopHomeActivity;
import com.example.admin.ccb.bean.ShopCartBean;
import com.example.admin.ccb.utils.ResData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import www.ccb.com.common.base.BaseFragment;
import www.ccb.com.common.utils.ToastUtils;
import www.ccb.com.common.utils.UiUtils;


public class SpcFragment extends BaseFragment {

    private SpcAdapter spcAp;
    private ShopCartBean spcs;
    private CheckBox cbAll;
    private TextView jiesuan;
    private RecyclerView rvShop;

    @Override
    protected View initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_spc, container, false);
    }

    @Override
    public void initView(View view) {
        rvShop = view.findViewById(R.id.rvShop);
        cbAll = view.findViewById(R.id.spc_cb_all);
        jiesuan = view.findViewById(R.id.jiesuan);
        rvShop.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        spcAp = new SpcAdapter(R.layout.item_spc_shop);
        rvShop.setAdapter(spcAp);
        isTitleBar(true, view);
    }

    @Override
    public void loadData() {
        Random random = new Random();
        spcs = new ShopCartBean();
        spcs.shopList = new ArrayList<>();
        for (int i = 0; i < ResData.getMenus().size(); i++) { // add shop
            ShopCartBean.ShopBean shop = new ShopCartBean.ShopBean();
            shop.shopName = ResData.getMenus().get(i);
            int jj = random.nextInt(5) + 1;
            shop.carList = new ArrayList<>();
            for (int j = 0; j < jj; j++) { // add item
                ShopCartBean.ShopBean.CarListBean goods = new ShopCartBean.ShopBean.CarListBean();
                goods.title = ResData.getspcGoodsImages().get(random.nextInt(ResData.getspcGoodsImages().size() - 1));
                goods.icon = ResData.getSpcImages().get(random.nextInt(ResData.getSpcImages().size() - 1));
                shop.carList.add(goods);
            }
            spcs.shopList.add(shop);
        }
        spcAp.setNewData(spcs.shopList);
    }

    @Override
    public void initListener() {
        cbAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean is = cbAll.isChecked();
                for (int i = 0; i < spcs.shopList.size(); i++) {
                    spcs.shopList.get(i).isSelectShop = is;
                    for (int j = 0; j < spcs.shopList.get(i).carList.size(); j++) {
                        spcs.shopList.get(i).carList.get(j).isSelectGoods = is;
                    }
                }
                spcAp.notifyDataSetChanged();
            }
        });
        jiesuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wheel();
            }
        });
    }


    private void wheel() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int index, int options2, int options3, View v) {
                // ImageInfo aliPay = new ImageInfo();
                // aliPay.setSrcUrl(R.mipmap.ali_pay);
                // ImageInfo wePay = new ImageInfo();
                // wePay.setSrcUrl(R.mipmap.wecart_pay);
                // List<ImageInfo> pays = Arrays.asList(aliPay , wePay);
                // Intent intent = new Intent(getActivity(), ImagePreviewActivity.class);
                // Bundle bundle = new Bundle();
                // bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) pays);
                // bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, index);
                // intent.putExtras(bundle);
                // getActivity().startActivity(intent);
                ToastUtils.showToast(mContext, "Paying Now!");
            }
        })
                .setSubmitText("OK") // Confirm the button text
                .setCancelText("Cancel") // Cancel button text
                .setTitleText("Payment Methods") // title
                .setSubCalSize(18) // Determine and cancel the text size
                .setTitleSize(20) // Title text size
                .setTitleColor(getResources().getColor(R.color.color_212121)) // Title text color
                .setSubmitColor(getResources().getColor(R.color.wecart_color)) // Determine the button text color
                .setCancelColor(getResources().getColor(R.color.color_87c489)) // Cancel the button text color
                .setTitleBgColor(0xFFffffff) // Title background color Night mode
                .setBgColor(0xFFffffff) // Wheel background color (Night mode)
                .setContentTextSize(20) // Scroll text size
                .setCyclic(false, false, false) // Whether to loop
                .setSelectOptions(0, 0, 0) // Sets the default options
                .setOutSideCancelable(false) // Click outer dismiss default true
                .isDialog(false) // Whether to display a dialog style
                .build();

        pvOptions.setPicker(Arrays.asList("AliPay", "WeChat Pay")); // add data source
        pvOptions.show();
    }


    /**
     * adapter
     */
    public class SpcAdapter extends BaseQuickAdapter<ShopCartBean.ShopBean, BaseViewHolder> {
        public SpcAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(final BaseViewHolder helper, ShopCartBean.ShopBean item) {
            helper.setText(R.id.tvShop, item.shopName)
                    .setOnClickListener(R.id.tvShop, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mContext.startActivity(new Intent(mContext, ShopHomeActivity.class));
                        }
                    });
            final CheckBox cbShop = helper.getView(R.id.spc_cb_shops);
            cbShop.setChecked(item.isSelectShop);
            cbShop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    spcs.shopList.get(helper.getAdapterPosition()).isSelectShop = cbShop.isChecked();
                    for (int i = 0; i < spcs.shopList.get(helper.getAdapterPosition()).carList.size(); i++) {  //改变这个店铺中所有商品的数据
                        spcs.shopList.get(helper.getAdapterPosition()).carList.get(i).isSelectGoods = cbShop.isChecked();
                    }
                    // Traverse the store set to see if all selected, if selected, change the all selected mark
                    List<Boolean> shoplist = new ArrayList<>();
                    for (int i = 0; i < spcs.shopList.size(); i++) {
                        shoplist.add(spcs.shopList.get(i).isSelectShop);
                    }
                    cbAll.setChecked(!shoplist.contains(false));
                    notifyDataSetChanged();
                }
            });
            RecyclerView rvGoods = helper.getView(R.id.rvGoods);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rvGoods.getLayoutParams();
            layoutParams.height = UiUtils.dp2px(100 * item.carList.size());
            rvGoods.setLayoutParams(layoutParams);

            rvGoods.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            rvGoods.setHasFixedSize(true); // Prohibit recyclerView sliding, avoid conflict with ScrollView;
            rvGoods.setNestedScrollingEnabled(false); // Prohibit recyclerView sliding, avoid conflict with ScrollView;
            GoodsAdapter goodsAp = new GoodsAdapter(R.layout.item_spc_goods, helper.getAdapterPosition());
            rvGoods.setAdapter(goodsAp);
            goodsAp.setNewData(item.carList);

        }
    }

    class GoodsAdapter extends BaseQuickAdapter<ShopCartBean.ShopBean.CarListBean, BaseViewHolder> {
        private final int positionShop;

        public GoodsAdapter(int layoutResId, int positionShop) {
            super(layoutResId);
            this.positionShop = positionShop;
        }

        @Override
        protected void convert(final BaseViewHolder helper, ShopCartBean.ShopBean.CarListBean item) {
            helper.setText(R.id.spc_tv_shop_name_msg, item.title)
                    .setOnClickListener(R.id.rl, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mContext.startActivity(new Intent(mContext, GoodsInfoActivity.class));
                        }
                    });
            helper.setImageResource(R.id.spc_iv_page, item.icon);
            final CheckBox cbGoods = helper.getView(R.id.spc_cb_goods);
            cbGoods.setChecked(item.isSelectGoods);
            cbGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    spcs.shopList.get(positionShop).carList.get(helper.getAdapterPosition()).isSelectGoods = cbGoods.isChecked();
                    // After changing the selected state of the product, traverse to see if all are selected. If all are selected, change the selected state of the store
                    List<Boolean> goodslist = new ArrayList<>();
                    for (int i = 0; i < spcs.shopList.get(positionShop).carList.size(); i++) {
                        goodslist.add(spcs.shopList.get(positionShop).carList.get(i).isSelectGoods);
                    }
                    spcs.shopList.get(positionShop).isSelectShop = !goodslist.contains(false);
                    // After changing the selected state of the store, traverse to see if all are selected. If all are selected, change the selected state of all
                    List<Boolean> shoplist = new ArrayList<>();
                    for (int i = 0; i < spcs.shopList.size(); i++) {
                        shoplist.add(spcs.shopList.get(i).isSelectShop);
                    }
                    cbAll.setChecked(!shoplist.contains(false));
                    spcAp.notifyDataSetChanged();
                    notifyDataSetChanged();
                }
            });

            Button btnJian = helper.getView(R.id.spc_btn_comm_count_jian);
            Button btnJia = helper.getView(R.id.spc_btn_comm_count_jia);
            TextView count = helper.getView(R.id.spc_et_comm_count);
            count.setText(item.count + "");
            btnJia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    spcs.shopList.get(positionShop).carList.get(helper.getAdapterPosition()).count = spcs.shopList.get(positionShop).carList.get(helper.getAdapterPosition()).count + 1;
                    notifyDataSetChanged();
                }
            });
            btnJian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (spcs.shopList.get(positionShop).carList.get(helper.getAdapterPosition()).count > 1) {
                        spcs.shopList.get(positionShop).carList.get(helper.getAdapterPosition()).count = spcs.shopList.get(positionShop).carList.get(helper.getAdapterPosition()).count - 1;
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }
}

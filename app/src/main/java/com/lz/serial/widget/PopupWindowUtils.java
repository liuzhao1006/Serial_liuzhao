package com.lz.serial.widget;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lz.base.view.AnimUtil;
import com.lz.serial.R;


/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/27 下午6:33
 * 描述     :
 */
public class PopupWindowUtils extends PopupWindow {

    private Activity activity;
    private AnimUtil animUtil;

    private static final long DURATION = 100;
    private static final float START_ALPHA = 0.7f;
    private static final float END_ALPHA = 1f;

    private float bgAlpha = 1f;
    private boolean bright = false;

    private ImageView iv;

    public PopupWindowUtils(Activity activity, ImageView iv) {
        super(activity);
        this.activity = activity;
        this.iv = iv;
        animUtil = new AnimUtil();

    }

    public void showPop(View view) {
       // 设置布局文件
        super.setContentView(view);
        // 为了避免部分机型不显示，我们需要重新设置一下宽高
        super.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        super.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置pop透明效果
        super.setBackgroundDrawable(new ColorDrawable(0x0000));
        // 设置pop出入动画
        super.setAnimationStyle(R.style.pop_add);
        // 设置pop获取焦点，如果为false点击返回按钮会退出当前Activity，如果pop中有Editor的话，focusable必须要为true
        super.setFocusable(true);
        // 设置pop可点击，为false点击事件无效，默认为true
        super.setTouchable(true);
        // 设置点击pop外侧消失，默认为false；在focusable为true时点击外侧始终消失
        super.setOutsideTouchable(true);
        // 相对于 + 号正下面，同时可以设置偏移量
        super.showAsDropDown(iv, -100, 0);
        // 设置pop关闭监听，用于改变背景透明度
        super.setOnDismissListener(this::toggleBright);

        TextView tvScan = view.findViewById(R.id.tv_scan);
        tvScan.setOnClickListener(listener);
        TextView tvConnect = view.findViewById(R.id.tv_connect);
        tvConnect.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.tv_scan){
                if(listeners != null){
                    listeners.onClick(view);
                }
            }else if(view.getId() == R.id.tv_connect){
                if(listeners != null){
                    listeners.onClick(view);
                }
            }
        }
    };

    public void toggleBright() {
        // 三个参数分别为：起始值 结束值 时长，那么整个动画回调过来的值就是从0.5f--1f的
        animUtil.setValueAnimator(START_ALPHA, END_ALPHA, DURATION);
        animUtil.addUpdateListener(progress -> {
            // 此处系统会根据上述三个值，计算每次回调的值是多少，我们根据这个值来改变透明度
            bgAlpha = bright ? progress : (START_ALPHA + END_ALPHA - progress);
            backgroundAlpha(bgAlpha);
        });
        animUtil.addEndListner(animator -> {
            // 在一次动画结束的时候，翻转状态
            bright = !bright;
        });
        animUtil.startAnimator();
    }

    /**
     * 此方法用于改变背景的透明度，从而达到“变暗”的效果
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        // 0.0-1.0
        lp.alpha = bgAlpha;
        activity.getWindow().setAttributes(lp);
        // everything behind this window will be dimmed.
        // 此方法用来设置浮动层，防止部分手机变暗无效
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }
   public interface OnClickListeners{
        void onClick(View view);
    }

    private OnClickListeners listeners;

    public void setListeners(OnClickListeners listeners) {
        this.listeners = listeners;
    }
}

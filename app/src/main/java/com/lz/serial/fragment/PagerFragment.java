package com.lz.serial.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lz.base.log.LogUtils;
import com.lz.serial.R;
import com.lz.serial.fragment.factory.FragmentFactory;
import com.lz.serial.message.event.CDMessageEvent;
import com.lz.serial.message.event.FZMessageEvent;
import com.lz.serial.message.event.JCFMessageEvent;
import com.lz.serial.message.event.JCMessageEvent;
import com.lz.serial.message.event.JFMessageEvent;
import com.lz.serial.message.event.JKMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class PagerFragment extends Fragment {

    private int mFragmentIndex;
    private BaseFragmentImpl fragment;


    public static PagerFragment create(int index) {
        LogUtils.i("创建fragment了");
        PagerFragment pagerFragment = new PagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("key_of_index", index);
        pagerFragment.setArguments(bundle);
        return pagerFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mFragmentIndex = getArguments().getInt("key_of_index");
            LogUtils.i("创建fragment了,onCreate() key_of_index = " + mFragmentIndex);
        } else {
            mFragmentIndex = 0;
        }
    }
    static SparseArray<BaseFragmentImpl> map = new SparseArray<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentFactory.getFragment(mFragmentIndex);


        return inflater.inflate(R.layout.fragment_pager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (ifragmentList != null) {
            for (int i = 0; i < ifragmentList.size(); i++) {
                LogUtils.i("创建布局 mFragmentIndex" + mFragmentIndex + " : " + ifragmentList.size());
                ifragmentList.get(i).addView(getActivity(),mFragmentIndex,view);
            }

        }
    }

    private List<Ifragment> ifragmentList = new ArrayList<>();

    public void addIfragment(Ifragment ifragment) {
        ifragmentList.add(ifragment);
    }

    public void removeIfragment(Ifragment ifragment) {
        ifragmentList.remove(ifragment);
    }


    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        for (int i = 0; i < ifragmentList.size(); i++) {
            ifragmentList.get(i).destroy();
            removeIfragment(ifragmentList.get(i));
        }

        if (fragment != null) {
            fragment = null;
        }

        if (ifragmentList.size() > 0) {
            ifragmentList.clear();
        }


    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onJkMessageEvent(JKMessageEvent messageEvent) {
        LogUtils.i("Jk onJkMessageEvent " + messageEvent.getBean());
        if(fragment == null){
            return;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onJCMessageEvent(JCMessageEvent messageEvent) {
        LogUtils.i("JC onJCMessageEvent " + messageEvent.getJcBean());
        if(fragment == null){
            return;
        }
//        if (fragment instanceof JCFragment) {
//            if (messageEvent.getJcBean() == null) {
//                LogUtils.i("JC exception");
//                return;
//            }
//            ((JCFragment) fragment).setTextView(messageEvent.getJcBean());
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onJFMessageEvent(JFMessageEvent messageEvent) {
        LogUtils.i("JF onJFMessageEvent " + messageEvent.getJfBean());
        if(fragment == null){
            return;
        }
//        if (fragment instanceof JFFragment) {
//            if (messageEvent.getJfBean() == null) {
//                LogUtils.i("Jf exception");
//                return;
//            }
//            ((JFFragment) fragment).setTextView(messageEvent.getJfBean());
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFZMessageEvent(FZMessageEvent messageEvent) {
        LogUtils.i("FZ onFZMessageEvent " + messageEvent.getBean());
        if(fragment == null){
            return;
        }
//        if (fragment instanceof FZFragment) {
//            if (messageEvent.getBean() == null) {
//                LogUtils.i("FZ exception");
//                return;
//            }
//            ((FZFragment) fragment).setTextView(messageEvent.getBean());
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onJCFMessageEvent(JCFMessageEvent messageEvent) {
        LogUtils.i("JCF onJCFMessageEvent " + messageEvent.getBean());
        if(fragment == null){
            return;
        }
//        if (fragment instanceof JCFFragment) {
//            if (messageEvent.getBean() == null) {
//                LogUtils.i("FZ exception");
//                return;
//            }
//            ((JCFFragment) fragment).setTextView(messageEvent.getBean());
//        }
    }

}

package com.lz.serial.fragment.factory;

import android.support.v4.util.SparseArrayCompat;

import com.lz.serial.fragment.CDFragment;
import com.lz.serial.fragment.CFFragment;
import com.lz.serial.fragment.DCFragment;
import com.lz.serial.fragment.FZFragment;
import com.lz.serial.fragment.HHYCFFragment;
import com.lz.serial.fragment.HHYCFragment;
import com.lz.serial.fragment.HHYFFragment;
import com.lz.serial.fragment.JCFFragment;
import com.lz.serial.fragment.JCFragment;
import com.lz.serial.fragment.JFFragment;
import com.lz.serial.fragment.JKFragment;
import com.lz.serial.fragment.RLPGFragment;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/28 下午10:50
 * 描述     :
 */
public class FragmentFactory {

    static SparseArrayCompat<BaseFragment> map = new SparseArrayCompat<>();//

    public static BaseFragment getFragment(int position) {

        BaseFragment fragment = map.get(position);
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new JCFragment();
                    break;
                case 1:
                    fragment = new JKFragment();
                    break;
                case 2:
                    fragment = new CDFragment();
                    break;
                case 3:
                    fragment = new JFFragment();
                    break;
                case 4:
                    fragment = new JCFFragment();
                    break;
                case 5:
                    fragment = new FZFragment();
                    break;
                case 6:
                    fragment = new CFFragment();
                    break;
                case 7:
                    fragment = new RLPGFragment();
                    break;
                case 8:
                    fragment = new DCFragment();
                    break;
                case 9:
                    fragment = new HHYCFragment();
                    break;
                case 10:
                    fragment = new HHYFFragment();
                    break;
                case 11:
                    fragment = new HHYCFFragment();
                    break;
                default:
                    break;
            }
            map.put(position, fragment);
        }

        return fragment;
    }

    // 移除所有
    public static void removeAllFragment() {
        if (map != null && map.size() > 0) {
            map.clear();
        }

    }


    // 拿到列表
    public static SparseArrayCompat<BaseFragment> getTransFragment() {
        if (map != null && map.size() > 0) {
            return map;
        }

        return null;
    }

}

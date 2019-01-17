package com.example.jessie.focusing.utils;

import android.widget.Filter;

import com.example.jessie.focusing.Adapter.AppListAdapter;
import com.example.jessie.focusing.Model.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Yujie Lyu
 * @date : 17-01-2019
 * @time : 12:02
 */
public class SearchFilter extends Filter{
    private final AppListAdapter adapter;

    public SearchFilter(AppListAdapter discoverListAdapter) {
        adapter = discoverListAdapter;
    }



    @Override
    protected Filter.FilterResults performFiltering(CharSequence constraint) {
        String query=constraint.toString().toLowerCase();
        List<AppInfo> appInfoList = adapter.getData();
        List<AppInfo> matchApps = new ArrayList<>();
        int count = appInfoList.size();
        for (int i = 0; i < count; i++) {
            final AppInfo value = appInfoList.get(i);//从List<User>中拿到User对象
//                    final String valueText = value.toString().toLowerCase();
            final String valueText = value.getAppName().toLowerCase();//User对象的name属性作为过滤的参数
            // First match against the whole, non-splitted value
            if (valueText.startsWith(query) || valueText.contains(query)) {//第一个字符是否匹配
                matchApps.add(value);//将这个item加入到数组对象中
            } else {//处理首字符是空格
                final String[] words = valueText.split(" ");
                final int wordCount = words.length;

                // Start at index 0, in case valueText starts with space(s)
                for (int k = 0; k < wordCount; k++) {
                    if (words[k].startsWith(query)) {//一旦找到匹配的就break，跳出for循环
                        matchApps.add(value);
                        break;
                    }
                }
            }
        }
        Filter.FilterResults results = new Filter.FilterResults();
        results.values = matchApps;//此时的results就是过滤后的List<User>数组
        results.count = matchApps.size();

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
        //noinspection unchecked
        adapter.setData((List<AppInfo>) results.values);//此时，Adapter数据源就是过滤后的Results
        if (results.count > 0) {
            adapter.notifyDataSetChanged();//这个相当于从mDatas中删除了一些数据，只是数据的变化，故使用notifyDataSetChanged()
        } else {
            /**
             * 数据容器变化 ----> notifyDataSetInValidated

             容器中的数据变化  ---->  notifyDataSetChanged
             */
            adapter.notifyDataSetInvalidated();//当results.count<=0时，此时数据源就是重新new出来的，说明原始的数据源已经失效了
        }
//        adapter.setTemp(null);
    }
}

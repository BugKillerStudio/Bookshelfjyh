package com.com.jnu.recycleview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    public static class PageViewFragmentAdapter extends FragmentStateAdapter {

         public PageViewFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }          //当滑动Tab时ViewPager2获取相应Fragment

        @NonNull
        @Override
        public Fragment createFragment(int position) {
             switch(position)
             {
                 case 0:
                     return BookFragment.newInstance();//不能先创建一个Fragment的list,然后再返回，要在里面创建
                 case 1:
                     return WebViewFragment.newInstance();
                 case 2:
                     return MapViewFragment.newInstance();
             }
            return BookFragment.newInstance();
        }      //获得Tab对应的Fragment

        @Override
        public int getItemCount() {
            return 3;
        }    //Tab数目
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 viewPager2Main= findViewById(R.id.viewpager2_main);
        viewPager2Main.setAdapter(new PageViewFragmentAdapter(getSupportFragmentManager(), getLifecycle()));

        TabLayout tabLayout=findViewById(R.id.tablayout_header);
        TabLayoutMediator tabLayoutMediator=new TabLayoutMediator(tabLayout, viewPager2Main, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position)
                {
                    case 0:
                        tab.setText(R.string.tab_caption_0_图书);
                        break;
                    case 1:
                        tab.setText(R.string.tab_caption_1_新闻);
                        break;
                    case 2:
                        tab.setText(R.string.tab_caption_2_卖家);
                        break;
                }
            }
        });
        tabLayoutMediator.attach();             //TabLayout与ViewPage2联动关键代码
    }
}


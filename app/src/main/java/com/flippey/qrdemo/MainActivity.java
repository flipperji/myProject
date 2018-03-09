package com.flippey.qrdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements TabRadioGroup.OnCheckedChangeListener {
    private MainActivity mInstance;
    private TabRadioGroup mTabGroup;
    public static final int TAB_HOME = 0;
    public static final int TAB_ME = 1;
    public static final int[] TAB_IDS = {R.id.main_trb_home, R.id.main_trb_me};
    private FrameLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SimpleCaptureActivity.class);
                MainActivity.this.startActivityForResult(i, 2);
            }
        });*/
        mInstance = MainActivity.this;
        initView();
    }

    private void initView() {
        mTabGroup = (TabRadioGroup) findViewById(R.id.main_tab_group);
        mContainer = (FrameLayout) findViewById(R.id.container);
        mTabGroup.setOnCheckedChangeListener(this);
        setFragment(TAB_HOME);
        mTabGroup.check(TAB_IDS[TAB_HOME]);
    }


    private FragmentStatePagerAdapter mFragmentAdapter =
            new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    switch (position) {
                        case TAB_HOME:
                            return HomeFragment.newInstance();
                        case TAB_ME:
                            return MeFragment.newInstance();
                        default:
                            return HomeFragment.newInstance();
                    }
                }

                @Override
                public int getCount() {
                    return TAB_IDS.length;
                }
            };


    private void setFragment(int position) {
        Fragment fragment = (Fragment) mFragmentAdapter.instantiateItem(mContainer, position);
        mFragmentAdapter.setPrimaryItem(mContainer, 0, fragment);
        mFragmentAdapter.finishUpdate(mContainer);
    }


    @Override
    public void onCheckedChanged(TabRadioGroup group, int checkedId) {
        int tab = TAB_HOME;
        switch (checkedId) {
            case R.id.main_trb_home:
                tab = TAB_HOME;
                break;
            case R.id.main_trb_me:
                tab = TAB_ME;
                break;
        }
        setFragment(tab);
    }
}

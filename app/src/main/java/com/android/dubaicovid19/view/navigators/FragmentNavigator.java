package com.android.dubaicovid19.view.navigators;

import java.util.List;

public interface FragmentNavigator {
    void fragmentNavigator(String fragmentTag, Boolean addToBackStack, List<Object> params);
}

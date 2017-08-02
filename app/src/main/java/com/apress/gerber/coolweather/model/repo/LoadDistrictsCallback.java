package com.apress.gerber.coolweather.model.repo;

import java.util.List;

/**
 * 作者：Neil on 2017/8/2 15:28.
 * 邮箱：cn.neillee@gmail.com
 */

public interface LoadDistrictsCallback<T> {
    void onDistrictsLoaded(List<T> a);
}

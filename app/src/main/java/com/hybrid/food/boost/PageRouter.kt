package com.hybrid.food.boost

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.hybrid.food.ui.MainActivity
import com.hybrid.food.ui.TransportMapActivity
import com.idlefish.flutterboost.containers.BoostFlutterActivity

object PageRouter {

    // 应用程序首页
    const val URL_HOME = "sample://home"

    // 设置界面
    const val URL_SYSTEM_SETTINGS = "sample://system_settings"

    // 用户信息界面
    const val URL_USER_INFO = "sample://user_info"

    // 商铺信息界面
    const val URL_STORE_INFO = "sample://store_info"

    // 商品列表界面
    const val URL_PRODUCT_INFO = "sample://product_info"

    // 骑手送货位置信息界面
    const val URL_TRANSPORT_MAP = "sample://transport_map"

    // 搜索界面
    const val URL_SEARCH_INFO = "sample://search_info"

    // 选择收货地址界面
    const val URL_LOCATION_INFO = "sample://location_info"

    //
    const val NATIVE_PAGE_URL = "sample://nativePage"

    //
    const val FLUTTER_FRAGMENT_PAGE_URL = "sample://flutterFragmentPage"

    private val pageName: Map<String, PageHybrid> by lazy {
        val hashMap = HashMap<String, PageHybrid>()
        hashMap[URL_HOME] = PageHybrid("home", "com.hybrid.food.ui.MainActivity")
        hashMap[URL_SYSTEM_SETTINGS] =
            PageHybrid("system_settings", "com.hybrid.food.ui.SystemSettings")
        hashMap[URL_USER_INFO] = PageHybrid("user_info", "com.hybrid.food.ui.UserInfoActivity")
        hashMap[URL_STORE_INFO] = PageHybrid("store_info", "com.hybrid.food.ui.StoreInfoActivity")
        hashMap[URL_PRODUCT_INFO] =
            PageHybrid("product_info", "com.hybrid.food.ui.ProductInfoActivity")
        hashMap[URL_TRANSPORT_MAP] =
            PageHybrid("transport_map", "com.hybrid.food.ui.TransportMapActivity")
        hashMap[URL_SEARCH_INFO] = PageHybrid("search_info", "com.hybrid.food.ui.SearchActivity")
        hashMap[URL_LOCATION_INFO] =
            PageHybrid("location_info", "com.hybrid.food.ui.LocationActivity")
        hashMap
    }

    fun openPageByUrl(context: Context, url: String, params: Map<*, *>): Boolean {
        return openPageByUrl(context, url, params, 0)
    }

    fun openPageByUrl(context: Context, url: String, params: Map<*, *>, requestCode: Int): Boolean {
        val path = url.split("?")[0]
        return try {
            when {
                pageName.containsKey(path) -> {
                    val page = pageName[path] ?: return false
                    val activityClass = Class.forName(
                        page.activityClass,
                        false,
                        this.javaClass.classLoader
                    ) as Class<out BoostFlutterActivity>
                    val intent = BoostFlutterActivity.NewEngineIntentBuilder(activityClass)
                        .url(page.url)
                        .params(params)
                        .backgroundMode(BoostFlutterActivity.BackgroundMode.opaque)
                        .build(context)
                    if (context is Activity) {
                        context.startActivityForResult(intent, requestCode)
                    } else {
                        context.startActivity(intent)
                    }
                    return true
                }
                url.startsWith(FLUTTER_FRAGMENT_PAGE_URL) -> {
//                    context.startActivity(Intent(context, FlutterFragmentPageActivity::class.java))
                    return true
                }
                url.startsWith(NATIVE_PAGE_URL) -> {
                    context.startActivity(Intent(context, TransportMapActivity::class.java))
                    return true
                }
                else -> false
            }
        } catch (t: Throwable) {
            false
        }
    }
}
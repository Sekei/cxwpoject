package com.cxw.cxwproject.http;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;

public class CacheHelper {

	public static Cache.Entry parseCacheHeaders(NetworkResponse response,long catcheTime) {
        long now = System.currentTimeMillis();


        if (catcheTime<=0) {//缓存时间为0不缓存
			return null;
		}
        
        Cache.Entry entry = new Cache.Entry();
        entry.data = response.data;
        entry.etag = null;
        entry.softTtl = 0;
        entry.ttl = now + catcheTime;
        entry.serverDate = now;
        entry.lastModified = 0;
        return entry;
    }
}

package com.gt.magicbox.utils;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by wzb on 2017/11/23 0023.
 */

public class GlideUtils {
    /**
     * glide 不使用磁盘缓存
     */
   private RequestOptions noneCacheOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE);

    private GlideUtils(){

    }

   private static class GlideUtilsHelper{
       private static final GlideUtils INSTANCE=new GlideUtils();
   }

   public static GlideUtils getInstance(){
       return GlideUtilsHelper.INSTANCE;
   }

   public RequestOptions noneCacheOpt(){
       return this.noneCacheOptions;
   }

}

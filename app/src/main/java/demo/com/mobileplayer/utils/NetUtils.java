package demo.com.mobileplayer.utils;

import android.content.Context;
import android.net.TrafficStats;

public class NetUtils {

    private long lastTotalRxBytes = 0;
    private long lastTimeStamp = 0;

    /**
     * 判断是否是网络的资源
     * @param uri
     * @return
     */
    public static boolean isNetUri(String uri) {
        boolean reault = false;
        if (uri != null) {
            if (uri.toLowerCase().startsWith("http")
                    //常用的网络直播协议
                    || uri.toLowerCase().startsWith("rtsp")
                    //流媒体协议
                    || uri.toLowerCase().startsWith("mms"))
            {
                reault = true;
            }
        }
        return reault;
    }


    /**
     * 得到网络速度
     * 每隔两秒调用一次
     * @param context
     * @return
     */
    public String getNetSpeed(Context context) {
        String netSpeed = "0 kb/s";
        //   转为KB;
        long nowTotalRxBytes = TrafficStats.getUidRxBytes(context.getApplicationInfo().uid)==TrafficStats.UNSUPPORTED ? 0 :(TrafficStats.getTotalRxBytes()/1024);
        long nowTimeStamp = System.currentTimeMillis();

        //毫秒转换
        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));

        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;
        netSpeed  = String.valueOf(speed) + " kb/s";
        return  netSpeed;
    }


}

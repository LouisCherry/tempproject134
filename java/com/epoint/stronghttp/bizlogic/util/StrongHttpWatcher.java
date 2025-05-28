package com.epoint.stronghttp.bizlogic.util;

import java.util.Date;
import java.util.Map;

import com.epoint.basic.bizlogic.sysconf.systemparameters.service.FrameConfigService9;
import com.epoint.core.utils.httpclient.watcher.HttpEvent;
import com.epoint.core.watch.AEpointWatchedEvent;
import com.epoint.core.watch.AEpointWatcher;
import com.epoint.core.watch.annotation.EpointWatcher;
import com.epoint.stronghttp.bizlogic.api.StrongHttpCommonKeys;
import com.epoint.stronghttp.bizlogic.api.StrongRestLogApi;

@EpointWatcher(watchEventClass = HttpEvent.class)
public class StrongHttpWatcher extends AEpointWatcher
{

    @Override
    public String process(AEpointWatchedEvent event) {
        FrameConfigService9 configService9 = new FrameConfigService9();
        String useStrong = configService9.getFrameConfigValue(StrongHttpCommonKeys.DISABLE_WATCHER);
        if ("1".equals(useStrong)) {
            return null;
        }
        // http监听事件对象
        HttpEvent edv = (HttpEvent) event;
        // 请求路径
        String url = edv.getUrl();
        // 请求参数
        Object params = edv.getParams();
        // 请求前时间
        Long beforeTime = edv.getBeforeTime();
        // 请求头
        Map<String, String> headerMap = edv.getHeaderMap();
        // 请求方法
        String method = edv.getMethod();
        // 连接异常
        Exception exception = edv.getException();
        // 命名空间
        String namespace = edv.getNameSpace();
        // http请求
        if (HttpEvent.HTTP_EVENT_WATCH_TYPE_HTTPUTIL.equals(edv.getType())) {
            // http调用执行之后
            if (edv.getPhase().equals(HttpEvent.HTTP_EVENT_WATCH_PHASE_AFTER)) {
                // 记录日志
                if (exception == null) {
                    StrongRestLogApi.writeRestLogToDB(url, headerMap, params, method, new Date(beforeTime),
                            edv.getResponse());
                }
                else {
                    StrongRestLogApi.writeRestLogToDB(url, headerMap, params, method, new Date(beforeTime), exception);
                }
            }
        }
        // webservice请求
        else if (HttpEvent.HTTP_EVENT_WATCH_TYPE_WEBSERVICEUTIL.equals(edv.getType())) {
            // http调用执行之后
            if (edv.getPhase().equals(HttpEvent.HTTP_EVENT_WATCH_PHASE_AFTER)) {
                // 记录日志
                if (exception == null) {
                    StrongRestLogApi.writeRestLogToDB(url, namespace, method, (Object[]) params, new Date(beforeTime),
                            edv.getResponse());
                }
                else {
                    StrongRestLogApi.writeRestLogToDB(url, namespace, method, (Object[]) params, new Date(beforeTime),
                            exception);
                }
            }
        }

        return null;
    }
}

package com.sumit.flurryanalytics;

import android.content.Context;
import android.util.Log;
import com.flurry.android.FlurryAgent;
import com.flurry.android.FlurryAgentListener;
import com.flurry.android.FlurryPerformance;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.util.YailDictionary;
import com.google.appinventor.components.runtime.util.YailList;

import java.util.HashMap;
import java.util.Map;

public class FlurryAnalytics extends AndroidNonvisibleComponent implements Component {
    private final Context context;
    private final HashMap<String, String> events = new HashMap<>();

    public FlurryAnalytics(ComponentContainer container) {
        super(container.$form());
        this.context = container.$context();
    }

    @SimpleFunction
    public void Initialize(String apikey) {
        new FlurryAgent.Builder()
                .withIncludeBackgroundSessionsInMetrics(true)
                .withLogLevel(Log.VERBOSE)
                .withPerformanceMetrics(FlurryPerformance.ALL)
                .withListener(new FlurryAgentListener() {
                    @Override
                    public void onSessionStarted() {
                        OnSessionStarted();
                    }
                })
                .build(context, apikey);
    }

    @SimpleFunction
    public boolean IsSessionActive() {
        return FlurryAgent.isSessionActive();
    }

    @SimpleFunction
    public String GetSessionId() {
        return FlurryAgent.getSessionId();
    }

    @SimpleFunction
    public boolean IsInitialized() {
        return FlurryAgent.isInitialized();
    }

    @SimpleEvent
    public void OnSessionStarted() {
        EventDispatcher.dispatchEvent(this, "OnSessionStarted");
    }

    @SimpleProperty
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "False")
    public void ReportLocation(boolean report) {
        FlurryAgent.setReportLocation(report);
    }

    @SimpleFunction
    public void LogEvents(String event, YailDictionary parameters) {
        Map<String, String> map = new HashMap<>();
        for (Object obj : parameters.keySet()) {
            String key = (String) obj;
            map.put(key, (String) parameters.get(obj));
        }

        if (!map.isEmpty())
            FlurryAgent.logEvent(event, map);
    }

    @SimpleFunction
    public void StartTimedEvents(String event, YailDictionary parameters) {
        Map<String, String> map = new HashMap<>();
        for (Object obj : parameters.keySet()) {
            String key = (String) obj;
            map.put(key, (String) parameters.get(obj));
        }

        if (!map.isEmpty())
            FlurryAgent.logEvent(event, map, true);
        this.events.put(event, "NOTHING");
    }

    @SimpleFunction
    public void EndTimedEvent(String event) {
        if (events.containsKey(event)) {
            FlurryAgent.endTimedEvent(event);
            events.remove(event);
        }
    }

    @SimpleFunction
    public YailList GetTimedEvents() {
        if (this.events.isEmpty())
            return YailList.makeEmptyList();
        else
            return YailList.makeList(events.keySet());
    }
}
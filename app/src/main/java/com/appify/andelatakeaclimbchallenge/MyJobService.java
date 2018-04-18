package com.appify.andelatakeaclimbchallenge;


import android.support.v4.app.NotificationCompat;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Yinka Ige on 04/04/2018.
 */

public class MyJobService extends JobService {
    private static final int CHANNEL_ID = 369;
    private String id;
    private String name;

    public MyJobService(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean onStartJob(JobParameters job) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }

    private void buildNotification() {

    }
}

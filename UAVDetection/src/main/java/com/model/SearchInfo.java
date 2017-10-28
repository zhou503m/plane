package com.model;

import java.io.Serializable;

/**
 * Created by taozhiheng on 16-12-6.
 *
 *
 *
 */
public class SearchInfo implements Serializable {

    public String url;

    public String video_id;

    public long time_stamp;

    public boolean special;

    //the dir to store multiple faces
    public String dir;

    public long receiveTime;

    //capture timestamp
    //download timestamp
    //compute timestamp
    //hash timestamp
    //times timestamp
    //notify timestamp
    public long[] intervals = new long[6];

    public long[] candidateIntervals = new long[16];
    public long[] queryINtervals = new long[16];

    //before download
    //after download

    //before getFaces
    //after getFaces

    //before extractFeatures
    //after extractFeatures

    //before queryHashcode
    //after queryHashcode

    //before getCandidates
    //after getCandidates

    //before queryFeatures
    //after queryFeatures

    //before getSamePerson
    //after getSamePerson

    //before getTimes
    //after getTimes

    //before notify
    //after notify
    public long[] timestamps = new long[18];


    public SearchInfo()
    {

    }


    public SearchInfo(String url, long time_stamp) {
        this.url = url;
        this.time_stamp = time_stamp;
    }

/*    public void setUrl(String url) {
        this.url = url;
    }

    public void setTime_stamp(String time_stamp){
        this.time_stamp = time_stamp;
    }*/

    public SearchInfo(String url, String video_id, long time_stamp, boolean special,
                      long receiveTime, long[] intervals, long[] timestamps)
    {
        this.url = url;
        this.video_id = video_id;
        this.time_stamp = time_stamp;
        this.special = special;
        this.receiveTime = receiveTime;
        if(intervals != null) {
            for(int i = 0; i < intervals.length; i++)
            {
                this.intervals[i] = intervals[i];
            }
        }
        if(timestamps != null)
        {
            for(int i = 0; i < timestamps.length; i++)
            {
                this.timestamps[i] = timestamps[i];
            }
        }
    }

    public SearchInfo copy()
    {
        return new SearchInfo(url, video_id, time_stamp, special, receiveTime, intervals, timestamps);
    }
}

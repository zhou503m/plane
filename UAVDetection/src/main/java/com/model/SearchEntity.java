package com.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by taozhiheng on 16-12-6.
 */
public class SearchEntity implements Serializable {

    @Expose
    public SearchInfo info;

    public float[] feature;

    public byte[] hash;

    public byte[] face;

    public int index;

    public String id;

    public boolean publish = false;

    public String blackUrl;
    public float similarity;

    public SearchEntity()
    {

    }


    public SearchEntity(SearchInfo info, float[] feature)
    {
        this.info = info;
        this.feature = feature;
    }

    public SearchEntity(SearchInfo info, float[] feature, byte[] hash)
    {
        this.info = info;
        this.feature = feature;
        this.hash = hash;
    }

    public SearchEntity(SearchInfo info, float[] feature, byte[] hash, byte[] face, int index)
    {
        this.info = info;
        this.feature = feature;
        this.hash = hash;
        this.face = face;
        this.index = index;
    }

    public SearchEntity(SearchInfo info, float[] feature, byte[] hash, byte[] face, int index, String id)
    {
        this.info = info;
        this.feature = feature;
        this.hash = hash;
        this.face = face;
        this.index = index;
        this.id = id;
    }
}

package com.model;

import java.io.Serializable;

/**
 * Created by WangRupeng on 2017/7/6 0006.
 */
public class FaceSearchFeature implements Serializable {
    private static final long serialVersionUID = -5960951379939233137L;
    public BBox bbox;
    public float score; // confidence
    public float[] feature;
    public String blackTableFaceUrl = "";
    public float similarity = 0;
    public String name = "";

    public FaceSearchFeature(float[] arr) {
        bbox = new BBox(subArray(arr, 0, 4));
        score = arr[4];
        feature = subArray(arr, 5, arr.length);
    }

    public static float[] subArray(float[] src, int begin, int end) {
        float[] dest = new float[end-begin];
        System.arraycopy(src, begin, dest, 0, dest.length);
        return dest;
    }

    public class Point implements Serializable {
        private static final long serialVersionUID = 4945088763170045194L;
        public int x;
        public int y;
        public Point(float x, float y) {
            this.x = Math.round(x);
            this.y = Math.round(y);
        }
    }

    public class BBox implements Serializable {
        private static final long serialVersionUID = -7865805706288242922L;
        public Point left_top;
        public Point right_down;

        public BBox(float[] arr) {
            left_top   = new Point(arr[0], arr[1]);
            right_down = new Point(arr[2], arr[3]);
        }
    }
}

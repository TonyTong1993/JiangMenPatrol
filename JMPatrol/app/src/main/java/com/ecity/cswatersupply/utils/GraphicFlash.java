package com.ecity.cswatersupply.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.os.Message;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.map.Graphic;

public class GraphicFlash {
    private boolean replace = false;
    private int uid = 1;
    private Graphic oldGraphic = null;
    private Graphic newGraphic = null;
    private GraphicsLayer animationLayer = null;

    public GraphicFlash(Graphic oldGraphic, Graphic newGraphic, GraphicsLayer animationLayer) {
        this.oldGraphic = oldGraphic;
        this.newGraphic = newGraphic;
        this.animationLayer = animationLayer;
        this.animationLayer.removeAll();
        this.animationLayer.addGraphic(oldGraphic);
    }

    /**
     * 开始闪烁
     */
    public void startFlash() {
        if (newGraphic == null || oldGraphic == null || animationLayer == null) {
            return;
        }
        animationLayer.removeAll();
        timer.schedule(task, 0, 500);
    }

    /**
     * 开始闪烁
     * @param millisecond 闪烁频率（毫秒）
     */
    public void startFlash(int millisecond) {
        if (newGraphic == null || oldGraphic == null || animationLayer == null)
            return;
        timer.schedule(task, 0, millisecond);
    }

    public void stopFlash() {
        if (animationLayer != null) {
            animationLayer.removeAll();
        }
        if (timer != null) {
            try {
                timer.cancel();
            } catch (Exception e) {
            }
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                replace = !replace;
                try {
                    for (int i : animationLayer.getGraphicIDs()) {
                        uid = i;
                    }
                    if (replace) {
                        animationLayer.updateGraphic(uid, newGraphic);
                    } else {
                        animationLayer.updateGraphic(uid, oldGraphic);
                    }
                } catch (Exception e) {
                    stopFlash();
                    e.printStackTrace();
                }
            }
        }
    };
    private Timer timer = new Timer(true);
    private TimerTask task = new TimerTask() {
        public void run() {
            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);
        }
    };
}
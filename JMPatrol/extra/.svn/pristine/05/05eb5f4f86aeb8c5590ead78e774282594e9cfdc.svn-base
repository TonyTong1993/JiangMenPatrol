package com.ecity.z3map.maploader.model;

/**
 * Created by zhengzhuanzi on 2017/6/17.
 */

public class MapScaleValue {
    final double INCH_CM = 0.0254000508;
    final double NAN = 1 * 10e100;
    int[] scaleValuesNum;  //比例尺数值
    String[] scaleValuesStr; //比例尺字符串
    double _resolutionToScale; //设置从分辨率转为比例尺的参数
    int _scaleIndex;
    int _dpi;
    int _mapLen;
    double _lenth;                  // 屏幕上的长度

    // 构造函数初始化
    public MapScaleValue() {
        if (this != null) {
            scaleValuesNum = new int[]{5, 10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000, 25000, 50000, 100000, 250000, 500000, 1000000};
            //scaleValuesNum ={"5","10","20","50","100","200","500","1000","2000","5000","10000","25000","50000","100000","250000","500000","1000000"};
            scaleValuesStr = new String[]{"5米", "10米", "20米", "50米", "100米", "200米", "500米", "1公里", "2公里", "5公里", "10公里", "25公里", "50公里", "100公里", "250公里", "500公里", "1000公里"};    //比例尺字符串
        }
        _resolutionToScale = 0.f;
    }

    public void setParameterOfResolutionToScaleWithResolution(double resolution, double mapScale, int dpi) {
        _resolutionToScale = (mapScale * INCH_CM / _dpi) / resolution;
    }

    public double getScaleBarLenthWithResolution(double resolution, int wkid) {
        double scale = 0.f;
        scale = resolution * _resolutionToScale / (INCH_CM / _dpi);
        if (scale == NAN) {
            scale = 0.f;
        } else {//长度控制有问题
            //_scaleIndex = [self getScaleIndexByScale:scale];
            //_lenth = _mapLen/(scale*INCH_CM/_dpi);
            double a = resolution * 120;
            if (wkid == 4326) {
                a = a * 111111;
            }
            //double b = resolution*120;
            _scaleIndex = getScaleIndexByScale(a);
            _lenth = _mapLen / resolution;
            if ((_mapLen / resolution) > 150 && _scaleIndex > 0) {
                _scaleIndex--;
                if (wkid == 4326) {
                    _lenth = scaleValuesNum[_scaleIndex] / resolution / 111111;
                } else {
                    _lenth = scaleValuesNum[_scaleIndex] / resolution;
                }
            }
        }
        return _lenth;
    }

    public double getScaleBarLenth() {
        return _lenth;
    }

    public int getScaleIndexByScale(double scale) {
        int index = 0;
        int tmp = 0;
        int tmp2 = 0;
        for (int i = 0; i < scaleValuesNum.length - 1; i++) {
            tmp = scaleValuesNum[i];
            tmp2 = scaleValuesNum[i + 1];
            if (scale >= tmp && (int) scale < tmp2) {
                index = i + 1;
                _mapLen = tmp2;

            }
        }
        return scaleValuesNum.length - 1;
    }

    public int getScaleByIndex(int index) {
        if (index >= scaleValuesNum.length) {
            return scaleValuesNum[scaleValuesNum.length - 1];
        }

        return scaleValuesNum[index];
    }

    public int getScaleByMapScale(double scale) {
        return getScaleByIndex(getScaleIndexByScale(scale));
    }

    public String getScaleBarString() {
        String str = null;
        if (_scaleIndex < scaleValuesStr.length) {
            str = scaleValuesStr[_scaleIndex];
        }
        return str;
    }

    public int getScaleValue() {
        int scaleValue = 0;
        if (_scaleIndex < scaleValuesNum.length) {
            scaleValue = scaleValuesNum[_scaleIndex];
        }
        return scaleValue;
    }
}

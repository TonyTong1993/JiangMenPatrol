package com.ecity.android.map.core.dbquery;

import com.esri.core.map.Graphic;

public class ECityGraphic {

    private Graphic graphic;
    private int     dno;
    
    public ECityGraphic(){
        
    }
    
    public ECityGraphic(int dno,Graphic graphic){
        this();
        this.dno = dno;
        this.graphic = graphic;
    }
    public int getDno() {
        return dno;
    }
    public void setDno(int dno) {
        this.dno = dno;
    }
    public Graphic getGraphic() {
        return graphic;
    }
    public void setGraphic(Graphic graphic) {
        this.graphic = graphic;
    }
    
    
}

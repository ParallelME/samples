package br.ufmg.dcc.tonemapreinhard;

import android.graphics.Bitmap;

import br.ufmg.dcc.tonemapreinhard.formats.RGBE;

/**
 * Created by renatoutsch on 6/4/15.
 */
public interface ReinhardOperator {
    public void runOp(RGBE.ResourceData resourceData, float key, float power, Bitmap bitmap);
    public void waitFinish();
}

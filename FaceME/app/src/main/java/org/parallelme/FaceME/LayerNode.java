package org.parallelme.FaceME;

import org.parallelme.userlibrary.datatypes.Float32;
import org.parallelme.userlibrary.datatypes.NumericalData;

/**
 * This class was created to wrap the neural network layer's node weight. Other node attributes might be added later.
 * @author AlbertoSCA
 * @since 09/12/2015.
 */

public class LayerNode extends NumericalData {
    private Float32 weight;

    /**
     * @param _w: node's weight
     */
    public LayerNode(Float32 _w){
        weight = _w;
    }

    public Float32 getWeight() {
        return weight;
    }

    /**
     *
     * @param _w: node's weight
     */
    public void setWeight(Float32 _w){
        weight = _w;
    }
}

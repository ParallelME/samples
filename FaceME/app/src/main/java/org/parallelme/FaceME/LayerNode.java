package org.parallelme.FaceME;

/**
 * This class was created to wrap the neural network layer's node weight. Other node attributes might be added later.
 * @author AlbertoSCA
 * @since 09/12/2015.
 */

public class LayerNode {
    private float weight;

    /**
     * @param _w: node's weight
     */
    public LayerNode(float _w){
        weight = _w;
    }

    public float getWeight() {
        return weight;
    }

    /**
     *
     * @param _w: node's weight
     */
    public void setWeight(float _w){
        weight = _w;
    }
}

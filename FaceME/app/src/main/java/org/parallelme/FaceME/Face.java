package org.parallelme.FaceME;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

import java.util.Random;

/**
 * Created by Leandro on 02/12/2015.
 */
public class Face {
    public Rect trackedFace;
    public Rect faceRoi;
    public Mat faceImg;
    public Mat faceTemplate;
    public Mat matchingResult;
    public Point facePosition;
    public long id;
    private Scalar Color;
    public int faceTemplateNotFoundCount;
    public int faceHaarNotFoundCount;
    private String nome;
    public int recognitionStatus;

    public Face() {
        Random random = new Random();
//        setColor(new Scalar(random.nextInt(255), random.nextInt(255), random.nextInt(255), 255));
        setColor(new Scalar(255,0,0,255));
        faceTemplateNotFoundCount = 0;
        faceHaarNotFoundCount = 0;
        recognitionStatus = Parameters.rsNotRecognized;
        nome = new String();
    }

    public Scalar getColor() {
        return Color;
    }

    public void setColor(Scalar color) {
        Color = color;
    }

    public String getNome() {
        String s;
        if (nome.isEmpty()) {
            s = "Unknown " + id;
        } else {
            s = nome;
        }
        return s;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}

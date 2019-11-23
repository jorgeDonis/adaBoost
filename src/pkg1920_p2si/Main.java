/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1920_p2si;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import adaBoost.DataInput;
import adaBoost.HardClassifier;
import mnist_predict.MNISTPredictor;
import twoD.Constants;
import twoD.Generator2D;
import twoD.InputVisualizer;
import twoD.OutputVisualizer;

/**
 *
 * @author fidel
 */
public class Main {
    
    
//    public static int Byte2Unsigned(byte b) {
//        return b & 0xFF;
//    }
    /**
     * @param args the command line arguments
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        /*documentación para un futuro
        //Cargador MNIST de SI
        MNISTLoader ml = new MNISTLoader();
        ml.loadDBFromPath("./mnist_1000");
        
        //Accedo a las imagenes de dígito 1
        ArrayList d0imgs = ml.getImageDatabaseForDigit(1);
        
        //Y cojo la tercera imagen del dígito 1
        Imagen img = (Imagen) d0imgs.get(10);
        
        //La invierto para ilustrar como acceder a los pixels
        byte imageData[] = img.getImageData();
        for (int i = 0; i < imageData.length; i++){
            
            imageData[i] = (byte) (255 - imageData[i]);
            System.out.print(Byte2Unsigned(imageData[i])+ ",");
        }
        
        //Muestro la imagen invertida
        MostrarImagen imgShow = new MostrarImagen();
        imgShow.setImage(img);
        imgShow.mostrar();
		*/
    	
    	DataInput X = new DataInput(0, 20);
    	HardClassifier classifier = new HardClassifier(X);
    	MNISTPredictor predictor = new MNISTPredictor(classifier);
    	predictor.testImage(4, 10);
    }
    
}

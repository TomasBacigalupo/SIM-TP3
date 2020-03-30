package ar.edu.itba.sis;

import ar.edu.itba.sis.model.Board;

import java.io.*;
import java.util.Properties;

public class Main{
    public static void main( String[] args ) throws IOException {
        
    	int N;
        double L;
        double Tmin;
        double Tmax;
        double R1;
        double M1;
        double R2;
        double M2;
        double V;
        
        //set variables from config.properties
        
        InputStream input = new FileInputStream("src/resources/config.properties");
        Properties prop = new Properties();
        prop.load(input);
        
        N = new Integer(prop.getProperty("sim.N"));
        L = new Double(prop.getProperty("sim.L"));
        Tmin = new Double(prop.getProperty("sim.Tmin"));
        Tmax = new Double(prop.getProperty("sim.Tmax"));
        R1 = new Double(prop.getProperty("sim.R1"));
        M1 = new Double(prop.getProperty("sim.M1"));
        R2 = new Double(prop.getProperty("sim.R2"));
        M2 = new Double(prop.getProperty("sim.M2"));
        V = new Double(prop.getProperty("sim.V0MAX"));

        Board board = new Board(N,L,Tmin,Tmax,R1,M1,R2,M2,V);
        for (int i = 1; i < 5; i++) {
            completeSimulation(new Board(N,L,Tmin*i,Tmax*i,R1,M1,R2,M2,V*i),""+5*i);
        }





                
    }

    public static void completeSimulation(Board board,String path) throws IOException{
        StringBuilder simulacion = new StringBuilder();
        
        StringBuilder v_modules = new StringBuilder();
        double t = board.temperature();

        
        board.setMax_v_module(0);
        
        v_modules.append("t=" + 0);
        v_modules.append("\n");
        v_modules.append(board.get_modules());
        
        simulacion.append(board.toOvito());
        Animation.WallCrash = 0;
        Animation.Crash = 0;
        Animation.time = 0;
        Animation.colisionTimes.clear();
        Animation.modules.clear();
        Animation.boardModules.clear();

        double tc;
        double clock = 1; // set the clock at one second n*t
        double n = 0;
        
        while(!board.end()){
            tc = board.tc();


            Animation.modules.add(board.velocity());
            Animation.boardModules.add(board.getListModules());
            Animation.colisionTimes.add(tc);
            Animation.time+= tc;
            board.update(tc);

            if(board.getLastCrachA()!= -1){
                board.collision();
            }

            if(Animation.time > clock*n) {
            	//event ...
            	Animation.modules.add(board.velocity());
                simulacion.append(board.toOvito());
            	n++;
            }
        }
        
        System.out.println(Animation.Crash);
        System.out.println(Animation.WallCrash);
        System.out.println(Animation.time);

        FileWriter fw = new FileWriter("ovito"+path+".txt");
        fw.write(simulacion.toString());
        fw.close();

        FileWriter fw2 = new FileWriter("dataTCAcum"+path+".txt");
        double tim= 0;
        for (Double d: Animation.colisionTimes) {

            fw2.write(tim+"\n");
            tim+=d;
        }
        fw2.close();


        FileWriter fw3 = new FileWriter("dataTC"+path+".txt");
        for (Double d: Animation.colisionTimes) {

            fw3.write(d+"\n");
        }
        fw3.close();
        
        FileWriter fw4 = new FileWriter("|v|"+path+".txt");
        double times = 0;
        for (int i = 0; i <Animation.colisionTimes.size() ; i++) {
            times += Animation.colisionTimes.get(i);

            if(times > Animation.time*2/3) {
                for (Double m : Animation.boardModules.get(i)) {
                    fw4.write(m + "\n");
                }
            }

            if(times > Animation.time*2/3){
                fw4.write(Animation.modules.get(i)+ " "+ times+ "\n");

            }
        }

        fw4.close();

        FileWriter fw5 = new FileWriter("|vinicial|"+path+".txt");
        for (Double m : Animation.boardModules.get(0)) {
            fw5.write(m+"\n");
            System.out.println(m);
        }
        fw5.close();
    }

}




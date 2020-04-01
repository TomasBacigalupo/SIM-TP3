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
        double clock;
        
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
        clock = new Double(prop.getProperty("sim.Clock"));

        Board board = new Board(N,L,Tmin,Tmax,R1,M1,R2,M2,V);
        for (int i = 1; i < 5; i++) {
            completeSimulation(new Board(N,L,Tmin*i,Tmax*i,R1,M1,R2,M2,V*i),""+5*i,clock);
        }


//        for (int i = 0; i <10; i++) {
//            Board board2 = new Board(N,L,Tmin*5,Tmax*5,R1,M1,R2,M2,V);
//
//            Animation.bpPositionEvents.clear();
//
//            BPSimulation(board2);
//
//            FileWriter fwn = new FileWriter("D"+i+".txt");
//            //todo poner bien el start
////            Vector start = new Vector(Animation.bpPositionEvents.get(Animation.bpPositionEvents.size()/2)[0],
////                    Animation.bpPositionEvents.get(Animation.bpPositionEvents.size()/2)[1]);
//            Vector start = new Vector(0.25,0.25);
//            for (int j = Animation.bpPositionEvents.size()/2; j < Animation.bpPositionEvents.size(); j++) {
//                Vector v = new Vector(Animation.bpPositionEvents.get(j)[0],Animation.bpPositionEvents.get(j)[1]);
//                fwn.write(""+v.getCuadratic(start)+"\n");
//            }
//            fwn.close();
//
//        }

        for (int i = 0; i < 10; i++) {
            completeSimulation(new Board(N,L,Tmin,Tmax,R1,M1,R2,M2,V),"D"+i,10);
        }





                
    }


    public static void completeSimulation(Board board,String path,double clock) throws IOException{
        StringBuilder simulacion = new StringBuilder();
        
        StringBuilder v_modules = new StringBuilder();
        double t = board.temperature();


        
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

        FileWriter fw5 = new FileWriter("|v0|"+path+".txt");
        for (Double m : Animation.boardModules.get(0)) {
            fw5.write(m+"\n");
        }
        fw5.close();

    }
}




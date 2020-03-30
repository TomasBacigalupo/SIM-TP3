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
            completeSimulation(new Board(N,L,Tmin*i,Tmax*i,R1,M1,R2,M2,V),""+5*i);
        }





                
    }

    public static void completeSimulation(Board board,String path) throws IOException{
        StringBuilder simulacion = new StringBuilder();
        
        StringBuilder v_modules = new StringBuilder();
        
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
        double time_elapsed = 0;
        int steps = 0;

        while(!board.end()){
            tc = board.tc();
            time_elapsed += tc;
            Animation.modules.add(board.velocity());
            Animation.boardModules.add(board.getListModules());

            v_modules.append("t=" + time_elapsed);
            v_modules.append("\n");
            v_modules.append(board.get_modules());
            
            Animation.colisionTimes.add(tc);
            Animation.time+= tc;
            board.update(tc);
            if(board.getLastCrachA()!= -1){
                board.collision();

            }
            simulacion.append(board.toOvito());
            steps++;
        }

        System.out.println(Animation.Crash);
        System.out.println(Animation.WallCrash);
        System.out.println(Animation.time);
        System.out.println("steps*N="+board.getParticles().size()*steps);

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
        
        FileWriter fw4 = new FileWriter("velocityModules"+path+".txt");
        fw4.write(board.getMax_v_module()+"\n");
        fw4.write(time_elapsed+"\n");
        fw4.write(v_modules+"\n");
        fw4.close();

        FileWriter fw5 = new FileWriter("|v|"+path+".txt");
        double times = 0;
        for (int i = 0; i <Animation.colisionTimes.size() ; i++) {
            times += Animation.colisionTimes.get(i);
            if(times > tim*2/3){
                for (Double m : Animation.boardModules.get(i)) {
                    fw5.write(m+"\n");
                }
            }

        }
        fw5.close();

        FileWriter fw6 = new FileWriter("|vinicial|"+path+".txt");
        for (Double m : Animation.boardModules.get(0)) {
            fw6.write(m+"\n");
            System.out.println(m);
        }
        fw6.close();
        
    }

}




package MPP;

import MPP.VNS.BVNS;
import MPP.algorithm.AlgProcedure;
import MPP.algorithm.IAlgorithm;
import MPP.constructive.GreedyConstructive;
import MPP.constructive.GreedyDestructive;
import MPP.localSearch.*;
import MPP.structure.Instance;
import MPP.structure.RandomManager;
import MPP.structure.Result;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    static String pathFolder = "./instances";
    static ArrayList<Instance> instances;

    static boolean readAllInstances = true;

    static String instanceIndex = "delaunay_n10.txt";

    static List<String> instancesNames;
    static String instanceFolderPath;

    static IAlgorithm algorithm;
    static BVNS bvns;
    static GreedyDestructive destructive=new GreedyDestructive();
    static GreedyConstructive constructive=new GreedyConstructive();

    static ILocalSearch localSearch;
    static ILocalSearch localSearchEfficient;

    static int numberExecutions=1;

    public static long time=1400;

    static boolean otherFolder=false;
    static String path;

    public static void main(String[] args) {
        instances = new ArrayList<>();

        if(args.length==1){
            path=args[0];
            otherFolder=true;
            pathFolder=path.substring(0,path.lastIndexOf("/"));
            instanceIndex=path.substring(path.lastIndexOf("/")+1);
            readAllInstances=false;
        }
        if(args.length==2){
            path=args[0];
            otherFolder=true;
            pathFolder=path.substring(0,path.lastIndexOf("/"));
            instanceIndex=path.substring(path.lastIndexOf("/")+1);
            time =Long.parseLong(args[1]);
            readAllInstances=false;
        }

        try {
            PrintWriter pw = new PrintWriter(new FileWriter("variasejecuciones.csv"));
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        localSearchEfficient=new ILS();
        bvns=new BVNS(localSearchEfficient);

        algorithm=new AlgProcedure(destructive,bvns);
        execute();

    }

    private static void execute() {
        File file=new File(pathFolder);
        instanceFolderPath = file.getPath() + "/";
        printHeaders("./results/"+algorithm.toString()+".csv");
        readFolder();
    }

    private static void printHeaders(String path) {
        try (PrintWriter pw = new PrintWriter(path)) {
            pw.print("Instance;Time;OF");
            pw.println();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printResults(String path, Result result, String name) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path,true))) {

            pw.print(name+";");
            int nElems=result.getKeys().size();
            for (int i = 0; i < nElems; i++) {
                pw.print(result.get(i));
                if (i < nElems-1) pw.print(";");
            }
            pw.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readFolder(){
        File file;
        file=new File(pathFolder);

        instancesNames = Arrays.asList(file.list());
        instanceFolderPath = file.getPath() + "/";
        if(readAllInstances) readAllInstances();
        else if (instancesNames.contains(instanceIndex) || otherFolder) readInstance(instanceIndex);
        else System.out.println("Instance index exceeds the bounds of the array");


    }

    private static void readAllInstances(){
        for(String instanceName : instancesNames){
            if(!instanceName.startsWith(".") && !instanceName.startsWith(".."))
                readInstance(instanceName);
        }
    }

    private static void readInstance(String instanceName){
        System.out.println(instanceName);
        if(!otherFolder){
            path=instanceFolderPath+instanceName;
        }

        Instance MCInstance =new Instance(path);
        RandomManager.setSeed(13);

        int avgOfValue=0;
        float avgTime=0;

        try {
            PrintWriter pw = new PrintWriter(new FileWriter("variasejecuciones.csv", true));
            pw.print(instanceName+";");
            for(int i=0;i<numberExecutions; i++){
                Result result= algorithm.execute(MCInstance);
                avgTime+=result.get(0);
                avgOfValue+=result.get(1);
                pw.print(result.get(1)+";");
            }
            pw.println();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Result avgResult=new Result(instanceName);
        avgResult.add("Time",avgTime/numberExecutions);
        avgResult.add("OF",avgOfValue*1.0f/numberExecutions);

        printResults("./results/"+algorithm.toString()+".csv", avgResult, instanceName);
    }

}

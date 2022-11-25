package MPP.structure;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Instance {

    private String path;
    private String name;
    private byte[][] adjacencyMatrix;
    private List<Integer>[] adjacencyList;

    private Set<Integer> leavesNodes;
    private Set<Integer> unSelectedNodes;
    private Set<Integer> supportNodes;

    private int numNodes;
    private int numEdges;

    private int totalWeigth=0;
    private final boolean supportAndLeaveNodes=true;

    public Instance(String path){
        this.path = path;
        this.name = path.substring(Math.max(path.lastIndexOf("/"), path.lastIndexOf("\\"))+1).replace(".txt", "");
        readInstance();
    }

    public Instance(int numNodes, List<Integer>[] adyacencyList, Set<Integer> unSelectedNodes){
        this.numNodes=numNodes;
        this.adjacencyList =adyacencyList;
        this.unSelectedNodes=new HashSet<>(unSelectedNodes);
        setSupportNodes();
    }

    private void readInstance(){
        try (BufferedReader br = new BufferedReader(new FileReader(path))){
            String line;
            String[] lineContent;

            line= br.readLine();
            lineContent = line.split("\\s");

            numNodes = Integer.parseInt(lineContent[0]);
            numEdges = Integer.parseInt(lineContent[1]);
            unSelectedNodes=new HashSet<>(numNodes);

            adjacencyMatrix = new byte [numNodes][numNodes];
            adjacencyList = new ArrayList[numNodes];

            for(int i=0; i<numNodes;i++){
                adjacencyList[i]=new ArrayList<>();
                unSelectedNodes.add(i);
            }

            for (int i = 0; i< numEdges; i++){
                line = br.readLine();
                lineContent=line.split("\\s");

                int node1=Integer.parseInt(lineContent[0]);
                int node2=Integer.parseInt(lineContent[1]);
                int peso=Integer.parseInt(lineContent[2]);

                totalWeigth+=peso;
                adjacencyList[node1].add(node2);
                adjacencyList[node2].add(node1);
                adjacencyMatrix[node1][node2]=(byte)peso;
                adjacencyMatrix[node2][node1]=(byte)peso;
            }

        } catch (FileNotFoundException e){
            System.out.println(("File not found " + path));
        } catch (IOException e){
            System.out.println("Error reading line");
        }
        supportNodes=new HashSet<>();
        leavesNodes=new HashSet<>();
        if(supportAndLeaveNodes) setSupportNodes();
    }

    private void setSupportNodes(){
        for (int i =0;i< numNodes;i++){
            if(adjacencyList[i].size()==0){
                supportNodes.add(i);
                continue;
            }
            int neighbour=adjacencyList[i].get(0);
            if(adjacencyList[i].size()==1 && !leavesNodes.contains(neighbour)){
                leavesNodes.add(i);
                supportNodes.add(neighbour);
                unSelectedNodes.remove(i);
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public List<Integer>[] getAdjacencyList() {
        return adjacencyList;
    }

    public int getNumNodes() {
        return numNodes;
    }

    public int getNumEdges() {
        return numEdges;
    }

    public byte[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public Set<Integer> getLeavesNodes() {
        return leavesNodes;
    }

    public Set<Integer> getUnSelectedNodes() {
        return unSelectedNodes;
    }

    public Set<Integer> getSupportNodes() {
        return supportNodes;
    }

    public int getTotalWeight() {
        return totalWeigth;
    }
    
    public boolean isConnected() {
        Set<Integer> visited = new HashSet<>();
        Deque<Integer> next = new LinkedList<>();
        next.add(0);
        while (!next.isEmpty()) {
            int v = next.remove();
            for (int adj : adjacencyList[v]) {
                if (!visited.contains(adj)) {
                    visited.add(adj);
                    next.add(adj);
                }
            }
        }
        return visited.size() == numNodes;
    }
}


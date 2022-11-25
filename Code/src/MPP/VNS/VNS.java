package MPP.VNS;

import MPP.Main;
import MPP.localSearch.ILocalSearch;
import MPP.structure.Instance;
import MPP.structure.RandomManager;
import MPP.structure.Solution;

import java.text.CollationElementIterator;
import java.util.*;

public abstract class VNS{

    protected int kMaxPCT=20;//20
    protected int kStepPCT=10;//10
    protected int iterations=20;//20

    String shakeName="shakeRemoveSmart";//"shakeRemoveSmart"
    boolean smartShake=true;//true

    public Solution execute(Solution solution, Instance instance, long time) {

        int kMax= (int) (Math.ceil(kMaxPCT*solution.getSelectedNotSupportNodes().size())/100);
        int kStep= (int) (Math.ceil(kStepPCT*solution.getSelectedNotSupportNodes().size())/100);

        int bestOF=solution.evaluate();
        Solution bestSol=new Solution(instance);
        bestSol.copy(solution);

        for(int i=0; i<iterations && (System.currentTimeMillis()-time)/ 1000f<Main.time; i++){
            int k=1;
            while (k<kMax && (System.currentTimeMillis()-time)/ 1000f<Main.time)
            {
                procedure(k,solution,instance);
                if(solution.evaluate()<bestOF){
                    k=1;
                    bestOF=solution.evaluate();
                    bestSol.copy(solution);
                }
                else{
                    k+=kStep;
                }
            }
        }

        return bestSol;
    }

    protected void procedure(int k, Solution solution,Instance instance){

    }

    protected void shakeExchange(int k, Solution sol, Instance instance){
        List<Integer> selectedNotSupportList=new ArrayList<Integer>(sol.getSelectedNotSupportNodes());
        Collections.shuffle(selectedNotSupportList,RandomManager.getRandom());
        List<Integer> unselectedList=new ArrayList<Integer>(sol.getUnselectedNodes());
        Collections.shuffle(unselectedList,RandomManager.getRandom());

        shakeName="shakeExchange";
        for(int i=0; i<k;i++){
            sol.removeNode(selectedNotSupportList.get(i));
            sol.addNode(unselectedList.get(i));
        }
    }

    protected void shakeExchangeNodesOfSameEdge(int k, Solution sol, Instance instance){

        String mode="";
        boolean random=true;

        List<Integer> selectedNotSupportList=new ArrayList<Integer>(sol.getSelectedNotSupportNodes());
        Collections.shuffle(selectedNotSupportList,RandomManager.getRandom());

        for(int i = 0; i < k; i++){
            int removeNode=selectedNotSupportList.get(i);
            sol.removeNode(removeNode);
            int addNode;
            if(random){
                mode="Random";
                randomNeighbour(sol,instance,removeNode);
            }else{
                mode="Best";
                addNode=bestNeighbour(removeNode,instance,sol);
            }
            //if(addNode!=-1) sol.addNode(addNode);
        }
        shakeName="shakeExchangeNodesOfSameEdge"+mode;
    }

    protected int bestNeighbour(int node, Instance instance, Solution sol){
        int maxHeigh=0;
        int bestNode=-1;
        for(int i=0; i<instance.getAdjacencyList()[node].size();i++){
            int n=instance.getAdjacencyList()[node].get(i);
            if(!sol.getUnselectedNodes().contains(n)) continue;
            if(maxHeigh<instance.getAdjacencyMatrix()[node][n]){
                maxHeigh=instance.getAdjacencyMatrix()[node][n];
                bestNode=n;
            }
        }
        return bestNode;
    }

    protected void randomNeighbour(Solution solution, Instance instance, int removeNode){
        List<Integer> adjacencyListCopy=new ArrayList<>(instance.getAdjacencyList()[removeNode]);
        Collections.shuffle(adjacencyListCopy,RandomManager.getRandom());
        int i=0;
        while(solution.getPenalty()>0 && i<adjacencyListCopy.size()){
            boolean add=true;
            int node=adjacencyListCopy.get(i);
            if (solution.getSelectedNodes().contains(node)){
                add=false;
            }
            if(add){
                solution.addNode(node);
            }
            i++;
        }
    }

    protected void shakeRemove(int k, Solution sol){
        shakeName="shakeRemove"+(smartShake?"Smart":"Random");
        List<Integer> selectedNotSupportCopy=new ArrayList<>(sol.getSelectedNotSupportNodes());
        Collections.shuffle(selectedNotSupportCopy,RandomManager.getRandom());
        for(int i=0; i<k;i++){
            int removeNode=selectedNotSupportCopy.get(i);
            sol.removeNode(removeNode);
        }

        //busqueda local que reconstruye
        int node;
        while (sol.getPenalty()!=0){
            if(smartShake) node=sol.getBestNextNode();
            else node=getRandomNextSelected(sol);

            if(node!=-1){
                sol.addNode(node);
            }
        }

    }

    private int getRandomNextSelected(Solution sol){
        List<Integer> unSelected=new ArrayList<>(sol.getUnselectedNodes());
        return unSelected.get(RandomManager.getRandom().nextInt(unSelected.size()));

    }

    public String toString() {
        return getClass().getSimpleName();
    }
}

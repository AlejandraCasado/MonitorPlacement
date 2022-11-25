package MPP.structure;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public class Solution {

    private final Set<Integer> selectedNodes=new HashSet<>();
    private final Set<Integer> selectedNotSupportNodes =new HashSet<>();
    private final Set<Integer> unselectedNodes=new HashSet<>();

    private int numCovered=0;
    private Instance instance;
    private BigInteger numExchanges=BigInteger.valueOf(0);

    private int penalty=0;


    public Solution(Instance instance) {
        this.instance=instance;
        this.penalty=instance.getTotalWeight();
        this.unselectedNodes.addAll(instance.getUnSelectedNodes());
    }

    public Solution(Solution sol) {
        copy(sol);
        numExchanges = sol.numExchanges;
    }


    public void addNode(int node){
        selectedNodes.add(node);
        removePenalty(node);
        //instance.getUnSelectedNodes().remove(node);
        unselectedNodes.remove(node);
        if(!instance.getSupportNodes().contains(node)){
            selectedNotSupportNodes.add(node);
        }
    }

    public void removeNode(int node){ //hay que asegurarse de que el nodo no esta en el set de support antes de hacer esto
        selectedNodes.remove(node);
        addPenalty(node);
        //instance.getUnSelectedNodes().add(node);
        unselectedNodes.add(node);
        selectedNotSupportNodes.remove(node);

    }

    public int getBestNextNode(){
        int bestNode=-1;
        int maxPenalty=0;

        //for (int node:instance.getUnSelectedNodes()){
        for (int node : unselectedNodes) {
            int penalty=0;
            for(int ady:instance.getAdjacencyList()[node]){
                if(!selectedNodes.contains(ady)){
                    penalty+=instance.getAdjacencyMatrix()[ady][node];
                }
            }
            if(penalty>maxPenalty){
                maxPenalty=penalty;
                bestNode=node;
            }
        }

        return bestNode;
    }

    public int getWorstNode(){
        int worstNode=-1;
        int worstCount=0x3f3f3f;

        for(int node : selectedNotSupportNodes){
            if(instance.getAdjacencyList()[node].size()<worstCount && canRemove(node)){
                worstCount=instance.getAdjacencyList()[node].size();
                worstNode=node;
            }
        }

        return worstNode;
    }

    public void copy(Solution sol){

        this.instance=sol.getInstance();
        this.selectedNotSupportNodes.clear();
        this.selectedNotSupportNodes.addAll(sol.getSelectedNotSupportNodes());
        this.selectedNodes.clear();
        this.selectedNodes.addAll(sol.getSelectedNodes());
        this.numCovered=sol.getNumCovered();
        this.penalty=sol.getPenalty();
        this.unselectedNodes.clear();
        this.unselectedNodes.addAll(sol.getUnselectedNodes());

    }

    private void addPenalty(int node){
        int p=0;
        for(int n:instance.getAdjacencyList()[node]){
            if(!selectedNodes.contains(n)){
                p+=instance.getAdjacencyMatrix()[node][n];
            }
        }
        this.penalty+=p;
        //System.out.println(penalty);
    }

    private void removePenalty(int node){
        int p=0;
        for(int n:instance.getAdjacencyList()[node]){
            if(!selectedNodes.contains(n)){
                p+=instance.getAdjacencyMatrix()[node][n];
            }
        }
        this.penalty-=p;
    }

    public boolean canRemove(int node){
        for (int n:instance.getAdjacencyList()[node]){
            if(!selectedNodes.contains(n)){
                return false;
            }
        }
        return true;
    }

    public boolean removeRedundant() {
        boolean removed = true;
        boolean success = false;
        while (removed) {
            removed = false;
            for (int sel : selectedNotSupportNodes) {
                if (canRemove(sel)) {
                    removeNode(sel);
                    success = true;
                    removed = true;
                    break;
                }
            }
        }
        return success;
    }

    public int evaluate(){
        return selectedNodes.size() + this.penalty;
    }

    public float calculateEdgesWithOneNodeSelectedPercentage(){
        int countOneNode=0;
        int countTwoNodes=0;
        for (int v = 0; v < instance.getNumNodes(); v++) {
            for (int adj : instance.getAdjacencyList()[v]) {
                if (adj < v) {
                    if(selectedNodes.contains(adj) && selectedNodes.contains(v)){
                        countTwoNodes++;
                    }
                    else if(selectedNodes.contains(adj) || selectedNodes.contains(v)){
                        countOneNode++;
                    }

                }
            }
        }
        return (countOneNode*1.0f)/(countOneNode+countTwoNodes);
    }

    public void checkPenalty(){
        int p=0;
        for(int i=0;i<instance.getNumNodes();i++){
            for(int j=i+1; j<instance.getNumNodes();j++){
                if(!selectedNodes.contains(i) && !selectedNodes.contains(j)){
                    p+=instance.getAdjacencyMatrix()[i][j];
                }
            }
        }
        System.out.println("PENALTY: "+p);
    }

    public int getNumCovered() {
        return numCovered;
    }

    public Set<Integer> getSelectedNodes() {
        return selectedNodes;
    }

    public Set<Integer> getSelectedNotSupportNodes() {
        return selectedNotSupportNodes;
    }

    public Instance getInstance() {
        return instance;
    }

    public int getPenalty() {
        return this.penalty;
    }

    public Set<Integer> getUnselectedNodes() {
        return unselectedNodes;
    }

    public BigInteger getNumExchanges(){ return numExchanges;}

    public void incrementNumExchanges(){ numExchanges=numExchanges.add(BigInteger.ONE);}
}


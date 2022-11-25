package MPP.algorithm;

import MPP.Main;
import MPP.VNS.BVNS;
import MPP.VNS.VNS;
import MPP.constructive.IConstructive;
import MPP.localSearch.ILocalSearch;
import MPP.structure.Instance;
import MPP.structure.Result;
import MPP.structure.Solution;

public class AlgProcedure implements IAlgorithm{

    private final IConstructive constructive;
    private BVNS vns=null;
    private ILocalSearch localSearch=null;


    public AlgProcedure(IConstructive constructive, BVNS vns) {
        this.constructive = constructive;
        this.vns=vns;
    }

    public AlgProcedure(IConstructive constructive, ILocalSearch localSearch) {
        this.constructive = constructive;
        this.localSearch=localSearch;
    }

    public AlgProcedure(IConstructive constructive) {
        this.constructive = constructive;
    }

    @Override
    public Result execute(Instance instance) {

        System.out.println(instance.getName());
        Result result=new Result(instance.getName());
        float secs;
        long totalTime=System.currentTimeMillis();
        Solution solution=constructive.construct(instance);
        if(localSearch!=null) solution=localSearch.execute(solution,instance);
        if(vns!=null) solution=vns.execute(solution,instance,totalTime);
        totalTime = System.currentTimeMillis() - totalTime;
        secs = totalTime / 1000f;

        result.add("Time", secs);
        System.out.println("Time: "+ secs);
        System.out.println("Penalty: "+ solution.getPenalty());
        result.add("OF", solution.evaluate());
        System.out.println("OF: "+ solution.evaluate());

        return result;
    }

    public String toString() {

        String vnsName= vns==null ? "" : ", " + vns.toString();
        String lsName= localSearch==null ? "" : ", " + localSearch.toString();

        return this.getClass().getSimpleName()+"("+constructive.toString()+vnsName+lsName+")";
    }
}

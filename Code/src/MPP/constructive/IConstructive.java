package MPP.constructive;

import MPP.structure.Instance;
import MPP.structure.Solution;

public interface IConstructive {
    Solution construct(Instance instance);
    String toString();
}

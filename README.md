# Monitor Placement Problem
Several problems are emerging in the context of communication networks and most of them must be solved in reduced computing time since they affect to critical tasks. In this research, the monitor placement problem is tackled. This problem tries to cover the communications of an entire network by locating a monitor in specific nodes of the network, in such a way that every link remains surveyed. Since networks are in continuous evolution, the algorithm responsible to solve the problem requires to be executed in considerably small computing times. In case that a solution cannot be generated in the allowed computing time, a penalty will be assumed for each link uncovered. The problem is addressed by an algorithm based on the Variable Neighborhood Search framework, proposing a novel constructive method, an intelligent local search to optimize the improvement phase, and an intensified shake to guide the search to more promising solutions. The proposed algorithm is compared with the state-of-the-art method over a set of instances derived from real-life networks to prove its performance. The extensive experimental computation shows the contribution of each stage of the proposed algorithm as well as a competitive testing with the best method found in the literature.

## Datasets
We downloaded the instances from different websites, searching for that ones that are used in the previous work. To facilitate this task to future researches, we have a folder with all of them. The set consists of 39 instances and they are in the folder Instances in this repository. 2 of them, like we say in the paper, are too large and we can not include them in the experiments. You can find that instances in a folder named "InstancesTooBig".

## Code
The code is available in the package "Code".

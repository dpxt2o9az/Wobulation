# Rapid Prototype for Percentile based Cluster Wobulation
## Overview
This is a basic research effort focused on the examination of a percentile-based cluster wobulation strategy. 
Focus is strictly on the proposed algorithm’s ability to “follow the data” toward a sufficiently accurate steady state solution.
The initial effort will focus on wobulation of a single cluster.
## Concept
Recursively update cluster descriptions based on a preserved frequency distribution of the relevant underlying data.  
The underlying frequency distribution is also recursively updated in conjunction with the cluster definition, maintaining a predefined, fixed width buffer above and below the current cluster boundaries.  
The use of percentiles about the frequency distribution mean to establish cluster boundaries enables the formation of asymmetric cluster ranges and does not require a normality assumption.
## Assumptions
1. The underlying cluster data is not normally distributed.
2. The underlying data is uniformly distributed within a given bin (small bin assumption.)
## Recursive Update Process
1. Increment frequency count by 1 for the bin containing the new data point. If the data point does not correspond to an existing bin (inside the cluster), discard the data point without update.
2. Update the cluster mean (see formula, below.)
3. Update the cluster min to be the lower boundary of bin containing the (1-p)th percentile of the data below the new mean.
4. Update the cluster max to be the upper boundary of the bin containing the pth percentile of the data above the new mean.
5. Add or delete bins (from the cluster) so that the lowest boundary of the lowest bin is cluster_min-H and the upper boundary of the largest bin is the cluster_max +H.
### Input Parameters
p – percentile to be used to form cluster boundaries, range: (0,1), default = 0.9  
H – horizon to be monitored beyond cluster boundaries  (could be integer used as incr multiplier)  
Incr - Processing increment (default = 0.1)  
### Output Values
$\mu$ – cluster mean  
C_min – lower cluster boundary  
C_max – upper cluster boundary  
### Computational
#### Precision
Cluster boundaries and bin boundaries are in tyeaenths, bin midpoints are of the form xxx.x5
#### Calculating the Mean
![mu = sum(f*m) / N](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "Logo Title Text 1")

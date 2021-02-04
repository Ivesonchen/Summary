/**
 * On an infinite plane, a robot initially stands at (0, 0) and faces north. 
 * The robot can receive one of three instructions:

"G": go straight 1 unit;
"L": turn 90 degrees to the left;
"R": turn 90 degrees to the right.
The robot performs the instructions given in order, and repeats them forever.

Return true if and only if there exists a circle in the plane such that the robot never leaves the circle.

Input: instructions = "GGLLGG"
Output: true
Explanation: The robot moves from (0,0) to (0,2), turns 180 degrees, and then returns to (0,0).
When repeating these instructions, the robot remains in the circle of radius 2 centered at the origin.
 */

/**
 * Calculate the final vector of how the robot travels after executing all instructions once 
 * - it consists of a change in position plus a change in direction.
 * The robot stays in the circle iff (looking at the final vector) 
 * it changes direction (ie. doesn't stay pointing north), or it moves 0.
 */

class Solution {
    public boolean isRobotBounded(String instructions) {
        int[][] direction = {{0,1}, {1,0}, {0, -1}, {-1, 0}};  // 有技巧   向左或者向右的不能市dir 变成负值
        
        int x = 0, y = 0;
        int dir = 0;
        
        for(char i : instructions.toCharArray()){
            if(i == 'L') {
                dir = (dir + 3) % 4;
            } else if (i == 'R'){
                dir = (dir + 1) % 4;
            } else {
                x += direction[dir][0];
                y += direction[dir][1];
            }
        }
        return (x == 0 && y == 0) || (dir != 0);
    }
}
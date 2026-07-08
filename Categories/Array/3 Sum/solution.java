/**
 *  Determine if there exists three elements in a given array that sum to the given target number. 
 *  Return all the triple of values that sums to target.

	Assumptions

	The given array is not null and has length of at least 3
	No duplicate triples should be returned, order of the values in the tuple does not matter
	Examples

	A = {1, 2, 2, 3, 2, 4}, target = 8, return [[1, 3, 4], [2, 2, 4]]
 */

/**
  * 先排序，然后左右夹逼，复杂度 O(n^2)
    这个方法可以推广到k-sum，先排序，然后做k-2次循环，在最内层循环左右夹逼，时间复杂度是 max(O(nLogn), O(n^k-1))
*/

// Tao-Lu
public class Solution {
    public List<List<Integer>> allTriples(int[] array, int target) {
		// Write your solution here
		List<List<Integer>> res = new ArrayList<>();
	
		Arrays.sort(array);
	
		for(int i = 0; i < array.length - 2; i ++){
			if(i > 0 && array[i - 1] == array[i]) continue; // 注意这是用来跳过重复的元素
			int j = i + 1;
			int k = array.length - 1;
	
			while(j < k){
				int sum = array[i] + array[j] + array[k];
				if(array[i] + array[j] + array[k] < target){
					j++;
					while(j < k && array[j] == array[j - 1]) j++;   // 注意这是用来跳过重复的元素
				} else if (array[i] + array[j] + array[k] > target){
					k--;
					while(j < k && array[k] == array[k + 1]) k--;   // 注意这是用来跳过重复的元素
				} else {
					res.add(Arrays.asList(array[i], array[j], array[k]));
					j++;
					k--;
					while(j < k && array[j] == array[j - 1]) j++;   // 注意这是用来跳过重复的元素
					while(j < k && array[k] == array[k + 1]) k--;   // 注意这是用来跳过重复的元素
				}
			}
		}
		return res;
    }
}
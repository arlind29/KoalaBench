/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package new_bench.util;

import java.util.Random;

public class RandomInt
        extends AbstractRandomInt
{
    public RandomInt()  {  }

    @Override
    public int getInt(long seed, int lowValue, int highValue)
    {
        return super.getInt(seed, lowValue, highValue);
    }
    
    public static void main(String[] argz){
    	int min=0;int max=1000; int length = max - min;   
    	
    	RandomInt rnd = new RandomInt();
    	Random r = new Random(10); 
    	for (int i=0;i<3;i++){
    		System.out.println(rnd.getInt(i+1, 0, 1000));     		
    		/*System.out.println(min + (int) (Math.random()*length));
    		System.out.println(r.nextInt()); 
    		System.out.println(min + r.nextInt(length));
    		System.out.println(min + r.nextFloat()*(length));*/
    		System.out.println(); 
    	}
    }
}

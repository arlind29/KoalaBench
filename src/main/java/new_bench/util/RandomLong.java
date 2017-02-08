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

public class RandomLong
{

    Random rnd = new Random(1); 
    /**
     * Creates a new random number generator with the specified seed and
     * specified number of random values per row.
     */
    public RandomLong(){   }

    /**
     * Get a random value between lowValue (inclusive) and highValue (inclusive).
     */
    protected long getLong(long seed, long lowValue, long highValue)
    {
    	rnd.setSeed(seed);
        return lowValue + (long) (rnd.nextFloat() * (highValue - lowValue));  
    }
}

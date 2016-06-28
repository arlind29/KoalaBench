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

public class RandomBoundedLong
{
    private final RandomLong randomLong;
    private final RandomInt randomInt;

    private final long lowValue;
    private final long highValue;

    public RandomBoundedLong(boolean use64Bits, long lowValue, long highValue)
    {
        if (use64Bits) {
            this.randomLong = new RandomLong();
            this.randomInt = null;
        }
        else {
            this.randomLong = null;
            this.randomInt = new RandomInt();
        }

        this.lowValue = lowValue;
        this.highValue = highValue;
    }

    public long getValue(long seed)
    {
        if (randomLong != null) {
            return randomLong.getLong(seed, lowValue, highValue);
        }
        else {
            return randomInt.getInt(seed, (int) lowValue, (int) highValue);
        }
    }

}

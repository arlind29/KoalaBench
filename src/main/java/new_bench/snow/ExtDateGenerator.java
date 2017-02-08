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
package new_bench.snow;

import com.google.common.collect.AbstractIterator;

import java.util.Calendar;
import java.util.Iterator;

import new_bench.util.Distributions;
import new_bench.util.GenerateUtils;
import new_bench.util.RandomAlphaNumeric;
import new_bench.util.RandomBoundedInt;
import new_bench.util.RandomPhoneNumber;
import new_bench.util.RandomString;
import new_bench.util.RandomText;
import new_bench.util.TextPool;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Locale.ENGLISH;
import static new_bench.util.GenerateUtils.calculateRowCount;
import static new_bench.util.GenerateUtils.calculateStartIndex;

public class ExtDateGenerator
        implements Iterable<ExtDate>
{
    public static final int DATE_RANGE = 2557;
    private static final int DATE_MIN = 0; // equiv 1992-01-01


    @Override
    public Iterator<ExtDate> iterator()
    {

        return new ExtDateGeneratorIterator(DATE_MIN, DATE_RANGE); 
    }

    public static class ExtDateGeneratorIterator
            extends AbstractIterator<ExtDate>
    {

        private int index;
        private final int maxDate;

        public ExtDateGeneratorIterator(int minDate, int range)
        {
            this.index = minDate; 
            this.maxDate = minDate+range;
        }

        @Override
        public ExtDate computeNext()
        {
            if (index >= maxDate) { return endOfData();  }
            Calendar calendar = (Calendar) GenerateUtils.MIN_DATE.clone();  
    		calendar.add(Calendar.DATE, index);

            ExtDate date = makeDate(calendar);    		
            index++;
            return date;
        }

        public ExtDate makeDate(Calendar calendar){  
            return new ExtDate(calendar);
        }
    }
    
    public static void main(String[] argz){
    	ExtDateGenerator gen = new ExtDateGenerator();
    	int i=0;
    	for (ExtDate date: gen){
    		System.out.println(date.toJson(null));
    		i++; if (i>4) break; 
    	}
		
    	System.out.println();
    	
        Calendar calendar = (Calendar) GenerateUtils.MIN_DATE.clone();  
		calendar.add(Calendar.DATE, 2);

    	ExtDateGeneratorIterator it = (ExtDateGeneratorIterator) gen.iterator();
		System.out.println(it.makeDate(calendar).toJson(null)); 

    }
}

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

import java.util.Iterator;

import new_bench.util.Distribution;
import new_bench.util.Distributions;
import new_bench.util.RandomText;
import new_bench.util.TextPool;
import static com.google.common.base.Preconditions.checkNotNull;

public class NationGenerator
        implements Iterable<Nation>
{
    private static final int COMMENT_AVERAGE_LENGTH = 72;

    private final Distributions distributions;
    private final TextPool textPool;

    public NationGenerator()
    {
        this(Distributions.getDefaultDistributions(), TextPool.getDefaultTestPool());
    }

    public NationGenerator(Distributions distributions, TextPool textPool)
    {
        this.distributions = checkNotNull(distributions, "distributions is null");
        this.textPool = checkNotNull(textPool, "textPool is null");
    }

    @Override
    public Iterator<Nation> iterator()
    {
        return new NationGeneratorIterator(distributions.getNations(), textPool);
    }

    public static class NationGeneratorIterator
            extends AbstractIterator<Nation>
    {
        private final Distribution nations;
        private final RandomText commentRandom;

        private int index;

        private NationGeneratorIterator(Distribution nations, TextPool textPool)
        {
            this.nations = nations;
            this.commentRandom = new RandomText( textPool, COMMENT_AVERAGE_LENGTH);
        }

        @Override
        protected Nation computeNext()
        {
            if (index >= nations.size()) {return endOfData();}

            Nation nation = makeNation(index); 
            index++;

            return nation;
        }
        
        public Nation makeNation(int index){
        	return new Nation(index,
                    index,
                    nations.getValue(index),
                    nations.getWeight(index),
                    commentRandom.getValue(index));
        }
    }
}

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
import new_bench.util.TextPool;
import static com.google.common.base.Preconditions.checkNotNull;

public class RegionGenerator
        implements Iterable<Region>
{
    //private static final int COMMENT_AVERAGE_LENGTH = 72;

    private final Distributions distributions;
    private final TextPool textPool;

    public RegionGenerator()
    {
        this(Distributions.getDefaultDistributions(), TextPool.getDefaultTestPool());
    }

    public RegionGenerator(Distributions distributions, TextPool textPool)
    {
        this.distributions = checkNotNull(distributions, "distributions is null");
        this.textPool = checkNotNull(textPool, "textPool is null");
    }

    @Override
    public Iterator<Region> iterator()
    {
        return new RegionGeneratorIterator(distributions.getRegions(), textPool);
    }

    public static class RegionGeneratorIterator
            extends AbstractIterator<Region>
    {
        private final Distribution regions;

        private int index;

        private RegionGeneratorIterator(Distribution regions, TextPool textPool)
        {
            this.regions = regions;
        }

        @Override
        protected Region computeNext()
        {
            if (index >= regions.size()) { return endOfData(); }
            
            Region region = makeRegion(index); 

            index++;

            return region;
        }
        public Region makeRegion(int index){
        	return new Region(index,
                    index,
                    regions.getValue(index)); 
        }
    }
}
